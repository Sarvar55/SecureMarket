package com.codems.securemarket.notification.internal.adapter.out.persistence;

import com.codems.securemarket.notification.internal.application.port.out.LoadNotificationsPort;
import com.codems.securemarket.notification.internal.application.port.out.SaveNotificationPort;
import com.codems.securemarket.notification.internal.domain.exception.NotificationNotFoundException;
import com.codems.securemarket.notification.internal.domain.model.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
class NotificationPersistenceAdapter
        implements LoadNotificationsPort, SaveNotificationPort {

    private final JpaNotificationRepository repository;

    NotificationPersistenceAdapter(JpaNotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Notification> findAllByRecipientId(Long recipientId) {
        return repository.findAllByRecipientIdOrderByCreatedAtDesc(recipientId)
                .stream()
                .map(NotificationEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Notification> findByIdAndRecipientId(
            Long notificationId,
            Long recipientId
    ) {
        return repository.findByIdAndRecipientId(notificationId, recipientId)
                .map(NotificationEntity::toDomain);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Notification save(Notification notification) {
        NotificationEntity entity;

        if (notification.getId() == null) {
            entity = NotificationEntity.create(notification);
        } else {
            entity = repository.findByIdAndRecipientId(
                    notification.getId(),
                    notification.getRecipientId()
            ).orElseThrow(NotificationNotFoundException::new);
            entity.updateFrom(notification);
        }

        return repository.save(entity).toDomain();
    }
}
