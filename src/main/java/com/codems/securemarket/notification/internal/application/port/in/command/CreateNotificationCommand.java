package com.codems.securemarket.notification.internal.application.port.in.command;

import com.codems.securemarket.notification.internal.domain.model.NotificationType;
import java.time.Instant;

public record CreateNotificationCommand(
        Long recipientId,
        NotificationType type,
        String title,
        String message,
        Instant createdAt
) {
}

