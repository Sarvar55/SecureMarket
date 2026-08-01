package com.codems.securemarket.audit.internal.application.port.in.query;

import com.codems.securemarket.audit.internal.domain.model.AuditEntry;
import com.codems.securemarket.audit.internal.domain.model.AuditOutcome;
import java.time.Instant;

public record AuditView(
        Long id,
        String sourceModule,
        String action,
        Long actorId,
        String resourceType,
        Long resourceId,
        AuditOutcome outcome,
        String details,
        Instant occurredAt,
        Instant recordedAt
) {
    public static AuditView from(AuditEntry entry) {
        return new AuditView(
                entry.getId(), entry.getSourceModule(), entry.getAction(),
                entry.getActorId(), entry.getResourceType(), entry.getResourceId(),
                entry.getOutcome(), entry.getDetails(),
                entry.getOccurredAt(), entry.getRecordedAt()
        );
    }
}
