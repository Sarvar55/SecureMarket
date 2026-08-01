package com.codems.securemarket.notification.internal.application.port.in;

import com.codems.securemarket.notification.internal.application.port.in.query.NotificationView;

import java.util.List;

public interface ManageNotificationsUseCase {

    List<NotificationView> getForUser(Long recipientId);

    NotificationView markAsRead(Long notificationId, Long recipientId);
}

