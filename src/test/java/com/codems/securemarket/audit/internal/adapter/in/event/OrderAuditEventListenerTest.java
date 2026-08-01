package com.codems.securemarket.audit.internal.adapter.in.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.codems.securemarket.audit.internal.application.port.in.RecordAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.command.RecordAuditCommand;
import com.codems.securemarket.audit.internal.domain.model.AuditActions;
import com.codems.securemarket.order.api.event.OrderCreatedEvent;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class OrderAuditEventListenerTest {

    @Test
    void recordsCustomerAndCreatedOrder() {
        RecordAuditUseCase audit = mock(RecordAuditUseCase.class);
        var listener = new OrderAuditEventListener(audit);

        listener.on(new OrderCreatedEvent(
                UUID.randomUUID(), 100L, 8L, Instant.parse("2026-08-01T10:00:00Z")
        ));

        var captor = ArgumentCaptor.forClass(RecordAuditCommand.class);
        verify(audit).record(captor.capture());
        assertThat(captor.getValue().action()).isEqualTo(AuditActions.ORDER_CREATED);
        assertThat(captor.getValue().actorId()).isEqualTo(8L);
        assertThat(captor.getValue().resourceId()).isEqualTo(100L);
    }
}
