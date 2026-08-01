package com.codems.securemarket.audit.internal.application.service;

import com.codems.securemarket.audit.internal.application.port.in.QueryAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.query.AuditView;
import com.codems.securemarket.audit.internal.application.port.out.LoadAuditPort;
import java.util.List;

public final class AuditQueryService implements QueryAuditUseCase {

    private final LoadAuditPort loadAuditPort;

    public AuditQueryService(LoadAuditPort loadAuditPort) {
        this.loadAuditPort = loadAuditPort;
    }

    @Override
    public List<AuditView> getRecent(int limit) {
        if (limit < 1 || limit > 200) {
            throw new IllegalArgumentException("limit must be between 1 and 200");
        }
        return loadAuditPort.findRecent(limit).stream()
                .map(AuditView::from)
                .toList();
    }
}
