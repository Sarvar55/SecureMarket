package com.codems.securemarket.cart.api;

public interface CartFacade {

    CartSnapshot getCart(Long customerId);

    void clear(Long customerId);
}
