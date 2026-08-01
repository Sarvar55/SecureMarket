package com.codems.securemarket.audit.internal.application.port.out;

import com.codems.securemarket.audit.internal.domain.model.AuditEntry;
import java.util.List;

public interface LoadAuditPort {
    List<AuditEntry> findRecent(int limit);
}
