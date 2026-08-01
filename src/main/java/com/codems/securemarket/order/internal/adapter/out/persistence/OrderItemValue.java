package com.codems.securemarket.order.internal.adapter.out.persistence;

import com.codems.securemarket.order.internal.domain.model.OrderItem;
import com.codems.securemarket.shared.domain.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
class OrderItemValue {
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 180)
    private String productName;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private int quantity;

    protected OrderItemValue() {
    }

    static OrderItemValue from(OrderItem item) {
        var value = new OrderItemValue();
        value.productId = item.productId();
        value.productName = item.productName();
        value.unitPrice = item.unitPrice().amount();
        value.currency = item.unitPrice().currency();
        value.quantity = item.quantity();
        return value;
    }

    OrderItem toDomain() {
        return new OrderItem(productId, productName, new Money(unitPrice, currency), quantity);
    }
}
