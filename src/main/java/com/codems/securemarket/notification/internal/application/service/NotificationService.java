package com.codems.securemarket.notification.internal.application.service;

import com.codems.securemarket.notification.internal.application.port.in.command.*;
import com.codems.securemarket.notification.internal.application.port.in.CreateNotificationUseCase;
import com.codems.securemarket.notification.internal.application.port.in.ManageNotificationsUseCase;
import com.codems.securemarket.notification.internal.application.port.in.query.NotificationView;
import com.codems.securemarket.notification.internal.application.port.out.LoadNotificationsPort;
import com.codems.securemarket.notification.internal.application.port.out.SaveNotificationPort;
import com.codems.securemarket.notification.internal.domain.exception.NotificationNotFoundException;
import com.codems.securemarket.notification.internal.domain.model.Notification;
import java.time.Clock;
import java.util.List;

public final class NotificationService
        implements CreateNotificationUseCase, ManageNotificationsUseCase {

    private final LoadNotificationsPort loadNotificationsPort;
    private final SaveNotificationPort saveNotificationPort;
    private final Clock clock;

    public NotificationService(
            LoadNotificationsPort loadNotificationsPort,
            SaveNotificationPort saveNotificationPort,
            Clock clock
    ) {
        this.loadNotificationsPort = loadNotificationsPort;
        this.saveNotificationPort = saveNotificationPort;
        this.clock = clock;
    }

    @Override
    public void create(CreateNotificationCommand command) {
        saveNotificationPort.save(Notification.create(
                command.recipientId(),
                command.type(),
                command.title(),
                command.message(),
                command.createdAt()
        ));
    }

    @Override
    public List<NotificationView> getForUser(Long recipientId) {
        return loadNotificationsPort.findAllByRecipientId(recipientId)
                .stream()
                .map(NotificationView::from)
                .toList();
    }

    @Override
    public NotificationView markAsRead(Long notificationId, Long recipientId) {
        Notification notification = loadNotificationsPort
                .findByIdAndRecipientId(notificationId, recipientId)
                .orElseThrow(NotificationNotFoundException::new);

        notification.markAsRead(clock.instant());
        return NotificationView.from(saveNotificationPort.save(notification));
    }
}

