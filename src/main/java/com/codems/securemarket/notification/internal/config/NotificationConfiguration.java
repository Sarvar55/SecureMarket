package com.codems.securemarket.notification.internal.config;

import com.codems.securemarket.notification.internal.application.port.in.CreateNotificationUseCase;
import com.codems.securemarket.notification.internal.application.port.in.ManageNotificationsUseCase;
import com.codems.securemarket.notification.internal.application.port.out.LoadNotificationsPort;
import com.codems.securemarket.notification.internal.application.port.out.SaveNotificationPort;
import com.codems.securemarket.notification.internal.application.service.NotificationService;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfiguration {

    @Bean
    NotificationService notificationService(
            LoadNotificationsPort loadNotificationsPort,
            SaveNotificationPort saveNotificationPort,
            Clock clock
    ) {
        return new NotificationService(
                loadNotificationsPort,
                saveNotificationPort,
                clock
        );
    }

    @Bean
    CreateNotificationUseCase createNotificationUseCase(
            NotificationService notificationService
    ) {
        return notificationService;
    }

    @Bean
    ManageNotificationsUseCase manageNotificationsUseCase(
            NotificationService notificationService
    ) {
        return notificationService;
    }
}

