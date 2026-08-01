package com.codems.securemarket.audit.internal.adapter.in.event;

import com.codems.securemarket.audit.internal.application.port.in.RecordAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.command.RecordAuditCommand;
import com.codems.securemarket.audit.internal.domain.model.AuditActions;
import com.codems.securemarket.audit.internal.domain.model.AuditOutcome;
import com.codems.securemarket.shared.event.UnexpectedApplicationErrorEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class SystemAuditEventListener {

    private final RecordAuditUseCase audit;

    SystemAuditEventListener(RecordAuditUseCase audit) {
        this.audit = audit;
    }

    @EventListener
    public void on(UnexpectedApplicationErrorEvent event) {
        audit.record(new RecordAuditCommand(
                "SYSTEM",
                AuditActions.UNEXPECTED_ERROR,
                null,
                "HTTP_REQUEST",
                null,
                AuditOutcome.FAILURE,
                event.httpMethod() + " " + event.requestPath()
                        + ", exception=" + event.exceptionType(),
                event.occurredAt()
        ));
    }
}
