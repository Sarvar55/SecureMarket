package com.codems.securemarket.catalog.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class InvalidStockQuantityException extends DomainException {
    public InvalidStockQuantityException() {
        super("CATALOG_INVALID_STOCK_QUANTITY", ErrorCategory.VALIDATION, "Stock quantity is invalid");
    }
}

