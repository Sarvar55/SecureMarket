package com.codems.securemarket.cart.internal.application.port.out;

import com.codems.securemarket.cart.internal.domain.model.Cart;
import java.util.Optional;

public interface LoadCartPort {
    Optional<Cart> findByCustomerId(Long customerId);
}
