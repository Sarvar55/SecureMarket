package com.codems.securemarket.audit.internal.application.port.in.command;

import com.codems.securemarket.audit.internal.domain.model.AuditOutcome;
import java.time.Instant;

public record RecordAuditCommand(
        String sourceModule,
        String action,
        Long actorId,
        String resourceType,
        Long resourceId,
        AuditOutcome outcome,
        String details,
        Instant occurredAt
) {
}
