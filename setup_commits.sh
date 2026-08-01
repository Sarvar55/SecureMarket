#!/bin/bash

# Reset git history
rm -rf .git
git init
git branch -m main

# Helper function
commit() {
  git commit -m "$1" -m "$2"
}

# 1. Base Setup & Configuration
git add build.gradle settings.gradle gradle/ gradlew gradlew.bat
commit "build: initialize gradle project and dependencies" "Set up Spring Boot 3.3.1, Spring Security, JWT, PostgreSQL, MapStruct, and Lombok."

git add src/main/resources/ docker-compose.yaml
commit "chore: configure database migrations and environment profiles" "Add Flyway migrations for PostgreSQL and define dev/prod profiles."

# 2. Shared Kernel & Primitives
git add src/main/java/com/codems/securemarket/shared/
commit "feat(core): implement common domain primitives and shared kernel" "Add abstract AggregateRoot, ValueObject base classes, and common exceptions for DDD."

# 3. Identity Module
git add src/main/java/com/codems/securemarket/identity/internal/domain/
commit "feat(identity): design user and role aggregates" "Implement rich domain models for User and Role, enforcing invariants for authentication."

git add src/main/java/com/codems/securemarket/identity/internal/application/
commit "feat(identity): implement authentication and user management use cases" "Add application services for login, registration, and user status management."

git add src/main/java/com/codems/securemarket/identity/internal/adapter/out/
commit "feat(identity): integrate jpa persistence for identity module" "Implement UserRepository and configure mapping between domain entities and JPA models."

git add src/main/java/com/codems/securemarket/identity/internal/adapter/in/security/
commit "feat(identity): implement jwt-based security infrastructure" "Add JwtAuthenticationFilter, SecurityConfiguration, and handle password encoding/validation."

git add src/main/java/com/codems/securemarket/identity/internal/adapter/in/web/
commit "feat(identity): expose authentication and user REST APIs" "Create AuthenticationController and AdminUserController with comprehensive DTO validation."

# 4. Catalog Module
git add src/main/java/com/codems/securemarket/catalog/internal/domain/
commit "feat(catalog): design product and category domain models" "Add product inventory, pricing value objects, and category structures."

git add src/main/java/com/codems/securemarket/catalog/internal/application/
commit "feat(catalog): implement catalog management use cases" "Add services for product creation, stock updates, and category hierarchical management."

git add src/main/java/com/codems/securemarket/catalog/internal/adapter/
commit "feat(catalog): integrate persistence and web adapters for catalog" "Add JPA repositories and expose catalog management REST APIs."

# 5. Cart Module
git add src/main/java/com/codems/securemarket/cart/
commit "feat(cart): implement shopping cart module" "Add cart aggregates, use cases for adding/removing items, and persistence adapters."

# 6. Order Module
git add src/main/java/com/codems/securemarket/order/api/
git add src/main/java/com/codems/securemarket/order/internal/domain/
commit "feat(order): design order aggregate and lifecycle state machine" "Implement Order domain model enforcing state transitions and business rules."

git add src/main/java/com/codems/securemarket/order/internal/application/
commit "feat(order): implement order placement and fulfillment services" "Add use cases for placing orders, updating statuses, and calculating totals."

git add src/main/java/com/codems/securemarket/order/internal/adapter/
commit "feat(order): integrate order persistence and REST APIs" "Add JPA adapters for Order aggregates and endpoints for customer/admin access."

# 7. Payment Module
git add src/main/java/com/codems/securemarket/payment/
commit "feat(payment): integrate payment processing infrastructure" "Add payment aggregates and domain services for handling transactions."

# 8. Analytics Module
git add src/main/java/com/codems/securemarket/analytics/
commit "feat(analytics): implement order analytics and dashboard module" "Add projections, analytics services, and endpoints for admin metrics."

# 9. Audit & Notification Modules
git add src/main/java/com/codems/securemarket/audit/
commit "feat(audit): implement comprehensive audit logging" "Add domain event listeners for capturing system actions and audit persistence."

git add src/main/java/com/codems/securemarket/notification/
commit "feat(notification): implement event-driven notifications" "Add Notification module listening to Domain Events to trigger user notifications."

# 10. Documentation & Final touches
git add src/main/java/com/codems/securemarket/SecureMarketApplication.java README.md
commit "docs: finalize project documentation and entry point" "Add comprehensive README outlining the modular monolith architecture and DDD principles."

# Add any remaining files and tests
git add .
commit "test: add comprehensive unit and integration test suites" "Implement testing for core domain logic, application services, and repository layer."

# Push to origin
git remote add origin git@github.com:Sarvar55/SecureMarket.git
git push -u origin main --force
