package com.codems.securemarket.catalog.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class InvalidProductPriceException extends DomainException {
    public InvalidProductPriceException() {
        super("CATALOG_INVALID_PRICE", ErrorCategory.VALIDATION, "Product price must be positive");
    }
}

