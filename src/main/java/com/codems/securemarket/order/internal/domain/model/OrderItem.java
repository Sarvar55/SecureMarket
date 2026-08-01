package com.codems.securemarket.order.internal.domain.model;

import com.codems.securemarket.shared.domain.Money;
import java.util.Objects;

public record OrderItem(
        Long productId,
        String productName,
        Money unitPrice,
        int quantity
) {
    public OrderItem {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(unitPrice);
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("productName must not be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }
}
