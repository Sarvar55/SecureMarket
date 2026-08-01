package com.codems.securemarket.order.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class EmptyCartException extends DomainException {
    public EmptyCartException() {
        super("ORDER_EMPTY_CART", ErrorCategory.BUSINESS_RULE, "Cart cannot be empty");
    }
}
