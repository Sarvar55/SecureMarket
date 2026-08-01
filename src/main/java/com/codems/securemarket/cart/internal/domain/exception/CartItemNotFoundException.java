package com.codems.securemarket.cart.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class CartItemNotFoundException extends DomainException {
    public CartItemNotFoundException() {
        super("CART_ITEM_NOT_FOUND", ErrorCategory.NOT_FOUND, "Cart item was not found");
    }
}
