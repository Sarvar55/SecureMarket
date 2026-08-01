package com.codems.securemarket.notification.internal.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findAllByRecipientIdOrderByCreatedAtDesc(Long recipientId);

    Optional<NotificationEntity> findByIdAndRecipientId(Long id, Long recipientId);
}
