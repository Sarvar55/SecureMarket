package com.codems.securemarket.notification.internal.application.port.in.query;

import com.codems.securemarket.notification.internal.domain.model.Notification;
import com.codems.securemarket.notification.internal.domain.model.NotificationType;
import java.time.Instant;

public record NotificationView(
        Long id,
        NotificationType type,
        String title,
        String message,
        boolean read,
        Instant createdAt,
        Instant readAt
) {
    public static NotificationView from(Notification notification) {
        return new NotificationView(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt(),
                notification.getReadAt()
        );
    }
}

