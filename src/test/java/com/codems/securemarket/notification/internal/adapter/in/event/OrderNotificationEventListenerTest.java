package com.codems.securemarket.notification.internal.adapter.in.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.codems.securemarket.notification.internal.application.port.in.CreateNotificationUseCase;
import com.codems.securemarket.notification.internal.application.port.in.command.CreateNotificationCommand;
import com.codems.securemarket.notification.internal.domain.model.NotificationType;
import com.codems.securemarket.order.api.event.OrderStatusChangedEvent;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class OrderNotificationEventListenerTest {

    @Test
    void createsShippedNotificationForCustomer() {
        CreateNotificationUseCase useCase = mock(CreateNotificationUseCase.class);
        var listener = new OrderNotificationEventListener(useCase);

        listener.on(new OrderStatusChangedEvent(
                UUID.randomUUID(), 100L, 7L,
                "PROCESSING", "SHIPPED", 3L,
                Instant.parse("2026-08-01T10:00:00Z")
        ));

        var captor = ArgumentCaptor.forClass(CreateNotificationCommand.class);
        verify(useCase).create(captor.capture());
        assertThat(captor.getValue().recipientId()).isEqualTo(7L);
        assertThat(captor.getValue().type()).isEqualTo(NotificationType.ORDER_SHIPPED);
        assertThat(captor.getValue().message()).contains("100");
    }
}
