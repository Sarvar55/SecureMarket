package com.codems.securemarket.cart.internal.application.port.out;

import com.codems.securemarket.cart.internal.domain.model.Cart;

public interface SaveCartPort {
    Cart save(Cart cart);
}
