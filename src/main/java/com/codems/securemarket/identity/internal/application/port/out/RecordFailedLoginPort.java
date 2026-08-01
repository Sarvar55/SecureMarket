package com.codems.securemarket.identity.internal.application.port.out;

import com.codems.securemarket.identity.internal.domain.model.Email;
import java.time.Instant;

@FunctionalInterface
public interface RecordFailedLoginPort {

    void record(Email email, Instant occurredAt);
}
