package com.codems.securemarket.cart.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class RequestedQuantityUnavailableException extends DomainException {
    public RequestedQuantityUnavailableException() {
        super("CART_QUANTITY_UNAVAILABLE", ErrorCategory.CONFLICT, "Requested quantity is unavailable");
    }
}
