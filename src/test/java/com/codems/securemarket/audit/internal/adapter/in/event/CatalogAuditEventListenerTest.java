package com.codems.securemarket.audit.internal.adapter.in.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.codems.securemarket.audit.internal.application.port.in.RecordAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.command.RecordAuditCommand;
import com.codems.securemarket.audit.internal.domain.model.AuditActions;
import com.codems.securemarket.catalog.api.event.ProductStockChangedEvent;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CatalogAuditEventListenerTest {

    @Test
    void recordsActorAndStockChangeReason() {
        RecordAuditUseCase audit = mock(RecordAuditUseCase.class);
        var listener = new CatalogAuditEventListener(audit);
        var occurredAt = Instant.parse("2026-08-01T10:00:00Z");

        listener.on(new ProductStockChangedEvent(
                UUID.randomUUID(), 20L, 15, 7L, "ADMIN_ADJUSTMENT", occurredAt
        ));

        var captor = ArgumentCaptor.forClass(RecordAuditCommand.class);
        verify(audit).record(captor.capture());
        assertThat(captor.getValue().action()).isEqualTo(AuditActions.PRODUCT_STOCK_CHANGED);
        assertThat(captor.getValue().actorId()).isEqualTo(7L);
        assertThat(captor.getValue().resourceId()).isEqualTo(20L);
        assertThat(captor.getValue().details()).contains("ADMIN_ADJUSTMENT");
    }
}
