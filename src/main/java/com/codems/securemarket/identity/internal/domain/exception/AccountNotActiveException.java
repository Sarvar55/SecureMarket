package com.codems.securemarket.identity.internal.domain.exception;

import com.codems.securemarket.identity.internal.domain.model.AccountStatus;
import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;
import java.util.Map;

public final class AccountNotActiveException extends DomainException {

    private static final String CODE = "IDENTITY_ACCOUNT_NOT_ACTIVE";

    public AccountNotActiveException(AccountStatus currentStatus) {
        super(
                CODE,
                ErrorCategory.UNAUTHORIZED,
                "Account is not active",
                Map.of("currentStatus", currentStatus.name()));
    }
}
