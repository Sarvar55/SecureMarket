# SecureMarket — Modular Monolith E-Commerce Backend

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen.svg)](https://spring.io/)
[![Architecture](https://img.shields.io/badge/Architecture-Hexagonal%20%2B%20DDD-blue.svg)](https://en.wikipedia.org/wiki/Domain-driven_design)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**SecureMarket** is a production-grade e-commerce backend built with **Domain-Driven Design (DDD)** and **Hexagonal Architecture (Ports & Adapters)**. The project is organized as a **Modular Monolith** — each bounded context (Identity, Catalog, Cart, Order, Payment, Notification, Audit) is a self-contained module with no shared internal dependencies, communicating only through well-defined public APIs and domain events.

---

## Table of Contents

1. [Project Structure](#project-structure)
2. [Domain-Driven Design: Core Concepts](#domain-driven-design-core-concepts)
   - [Value Object](#1-value-object)
   - [Entity](#2-entity)
   - [Aggregate Root](#3-aggregate-root)
   - [Domain Service](#4-domain-service)
   - [Application Service](#5-application-service)
3. [Bounded Contexts & Their Purpose](#bounded-contexts--their-purpose)
4. [Event-Driven Communication](#event-driven-communication)
5. [Security Architecture](#security-architecture)
6. [Running the Application](#running-the-application)
7. [API Reference & Testing](#api-reference--testing)
8. [Health & Observability](#health--observability)

---

## Project Structure

The directory layout enforces the architectural rules. Every bounded context follows the exact same internal layout, making the codebase predictable and easy to navigate.

```
com.codems.securemarket
├── shared/                          ← Shared Kernel (cross-cutting concerns)
│   ├── domain/Money.java            ← Cross-context Value Object
│   ├── event/DomainEvent.java       ← Base marker for all domain events
│   ├── exception/                   ← Base exception hierarchy
│   └── web/                         ← Response wrappers, global error handler
│
├── identity/                        ← Bounded Context: Authentication & Users
│   ├── api/                         ← Public API (events emitted by this context)
│   │   └── event/                   ← UserRegisteredEvent, LoginFailedEvent, ...
│   └── internal/                    ← Internal implementation (not accessible by others)
│       ├── domain/
│       │   ├── model/               ← User (Aggregate), Email, Password, Role, AccountStatus
│       │   ├── service/             ← UserDomainService
│       │   └── exception/           ← Domain-specific exceptions
│       ├── application/
│       │   ├── port/in/             ← Use case interfaces (inbound ports)
│       │   │   ├── command/         ← RegisterUserCommand, LoginCommand, ...
│       │   │   └── query/           ← UserView, AccessTokenView (read models)
│       │   ├── port/out/            ← Infrastructure interfaces (outbound ports)
│       │   └── service/             ← RegistrationService, AuthenticationService, ...
│       └── adapter/
│           ├── in/web/              ← REST Controllers (primary adapters)
│           ├── in/event/            ← Event listeners (primary adapters)
│           ├── out/persistence/     ← JPA repositories & mappers (secondary adapters)
│           └── out/token/           ← JWT token adapter (secondary adapter)
│
├── catalog/                         ← Bounded Context: Product & Category Management
├── cart/                            ← Bounded Context: Shopping Cart
├── order/                           ← Bounded Context: Order Lifecycle & Fulfillment
├── payment/                         ← Bounded Context: Payment Processing
├── notification/                    ← Bounded Context: Customer Notifications
└── audit/                           ← Bounded Context: System Audit Trail
```

**The key rule**: `internal/` packages are **invisible** to other bounded contexts. Cross-context calls happen only through public `api/` contracts or domain events — never through internal service classes.

---

## Domain-Driven Design: Core Concepts

### 1. Value Object

A **Value Object** is immutable and has no identity. Two value objects with the same attributes are considered equal. They carry and enforce business rules about their own data.

**`Email.java`** — Ensures an email is always valid and normalized:
```java
public record Email(String value) {
    public Email {
        // Automatically called by the record constructor
        value = value.trim().toLowerCase(Locale.ROOT);
        if (value.isBlank() || value.length() > 254 || !FORMAT.matcher(value).matches()) {
            throw new InvalidEmailException();
        }
    }
}
// Usage: new Email(" John@EXAMPLE.COM ") → value = "john@example.com"
// Usage: new Email("not-an-email") → throws InvalidEmailException immediately
```

**`Money.java`** — Prevents negative amounts and invalid currency codes at the type level:
```java
public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount.signum() < 0) throw new IllegalArgumentException("Money cannot be negative");
        amount = amount.setScale(2, RoundingMode.HALF_UP); // Always 2 decimal places
        currency = currency.trim().toUpperCase(Locale.ROOT);
        if (currency.length() != 3) throw new IllegalArgumentException("Currency must be 3 letters");
    }
    public Money multiply(int quantity) { ... }
}
// Usage in Order: items.stream().map(item -> item.unitPrice().multiply(item.quantity()))
```

> **Why**: It is physically impossible to create an invalid `Email` or `Money` anywhere in the codebase. Business rule violations are caught at the object boundary, not with scattered `if` checks throughout services.

---

### 2. Entity

An **Entity** has a unique identity that persists through state changes. Equality is determined by identity, not attributes.

**`OrderItem.java`** — A child entity within the Order aggregate:
```java
public record OrderItem(Long productId, String productName, Money unitPrice, int quantity) {
    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }
}
```

**`Notification.java`** — Entity with identity managed by the Notification aggregate:
```java
public final class Notification {
    private final Long id;
    private final Long userId;
    private final NotificationType type;
    private final String message;
    private boolean read;
    private final Instant createdAt;

    public void markAsRead() { this.read = true; }
}
```

---

### 3. Aggregate Root

An **Aggregate Root** is the entry point to a cluster of domain objects. All state changes go through the root's methods, which protect invariants. External code never directly mutates internal entities.

**`User.java`** — Guards authentication state and account status rules:
```java
public final class User {
    // private constructor — creation only via factory methods
    public static User register(Email email, String passwordHash, Instant now) {
        return new User(null, email, passwordHash, AccountStatus.ACTIVE, Set.of(Role.CUSTOMER), 0, null, now, now);
    }

    // Business rule: max attempts → auto-lock
    public void recordFailedLogin(int maximumAttempts, Instant lockUntil, Instant now) {
        failedLoginAttempts++;
        if (failedLoginAttempts >= maximumAttempts) {
            status = AccountStatus.LOCKED;
            lockedUntil = lockUntil;
        }
        updatedAt = now;
    }

    // Business rule: lock expires automatically on next auth attempt
    public void ensureCanAuthenticate(Instant now) {
        if (status == AccountStatus.LOCKED && isLockExpired(now)) {
            status = AccountStatus.ACTIVE;
            failedLoginAttempts = 0;
            lockedUntil = null;
        }
        if (status != AccountStatus.ACTIVE) throw new AccountNotActiveException(status);
    }

    // Business rule: cannot transition to invalid state
    public void changeStatus(AccountStatus targetStatus, Instant now) {
        if (status == targetStatus || targetStatus == AccountStatus.PENDING)
            throw new InvalidAccountStatusTransitionException(status, targetStatus);
        status = targetStatus;
        updatedAt = now;
    }
}
```

**`Product.java`** — Guards stock integrity and price validity:
```java
public final class Product {
    public void adjustStock(int quantity, Instant now) {
        if (quantity == 0) throw new InvalidStockQuantityException();
        if (stock + quantity < 0) throw new InsufficientStockException(id); // Can't go negative
        stock += quantity;
        updatedAt = now;
    }

    public void changePrice(Money newPrice, Instant now) {
        unitPrice = requirePositivePrice(newPrice); // Value Object validates itself
        updatedAt = now;
    }
}
```

**`Order.java`** — Guards checkout invariants and fulfillment state machine:
```java
public final class Order {
    public static Order create(Long customerId, List<OrderItem> items, Instant now) {
        if (items.isEmpty()) throw new EmptyCartException();
        String currency = items.get(0).unitPrice().currency();
        if (items.stream().anyMatch(i -> !currency.equals(i.unitPrice().currency())))
            throw new MixedCurrencyException(); // All items must share same currency
        // ... calculate total and construct
    }

    // Strict state machine — only valid transitions allowed
    public void advanceFulfillment(OrderStatus targetStatus, Instant now) {
        OrderStatus expected = switch (status) {
            case PAID -> OrderStatus.PROCESSING;
            case PROCESSING -> OrderStatus.SHIPPED;
            case SHIPPED -> OrderStatus.DELIVERED;
            default -> null;
        };
        if (targetStatus != expected) throw new InvalidOrderStatusTransitionException();
        status = targetStatus;
        updatedAt = now;
    }
}
```

---

### 4. Domain Service

A **Domain Service** holds business logic that doesn't naturally fit inside a single aggregate — logic that involves coordination between aggregates or requires domain knowledge that spans multiple objects.

In this project, `UserDomainService` is intentionally kept minimal because most invariants live directly inside `User`. However, authentication coordination in `AuthenticationService` acts as an application-level domain coordinator, delegating each concern to the aggregate.

---

### 5. Application Service

An **Application Service** orchestrates the use case: it loads aggregates through outbound ports, delegates business logic to the domain, persists results, and publishes events. It has **no business rules of its own**.

**`RegistrationService.java`** — A clean, readable use case:
```java
public final class RegistrationService implements RegisterUserUseCase {

    @Override
    public UserView register(RegisterUserCommand command) {
        Email email = new Email(command.email());       // Value Object validates format
        Password password = new Password(command.password()); // Value Object checks strength

        if (loadUserPort.existsByEmail(email))          // Query outbound port
            throw new EmailAlreadyExistsException();

        Instant now = clock.instant();
        String passwordHash = passwordHasherPort.hash(password); // Outbound port for BCrypt
        User saved = saveUserPort.save(User.register(email, passwordHash, now)); // Domain factory

        eventPublisher.publish(new UserRegisteredEvent(UUID.randomUUID(), saved.getId(),
                saved.getEmail().value(), now));         // Notify other contexts

        return UserView.from(saved);                    // Return read model
    }
}
```

---

## Bounded Contexts & Their Purpose

| Context | Responsibility | Key Aggregates |
|---------|---------------|----------------|
| **Identity** | User registration, JWT auth, account locking, role management | `User` |
| **Catalog** | Product/category lifecycle, price & stock management | `Product`, `Category` |
| **Cart** | Per-customer shopping cart, quantity validation | `Cart` |
| **Order** | Checkout, payment orchestration, fulfillment state machine | `Order` |
| **Payment** | Payment processing simulation, success/failure dispatch | — (event-driven) |
| **Notification** | Customer-facing notifications triggered by domain events | `Notification` |
| **Audit** | Immutable audit trail of all security and business events | `AuditEntry` |

---

## Event-Driven Communication

Bounded contexts are **decoupled via domain events**. A context publishes an event when something significant happens; other contexts listen and react independently — the publisher has no knowledge of listeners.

```
[Identity] ──UserRegisteredEvent──→ [Notification] creates welcome notification
                                 → [Audit]         records registration event

[Order]    ──PaymentRequestedEvent─→ [Payment]      processes the payment

[Payment]  ──PaymentProcessedEvent─→ [Order]        marks order paid/failed
                                  → [Catalog]       restores stock if payment failed
                                  → [Notification]  notifies customer of result
                                  → [Audit]         records payment outcome
```

**Example listener — `PaymentAuditEventListener`**:
```java
@EventListener
public void on(PaymentProcessedEvent event) {
    boolean succeeded = event.status() == PaymentStatus.SUCCEEDED;
    recordAuditUseCase.record(new RecordAuditCommand(
        "PAYMENT",
        succeeded ? AuditActions.PAYMENT_SUCCEEDED : AuditActions.PAYMENT_FAILED,
        event.customerId(), "ORDER", event.orderId(),
        succeeded ? AuditOutcome.SUCCESS : AuditOutcome.FAILURE,
        event.failureReason(), event.occurredAt()
    ));
}
```

**Published domain events across the system**:

| Event | Publisher | Subscribers |
|-------|-----------|-------------|
| `UserRegisteredEvent` | Identity | Notification, Audit |
| `LoginFailedEvent` | Identity | Audit |
| `LoginSucceededEvent` | Identity | Audit |
| `UserStatusChangedEvent` | Identity | Audit, Notification |
| `ProductCreatedEvent` | Catalog | Audit |
| `ProductStockChangedEvent` | Catalog | Audit |
| `CheckoutStartedEvent` | Order | Audit |
| `OrderCreatedEvent` | Order | Audit, Notification |
| `PaymentRequestedEvent` | Order | Payment |
| `PaymentProcessedEvent` | Payment | Order, Catalog, Notification, Audit |
| `OrderStatusChangedEvent` | Order | Audit, Notification |

---

## Security Architecture

- **Stateless JWT Authentication**: No sessions. Every request carries a Bearer token in the `Authorization` header.
- **Role-Based Access Control (RBAC)**: `@PreAuthorize("hasRole('SUPER_ADMIN')")` guards admin endpoints.
- **Account Locking**: Automatic lock after N consecutive failed login attempts, with time-based unlock.
- **Password Security**: BCrypt hashing via `PasswordHasherPort` — the raw password never leaves the application layer.
- **Custom 401/403 Handlers**: `RestAuthenticationEntryPoint` and `RestAccessDeniedHandler` return consistent JSON error responses instead of HTML redirects.

---

## Running the Application

### Prerequisites
- Docker & Docker Compose v2+
- JDK 17+ and Maven 3.9+ (for local development only)

### Environment Variables

Copy the example file and fill in your secrets:
```bash
cp .env.example .env
```

Required variables:
```env
POSTGRES_DB=secure_market
POSTGRES_USER=secure_market
POSTGRES_PASSWORD=your_strong_password
REDIS_PASSWORD=your_redis_password
JWT_SECRET=your_256_bit_secret_at_minimum
```

### Start with Docker Compose
```bash
docker compose up -d --build
```

The compose file starts three services in dependency order:
1. **PostgreSQL** — waits until `pg_isready` passes
2. **Redis** — waits until `redis-cli ping` returns `PONG`
3. **Application** — waits until `/actuator/health` returns `UP`

### Local Development Build
```bash
./mvnw clean package -DskipTests
java -jar target/secure-market-0.0.1-SNAPSHOT.jar
```

---

## API Reference & Testing

**Base URL**: `http://localhost:8080/api/v1`
**Swagger UI**: `http://localhost:8080/swagger-ui.html`

---

### Authentication

#### Register
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "alice@example.com", "password": "StrongPass123!"}'
```
Response `201 Created`:
```json
{ "success": true, "data": { "id": 1, "email": "alice@example.com", "status": "ACTIVE", "roles": ["CUSTOMER"] } }
```

#### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "alice@example.com", "password": "StrongPass123!"}'
```
Response `200 OK`:
```json
{ "success": true, "data": { "token": "eyJhbGc...", "tokenType": "Bearer", "expiresInSeconds": 900 } }
```
> **Save the token**: `TOKEN=eyJhbGc...` — you'll need it for all authenticated requests below.

---

### Catalog (Public — no token needed)

#### List Products
```bash
curl "http://localhost:8080/api/v1/products?page=0&size=20"
```

#### Create Product (Admin only)
```bash
curl -X POST http://localhost:8080/api/v1/admin/products \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"sku":"HEADPHONES-001","name":"Noise Cancelling Headphones","price":199.99,"currency":"USD","stock":50,"categoryId":1}'
```

---

### Cart

#### Add Item
```bash
curl -X POST http://localhost:8080/api/v1/cart/items \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'
```

#### View Cart
```bash
curl http://localhost:8080/api/v1/cart \
  -H "Authorization: Bearer $TOKEN"
```

---

### Orders

#### Checkout (creates order from cart)
```bash
curl -X POST http://localhost:8080/api/v1/orders/checkout \
  -H "Authorization: Bearer $TOKEN"
```
Response `200 OK`:
```json
{ "success": true, "data": { "orderId": 42, "status": "PENDING_PAYMENT", "total": { "amount": 399.98, "currency": "USD" } } }
```

#### List My Orders
```bash
curl http://localhost:8080/api/v1/orders \
  -H "Authorization: Bearer $TOKEN"
```

#### Advance Order Fulfillment (Admin)
```bash
curl -X PATCH http://localhost:8080/api/v1/admin/orders/42/status \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status": "PROCESSING"}'
```

---

### Notifications

#### List My Notifications
```bash
curl http://localhost:8080/api/v1/notifications \
  -H "Authorization: Bearer $TOKEN"
```

---

## Health & Observability

Spring Boot Actuator is exposed on the same port. Docker Compose uses the health endpoint to determine if the application is ready.

| Endpoint | URL | Auth Required |
|----------|-----|:---:|
| Health Check | `GET /actuator/health` | No |
| Info | `GET /actuator/info` | No |
| Metrics | `GET /actuator/metrics` | Yes |

```bash
# Check application health
curl http://localhost:8080/actuator/health
```
Response when healthy:
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "redis": { "status": "UP" },
    "diskSpace": { "status": "UP" }
  }
}
```

---

## Design Principles Summary

| Principle | How It's Applied |
|-----------|-----------------|
| **No anemic domain** | Aggregates (`User`, `Product`, `Order`) own all business rules; services only orchestrate |
| **Immutable Value Objects** | `Email`, `Password`, `Money` — invalid states are unrepresentable |
| **Private constructors** | Aggregates use factory methods (`create`, `restore`) — construction is always intentional |
| **Command/Query separation** | Inbound ports split into `command/` (writes) and `query/` (reads) packages |
| **Dependency inversion** | Application layer depends on port interfaces, never on JPA/Spring/Redis directly |
| **Event-driven decoupling** | Bounded contexts share zero internal code; they communicate only via public events |
| **Testability** | No `@Transactional` in domain/services; `Clock` injected for deterministic time-based tests |

---

*Built with precision and care. MIT License.*
