package com.codems.securemarket.cart.internal.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codems.securemarket.cart.internal.domain.exception.CartItemNotFoundException;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class CartTest {
    private static final Instant NOW = Instant.parse("2026-08-01T10:00:00Z");

    @Test
    void puttingSameProductReplacesQuantity() {
        Cart cart = Cart.create(5L, NOW);
        cart.put(10L, 2, NOW);
        cart.put(10L, 4, NOW.plusSeconds(1));

        assertThat(cart.getItems()).containsExactly(new CartItem(10L, 4));
    }

    @Test
    void removingMissingItemFails() {
        Cart cart = Cart.create(5L, NOW);

        assertThatThrownBy(() -> cart.remove(10L, NOW))
                .isInstanceOf(CartItemNotFoundException.class);
    }
}
