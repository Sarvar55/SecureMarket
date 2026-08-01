package com.codems.securemarket.audit.internal.application.port.in;

import com.codems.securemarket.audit.internal.application.port.in.query.AuditView;
import java.util.List;

public interface QueryAuditUseCase {
    List<AuditView> getRecent(int limit);
}
