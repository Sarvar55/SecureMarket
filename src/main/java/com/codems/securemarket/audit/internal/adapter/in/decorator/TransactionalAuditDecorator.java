package com.codems.securemarket.audit.internal.adapter.in.decorator;

import com.codems.securemarket.audit.internal.application.port.in.RecordAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.command.RecordAuditCommand;
import com.codems.securemarket.audit.internal.application.service.AuditService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Component
class TransactionalAuditDecorator implements RecordAuditUseCase {

    private final AuditService delegate;

    TransactionalAuditDecorator(AuditService delegate) {
        this.delegate = delegate;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(RecordAuditCommand command) {
        delegate.record(command);
    }
}
