package com.codems.securemarket.audit.internal.application.port.in;

import com.codems.securemarket.audit.internal.application.port.in.command.RecordAuditCommand;

public interface RecordAuditUseCase {

    void record(RecordAuditCommand command);
}
