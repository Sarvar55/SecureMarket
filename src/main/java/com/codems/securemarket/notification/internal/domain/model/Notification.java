package com.codems.securemarket.notification.internal.domain.model;

import java.time.Instant;
import java.util.Objects;

public final class Notification {

    private final Long id;
    private final Long recipientId;
    private final NotificationType type;
    private final String title;
    private final String message;
    private final Instant createdAt;
    private Instant readAt;

    private Notification(
            Long id,
            Long recipientId,
            NotificationType type,
            String title,
            String message,
            Instant createdAt,
            Instant readAt
    ) {
        this.id = id;
        this.recipientId = Objects.requireNonNull(recipientId);
        this.type = Objects.requireNonNull(type);
        this.title = requireText(title, "title");
        this.message = requireText(message, "message");
        this.createdAt = Objects.requireNonNull(createdAt);
        this.readAt = readAt;
    }

    public static Notification create(
            Long recipientId,
            NotificationType type,
            String title,
            String message,
            Instant createdAt
    ) {
        return new Notification(
                null,
                recipientId,
                type,
                title,
                message,
                createdAt,
                null
        );
    }

    public static Notification restore(
            Long id,
            Long recipientId,
            NotificationType type,
            String title,
            String message,
            Instant createdAt,
            Instant readAt
    ) {
        return new Notification(
                Objects.requireNonNull(id),
                recipientId,
                type,
                title,
                message,
                createdAt,
                readAt
        );
    }

    public void markAsRead(Instant now) {
        if (readAt == null) {
            readAt = Objects.requireNonNull(now);
        }
    }

    public boolean isRead() {
        return readAt != null;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }

    public Long getId() {
        return id;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getReadAt() {
        return readAt;
    }
}

