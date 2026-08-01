package com.codems.securemarket.catalog.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class ProductUnavailableException extends DomainException {
    public ProductUnavailableException(Long productId) {
        super("CATALOG_PRODUCT_UNAVAILABLE", ErrorCategory.CONFLICT, "Product is not available");
    }
}

