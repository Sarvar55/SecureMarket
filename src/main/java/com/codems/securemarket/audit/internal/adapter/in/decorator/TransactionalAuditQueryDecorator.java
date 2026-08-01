package com.codems.securemarket.audit.internal.adapter.in.decorator;

import com.codems.securemarket.audit.internal.application.port.in.QueryAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.query.AuditView;
import com.codems.securemarket.audit.internal.application.service.AuditQueryService;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Component
class TransactionalAuditQueryDecorator implements QueryAuditUseCase {

    private final AuditQueryService delegate;

    TransactionalAuditQueryDecorator(AuditQueryService delegate) {
        this.delegate = delegate;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditView> getRecent(int limit) {
        return delegate.getRecent(limit);
    }
}
