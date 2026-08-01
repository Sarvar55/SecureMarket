package com.codems.securemarket.order.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class InvalidOrderStatusTransitionException extends DomainException {
    public InvalidOrderStatusTransitionException() {
        super(
                "ORDER_INVALID_STATUS_TRANSITION",
                ErrorCategory.CONFLICT,
                "Order status transition is not allowed"
        );
    }
}
