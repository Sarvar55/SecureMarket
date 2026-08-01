package com.codems.securemarket.order.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class OrderNotFoundException extends DomainException {
    public OrderNotFoundException() {
        super("ORDER_NOT_FOUND", ErrorCategory.NOT_FOUND, "Order was not found");
    }
}
