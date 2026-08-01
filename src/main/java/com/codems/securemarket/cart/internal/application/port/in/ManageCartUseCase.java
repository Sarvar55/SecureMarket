package com.codems.securemarket.cart.internal.application.port.in;

import com.codems.securemarket.cart.internal.application.port.in.query.CartView;

import com.codems.securemarket.cart.internal.application.port.in.command.AddCartItemCommand;
import com.codems.securemarket.cart.internal.application.port.in.command.ChangeCartItemCommand;

public interface ManageCartUseCase {
    CartView get(Long customerId);
    CartView add(AddCartItemCommand command);
    CartView change(ChangeCartItemCommand command);
    CartView remove(Long customerId, Long productId);
    void clear(Long customerId);
}
