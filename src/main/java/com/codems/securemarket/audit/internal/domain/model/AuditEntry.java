package com.codems.securemarket.audit.internal.domain.model;

import java.time.Instant;
import java.util.Objects;

public final class AuditEntry {

    private final Long id;
    private final String sourceModule;
    private final String action;
    private final Long actorId;
    private final String resourceType;
    private final Long resourceId;
    private final AuditOutcome outcome;
    private final String details;
    private final Instant occurredAt;
    private final Instant recordedAt;

    private AuditEntry(
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
        this.id = id;
        this.sourceModule = requireText(sourceModule, "sourceModule");
        this.action = requireText(action, "action");
        this.actorId = actorId;
        this.resourceType = requireText(resourceType, "resourceType");
        this.resourceId = resourceId;
        this.outcome = Objects.requireNonNull(outcome);
        this.details = details;
        this.occurredAt = Objects.requireNonNull(occurredAt);
        this.recordedAt = Objects.requireNonNull(recordedAt);
    }

    public static AuditEntry create(
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
        return new AuditEntry(
                null,
                sourceModule,
                action,
                actorId,
                resourceType,
                resourceId,
                outcome,
                details,
                occurredAt,
                recordedAt
        );
    }

    public static AuditEntry restore(
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
        return new AuditEntry(
                Objects.requireNonNull(id), sourceModule, action, actorId,
                resourceType, resourceId, outcome, details, occurredAt, recordedAt
        );
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }

    public Long getId() {
        return id;
    }

    public String getSourceModule() {
        return sourceModule;
    }

    public String getAction() {
        return action;
    }

    public Long getActorId() {
        return actorId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public AuditOutcome getOutcome() {
        return outcome;
    }

    public String getDetails() {
        return details;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}
