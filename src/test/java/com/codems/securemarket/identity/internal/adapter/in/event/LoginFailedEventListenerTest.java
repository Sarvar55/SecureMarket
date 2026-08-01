package com.codems.securemarket.identity.internal.adapter.in.event;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.codems.securemarket.identity.api.event.LoginFailedEvent;
import com.codems.securemarket.identity.internal.application.port.out.RecordFailedLoginPort;
import com.codems.securemarket.identity.internal.domain.model.Email;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LoginFailedEventListenerTest {

    @Test
    void validEmailDelegatesFailedAttemptToPort() {
        RecordFailedLoginPort port = mock(RecordFailedLoginPort.class);
        var listener = new LoginFailedEventListener(port);
        Instant occurredAt = Instant.parse("2026-08-01T09:00:00Z");

        listener.on(new LoginFailedEvent(
                UUID.randomUUID(),
                "user@example.com",
                occurredAt
        ));

        verify(port).record(new Email("user@example.com"), occurredAt);
    }
}
