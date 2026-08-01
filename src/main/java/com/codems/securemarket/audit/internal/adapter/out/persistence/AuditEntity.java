package com.codems.securemarket.audit.internal.adapter.out.persistence;

import com.codems.securemarket.audit.internal.domain.model.AuditEntry;
import com.codems.securemarket.audit.internal.domain.model.AuditOutcome;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "audit_events")
class AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_module", nullable = false, length = 40)
    private String sourceModule;

    @Column(nullable = false, length = 80)
    private String action;

    @Column(name = "actor_id")
    private Long actorId;

    @Column(name = "resource_type", nullable = false, length = 40)
    private String resourceType;

    @Column(name = "resource_id")
    private Long resourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuditOutcome outcome;

    @Column(columnDefinition = "text")
    private String details;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "recorded_at", nullable = false, updatable = false)
    private Instant recordedAt;

    protected AuditEntity() {
    }

    static AuditEntity from(AuditEntry entry) {
        var entity = new AuditEntity();
        entity.sourceModule = entry.getSourceModule();
        entity.action = entry.getAction();
        entity.actorId = entry.getActorId();
        entity.resourceType = entry.getResourceType();
        entity.resourceId = entry.getResourceId();
        entity.outcome = entry.getOutcome();
        entity.details = entry.getDetails();
        entity.occurredAt = entry.getOccurredAt();
        entity.recordedAt = entry.getRecordedAt();
        return entity;
    }

    AuditEntry toDomain() {
        return AuditEntry.restore(
                id, sourceModule, action, actorId, resourceType, resourceId,
                outcome, details, occurredAt, recordedAt
        );
    }
}
