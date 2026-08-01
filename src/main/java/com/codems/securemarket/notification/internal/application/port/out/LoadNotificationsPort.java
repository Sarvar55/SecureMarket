package com.codems.securemarket.notification.internal.application.port.out;

import com.codems.securemarket.notification.internal.domain.model.Notification;
import java.util.List;
import java.util.Optional;

public interface LoadNotificationsPort {

    List<Notification> findAllByRecipientId(Long recipientId);

    Optional<Notification> findByIdAndRecipientId(Long notificationId, Long recipientId);
}

