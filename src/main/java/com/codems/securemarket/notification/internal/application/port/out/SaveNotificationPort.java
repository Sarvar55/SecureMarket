package com.codems.securemarket.notification.internal.application.port.out;

import com.codems.securemarket.notification.internal.domain.model.Notification;

public interface SaveNotificationPort {

    Notification save(Notification notification);
}

