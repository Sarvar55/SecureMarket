package com.codems.securemarket.catalog.api;

public record StockRequest(Long productId, int quantity) {

    public StockRequest {
        if (productId == null) {
            throw new IllegalArgumentException("productId must not be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}

