package com.codems.securemarket.catalog.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class DuplicateCategorySlugException extends DomainException {
    public DuplicateCategorySlugException() {
        super("CATALOG_DUPLICATE_CATEGORY_SLUG", ErrorCategory.CONFLICT, "Category slug already exists");
    }
}

