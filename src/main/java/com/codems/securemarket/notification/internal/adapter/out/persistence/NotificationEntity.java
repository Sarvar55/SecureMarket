package com.codems.securemarket.notification.internal.adapter.out.persistence;

import com.codems.securemarket.notification.internal.domain.model.Notification;
import com.codems.securemarket.notification.internal.domain.model.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;

@Entity
@Table(name = "notifications")
class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private NotificationType type;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "read_at")
    private Instant readAt;

    @Version
    private long version;

    protected NotificationEntity() {
    }

    static NotificationEntity create(Notification notification) {
        var entity = new NotificationEntity();
        entity.updateFrom(notification);
        return entity;
    }

    void updateFrom(Notification notification) {
        recipientId = notification.getRecipientId();
        type = notification.getType();
        title = notification.getTitle();
        message = notification.getMessage();
        createdAt = notification.getCreatedAt();
        readAt = notification.getReadAt();
    }

    Notification toDomain() {
        return Notification.restore(
                id,
                recipientId,
                type,
                title,
                message,
                createdAt,
                readAt
        );
    }
}
