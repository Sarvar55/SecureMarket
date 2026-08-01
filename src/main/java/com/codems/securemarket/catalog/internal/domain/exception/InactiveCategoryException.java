package com.codems.securemarket.catalog.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class InactiveCategoryException extends DomainException {
    public InactiveCategoryException() {
        super("CATALOG_INACTIVE_CATEGORY", ErrorCategory.BUSINESS_RULE, "Category is not active");
    }
}

