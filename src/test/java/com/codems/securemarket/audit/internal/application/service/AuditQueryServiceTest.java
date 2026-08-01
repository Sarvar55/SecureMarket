package com.codems.securemarket.audit.internal.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.codems.securemarket.audit.internal.application.port.out.LoadAuditPort;
import com.codems.securemarket.audit.internal.domain.model.AuditEntry;
import com.codems.securemarket.audit.internal.domain.model.AuditOutcome;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class AuditQueryServiceTest {

    @Test
    void returnsRecentEntriesAsViews() {
        LoadAuditPort loadAuditPort = mock(LoadAuditPort.class);
        var occurredAt = Instant.parse("2026-08-01T10:00:00Z");
        when(loadAuditPort.findRecent(25)).thenReturn(List.of(AuditEntry.restore(
                1L, "CATALOG", "CATALOG.PRODUCT_CREATED", 7L,
                "PRODUCT", 20L, AuditOutcome.SUCCESS, null, occurredAt, occurredAt
        )));

        var result = new AuditQueryService(loadAuditPort).getRecent(25);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).actorId()).isEqualTo(7L);
        assertThat(result.get(0).resourceId()).isEqualTo(20L);
    }
}
