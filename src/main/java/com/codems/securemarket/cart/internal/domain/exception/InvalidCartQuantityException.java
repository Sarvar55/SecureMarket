package com.codems.securemarket.cart.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class InvalidCartQuantityException extends DomainException {
    public InvalidCartQuantityException() {
        super("CART_INVALID_QUANTITY", ErrorCategory.VALIDATION, "Cart quantity must be positive");
    }
}
