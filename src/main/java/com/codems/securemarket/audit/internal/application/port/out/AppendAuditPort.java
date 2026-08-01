package com.codems.securemarket.audit.internal.application.port.out;

import com.codems.securemarket.audit.internal.domain.model.AuditEntry;

public interface AppendAuditPort {

    void append(AuditEntry entry);
}

