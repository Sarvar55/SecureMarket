package com.codems.securemarket.cart.internal.domain.model;

import com.codems.securemarket.cart.internal.domain.exception.InvalidCartQuantityException;

public record CartItem(Long productId, int quantity) {
    public CartItem {
        if (productId == null) {
            throw new IllegalArgumentException("productId must not be null");
        }
        if (quantity <= 0) {
            throw new InvalidCartQuantityException();
        }
    }
}
