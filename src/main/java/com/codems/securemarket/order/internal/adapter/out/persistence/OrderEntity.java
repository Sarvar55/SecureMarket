package com.codems.securemarket.order.internal.adapter.out.persistence;

import com.codems.securemarket.order.internal.domain.model.Order;
import com.codems.securemarket.order.internal.domain.model.OrderStatus;
import com.codems.securemarket.shared.domain.Money;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @OrderColumn(name = "line_number")
    private List<OrderItemValue> items = new ArrayList<>();

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private long version;

    protected OrderEntity() {
    }

    static OrderEntity from(Order order) {
        var entity = new OrderEntity();
        entity.id = order.getId();
        entity.updateFrom(order);
        return entity;
    }

    void updateFrom(Order order) {
        customerId = order.getCustomerId();
        items.clear();
        items.addAll(order.getItems().stream().map(OrderItemValue::from).toList());
        totalAmount = order.getTotal().amount();
        currency = order.getTotal().currency();
        status = order.getStatus();
        createdAt = order.getCreatedAt();
        updatedAt = order.getUpdatedAt();
    }

    Order toDomain() {
        return Order.restore(
                id, customerId, items.stream().map(OrderItemValue::toDomain).toList(),
                new Money(totalAmount, currency), status, createdAt, updatedAt
        );
    }
}
