package com.codems.securemarket.audit.internal.application.service;

import com.codems.securemarket.audit.internal.application.port.in.RecordAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.command.RecordAuditCommand;
import com.codems.securemarket.audit.internal.application.port.out.AppendAuditPort;
import com.codems.securemarket.audit.internal.domain.model.AuditEntry;
import java.time.Clock;

public final class AuditService implements RecordAuditUseCase {

    private final AppendAuditPort appendAuditPort;
    private final Clock clock;

    public AuditService(AppendAuditPort appendAuditPort, Clock clock) {
        this.appendAuditPort = appendAuditPort;
        this.clock = clock;
    }

    @Override
    public void record(RecordAuditCommand command) {
        appendAuditPort.append(
                AuditEntry.create(
                        command.sourceModule(),
                        command.action(),
                        command.actorId(),
                        command.resourceType(),
                        command.resourceId(),
                        command.outcome(),
                        command.details(),
                        command.occurredAt(),
                        clock.instant()));
    }
}
