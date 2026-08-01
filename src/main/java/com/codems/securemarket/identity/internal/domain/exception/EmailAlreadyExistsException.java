package com.codems.securemarket.identity.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class EmailAlreadyExistsException extends DomainException {

    public EmailAlreadyExistsException() {
        super("IDENTITY_EMAIL_ALREADY_EXISTS", ErrorCategory.CONFLICT, "Email address is already registered");
    }
}

