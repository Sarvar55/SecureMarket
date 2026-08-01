package com.codems.securemarket.notification.internal.application.port.in;

import com.codems.securemarket.notification.internal.application.port.in.command.CreateNotificationCommand;

public interface CreateNotificationUseCase {

    void create(CreateNotificationCommand command);
}

