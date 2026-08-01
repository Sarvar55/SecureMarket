package com.codems.securemarket.catalog.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(Long id) {
        super("CATALOG_PRODUCT_NOT_FOUND", ErrorCategory.NOT_FOUND, "Product was not found");
    }
}

