package com.codems.securemarket.identity.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class UserMustHaveRoleException extends DomainException {

    private static final String CODE = "IDENTITY_USER_MUST_HAVE_ROLE";

    public UserMustHaveRoleException() {
        super(CODE, ErrorCategory.BUSINESS_RULE, "User must have at least one role");
    }
}
