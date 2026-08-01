package com.codems.securemarket.identity.internal.adapter.in.event;

import com.codems.securemarket.identity.api.event.LoginFailedEvent;
import com.codems.securemarket.identity.internal.application.port.out.RecordFailedLoginPort;
import com.codems.securemarket.identity.internal.domain.exception.InvalidEmailException;
import com.codems.securemarket.identity.internal.domain.model.Email;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class LoginFailedEventListener {

    private final RecordFailedLoginPort recordFailedLoginPort;

    LoginFailedEventListener(RecordFailedLoginPort recordFailedLoginPort) {
        this.recordFailedLoginPort = recordFailedLoginPort;
    }

    @EventListener
    public void on(LoginFailedEvent event) {
        try {
            recordFailedLoginPort.record(
                    new Email(event.attemptedEmail()),
                    event.occurredAt()
            );
        } catch (InvalidEmailException ignored) {
            log.warn("Ignored failed login event with malformed email");
        }
    }
}
