package com.codems.securemarket.notification.internal.application.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.codems.securemarket.notification.internal.application.port.out.LoadNotificationsPort;
import com.codems.securemarket.notification.internal.application.port.out.SaveNotificationPort;
import com.codems.securemarket.notification.internal.domain.model.Notification;
import com.codems.securemarket.notification.internal.domain.model.NotificationType;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class NotificationServiceTest {

    @Test
    void userCanMarkOwnNotificationAsRead() {
        Instant now = Instant.parse("2026-08-01T09:00:00Z");
        LoadNotificationsPort loadPort = mock(LoadNotificationsPort.class);
        SaveNotificationPort savePort = mock(SaveNotificationPort.class);
        var notification = Notification.restore(
                1L,
                10L,
                NotificationType.WELCOME,
                "Welcome",
                "Account created",
                now.minusSeconds(60),
                null
        );

        when(loadPort.findByIdAndRecipientId(1L, 10L))
                .thenReturn(Optional.of(notification));
        when(savePort.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var service = new NotificationService(
                loadPort,
                savePort,
                Clock.fixed(now, ZoneOffset.UTC)
        );

        var result = service.markAsRead(1L, 10L);

        assertTrue(result.read());
    }
}

