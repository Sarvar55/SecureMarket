package com.codems.securemarket.identity.internal.adapter.out.persistence;

import com.codems.securemarket.identity.internal.application.port.out.LoadUserPort;
import com.codems.securemarket.identity.internal.application.port.out.RecordFailedLoginPort;
import com.codems.securemarket.identity.internal.application.port.out.SaveUserPort;
import com.codems.securemarket.identity.internal.domain.model.AccountStatus;
import com.codems.securemarket.identity.internal.domain.model.Email;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
class FailedLoginAttemptAdapter implements RecordFailedLoginPort {

    private static final int MAXIMUM_LOGIN_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;

    FailedLoginAttemptAdapter(
            LoadUserPort loadUserPort,
            SaveUserPort saveUserPort
    ) {
        this.loadUserPort = loadUserPort;
        this.saveUserPort = saveUserPort;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(Email email, Instant occurredAt) {
        loadUserPort.findByEmail(email)
                .filter(user -> user.getStatus() == AccountStatus.ACTIVE)
                .ifPresent(user -> {
                    user.recordFailedLogin(
                            MAXIMUM_LOGIN_ATTEMPTS,
                            occurredAt.plus(LOCK_DURATION),
                            occurredAt
                    );
                    saveUserPort.save(user);
                });
    }
}

