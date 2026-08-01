package com.codems.securemarket.identity.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException() {
        super("IDENTITY_INVALID_CREDENTIALS", ErrorCategory.UNAUTHORIZED, "Email or password is incorrect");
    }
}

