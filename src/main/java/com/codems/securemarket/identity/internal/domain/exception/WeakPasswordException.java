package com.codems.securemarket.identity.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class WeakPasswordException extends DomainException {

    private static final String CODE = "IDENTITY_WEAK_PASSWORD";

    public WeakPasswordException() {
        super(CODE, ErrorCategory.VALIDATION, "Password must contain between 12 and 72 characters");
    }
}
