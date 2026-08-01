package com.codems.securemarket.catalog.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class CategoryNotFoundException extends DomainException {
    public CategoryNotFoundException(Long id) {
        super("CATALOG_CATEGORY_NOT_FOUND", ErrorCategory.NOT_FOUND, "Category was not found");
    }
}

