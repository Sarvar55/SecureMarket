package com.codems.securemarket.order.internal.domain.model;

import com.codems.securemarket.order.internal.domain.exception.EmptyCartException;
import com.codems.securemarket.order.internal.domain.exception.MixedCurrencyException;
import com.codems.securemarket.order.internal.domain.exception.InvalidOrderStatusTransitionException;
import com.codems.securemarket.shared.domain.Money;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class Order {
    private final Long id;
    private final Long customerId;
    private final List<OrderItem> items;
    private final Money total;
    private OrderStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private Order(
            Long id, Long customerId, List<OrderItem> items, Money total,
            OrderStatus status, Instant createdAt, Instant updatedAt
    ) {
        this.id = id;
        this.customerId = Objects.requireNonNull(customerId);
        if (items == null || items.isEmpty()) throw new EmptyCartException();
        this.items = List.copyOf(items);
        this.total = Objects.requireNonNull(total);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static Order create(Long customerId, List<OrderItem> items, Instant now) {
        if (items == null || items.isEmpty()) throw new EmptyCartException();
        String currency = items.get(0).unitPrice().currency();
        if (items.stream().anyMatch(item -> !currency.equals(item.unitPrice().currency()))) {
            throw new MixedCurrencyException();
        }
        BigDecimal amount = items.stream()
                .map(item -> item.subtotal().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Order(
                null, customerId, items, new Money(amount, currency),
                OrderStatus.PENDING_PAYMENT, now, now
        );
    }

    public static Order restore(
            Long id, Long customerId, List<OrderItem> items, Money total,
            OrderStatus status, Instant createdAt, Instant updatedAt
    ) {
        return new Order(Objects.requireNonNull(id), customerId, items, total, status, createdAt, updatedAt);
    }

    public boolean isPaymentPending() { return status == OrderStatus.PENDING_PAYMENT; }

    public void markPaid(Instant now) {
        if (!isPaymentPending()) return;
        status = OrderStatus.PAID;
        updatedAt = Objects.requireNonNull(now);
    }

    public void markPaymentFailed(Instant now) {
        if (!isPaymentPending()) return;
        status = OrderStatus.PAYMENT_FAILED;
        updatedAt = Objects.requireNonNull(now);
    }

    public void advanceFulfillment(OrderStatus targetStatus, Instant now) {
        OrderStatus expectedStatus = switch (status) {
            case PAID -> OrderStatus.PROCESSING;
            case PROCESSING -> OrderStatus.SHIPPED;
            case SHIPPED -> OrderStatus.DELIVERED;
            default -> null;
        };

        if (targetStatus != expectedStatus) {
            throw new InvalidOrderStatusTransitionException();
        }

        status = targetStatus;
        updatedAt = Objects.requireNonNull(now);
    }

    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public List<OrderItem> getItems() { return items; }
    public Money getTotal() { return total; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
