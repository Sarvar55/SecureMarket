package com.codems.securemarket.identity.internal.domain.exception;

import com.codems.securemarket.identity.internal.domain.model.AccountStatus;
import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;
import java.util.Map;

public final class InvalidAccountStatusTransitionException extends DomainException {

    private static final String CODE = "IDENTITY_INVALID_ACCOUNT_STATUS_TRANSITION";

    public InvalidAccountStatusTransitionException(
            AccountStatus currentStatus,
            AccountStatus targetStatus
    ) {
        super(
                CODE,
                ErrorCategory.BUSINESS_RULE,
                "Account status cannot be changed from %s to %s"
                        .formatted(currentStatus, targetStatus),
                Map.of(
                        "currentStatus", currentStatus.name(),
                        "targetStatus", targetStatus.name()
                )
        );
    }
}
