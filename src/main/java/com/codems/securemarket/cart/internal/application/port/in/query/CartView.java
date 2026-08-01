package com.codems.securemarket.cart.internal.application.port.in.query;

import com.codems.securemarket.cart.internal.domain.model.Cart;
import com.codems.securemarket.cart.internal.domain.model.CartItem;
import java.util.List;

public record CartView(Long customerId, List<CartItem> items) {
    public static CartView from(Cart cart) {
        return new CartView(cart.getCustomerId(), cart.getItems());
    }
}
