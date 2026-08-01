package com.codems.securemarket.identity.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;
import java.util.Map;

public final class UserNotFoundException extends DomainException {

    public UserNotFoundException(Long userId) {
        super(
                "IDENTITY_USER_NOT_FOUND",
                ErrorCategory.NOT_FOUND,
                "User was not found",
                Map.of("userId", userId)
        );
    }
}

