package com.codems.securemarket.audit.internal.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.codems.securemarket.audit.internal.application.port.in.command.RecordAuditCommand;
import com.codems.securemarket.audit.internal.application.port.out.AppendAuditPort;
import com.codems.securemarket.audit.internal.domain.model.AuditOutcome;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class AuditServiceTest {

    @Test
    void recordAppendsAuditEntry() {
        AppendAuditPort appendAuditPort = mock(AppendAuditPort.class);
        Instant now = Instant.parse("2026-08-01T09:00:00Z");
        var service = new AuditService(
                appendAuditPort,
                Clock.fixed(now, ZoneOffset.UTC)
        );

        service.record(new RecordAuditCommand(
                "IDENTITY",
                "LOGIN_SUCCEEDED",
                10L,
                "USER",
                10L,
                AuditOutcome.SUCCESS,
                null,
                now
        ));

        verify(appendAuditPort).append(any());
    }
}
