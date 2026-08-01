package com.codems.securemarket.notification.internal.domain.exception;

import com.codems.securemarket.shared.exception.DomainException;
import com.codems.securemarket.shared.exception.ErrorCategory;

public final class NotificationNotFoundException extends DomainException {

    public NotificationNotFoundException() {
        super(
                "NOTIFICATION_NOT_FOUND",
                ErrorCategory.NOT_FOUND,
                "Notification was not found"
        );
    }
}

