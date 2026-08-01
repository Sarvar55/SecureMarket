package com.codems.securemarket.identity.internal.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codems.securemarket.identity.internal.application.port.out.LoadUserPort;
import com.codems.securemarket.identity.internal.application.port.out.SaveUserPort;
import com.codems.securemarket.identity.internal.domain.model.AccountStatus;
import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.Role;
import com.codems.securemarket.identity.internal.domain.model.User;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FailedLoginAttemptAdapterTest {

    @Test
    void recordsFailedAttemptForActiveUser() {
        LoadUserPort loadUserPort = mock(LoadUserPort.class);
        SaveUserPort saveUserPort = mock(SaveUserPort.class);
        var adapter = new FailedLoginAttemptAdapter(loadUserPort, saveUserPort);
        var email = new Email("user@example.com");
        var now = Instant.parse("2026-08-01T09:00:00Z");
        var user = User.restore(
                1L,
                email,
                "encoded-password",
                AccountStatus.ACTIVE,
                Set.of(Role.CUSTOMER),
                0,
                null,
                now,
                now
        );

        when(loadUserPort.findByEmail(email)).thenReturn(Optional.of(user));

        adapter.record(email, now);

        assertEquals(1, user.getFailedLoginAttempts());
        verify(saveUserPort).save(user);
    }
}
