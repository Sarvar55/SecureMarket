package com.codems.securemarket.catalog.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class InsufficientStockException extends DomainException {
    public InsufficientStockException(Long productId) {
        super("CATALOG_INSUFFICIENT_STOCK", ErrorCategory.CONFLICT, "Product stock is insufficient");
    }
}

