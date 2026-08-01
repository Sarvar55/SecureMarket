package com.codems.securemarket.catalog.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class DuplicateSkuException extends DomainException {
    public DuplicateSkuException() {
        super("CATALOG_DUPLICATE_SKU", ErrorCategory.CONFLICT, "Product SKU already exists");
    }
}

