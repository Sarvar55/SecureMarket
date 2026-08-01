package com.codems.securemarket.cart.internal.application.port.out;

public interface CheckProductQuantityPort {
    void check(Long productId, int quantity);
}
