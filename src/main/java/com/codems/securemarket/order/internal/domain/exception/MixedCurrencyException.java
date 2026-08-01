package com.codems.securemarket.order.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class MixedCurrencyException extends DomainException {
    public MixedCurrencyException() {
        super("ORDER_MIXED_CURRENCY", ErrorCategory.BUSINESS_RULE, "All order items must use the same currency");
    }
}
