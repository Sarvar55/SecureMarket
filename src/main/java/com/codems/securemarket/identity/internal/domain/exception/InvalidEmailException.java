package com.codems.securemarket.identity.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class InvalidEmailException extends DomainException {

    private static final String CODE = "IDENTITY_INVALID_EMAIL";

    public InvalidEmailException() {
        super(CODE, ErrorCategory.VALIDATION, "Email address is invalid");
    }
}
