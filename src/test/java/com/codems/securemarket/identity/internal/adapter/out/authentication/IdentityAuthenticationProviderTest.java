package com.codems.securemarket.identity.internal.adapter.out.authentication;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codems.securemarket.identity.internal.application.port.out.IdentityEventPublisherPort;
import com.codems.securemarket.identity.internal.application.port.out.LoadUserPort;
import com.codems.securemarket.identity.internal.application.port.out.PasswordHasherPort;
import com.codems.securemarket.identity.internal.application.port.out.SaveUserPort;
import com.codems.securemarket.identity.internal.domain.model.AccountStatus;
import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.Role;
import com.codems.securemarket.identity.internal.domain.model.User;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

class IdentityAuthenticationProviderTest {

    @Test
    void wrongPasswordPublishesLoginFailedEvent() {
        LoadUserPort loadUserPort = mock(LoadUserPort.class);
        SaveUserPort saveUserPort = mock(SaveUserPort.class);
        PasswordHasherPort passwordHasherPort = mock(PasswordHasherPort.class);
        IdentityEventPublisherPort eventPublisher = mock(IdentityEventPublisherPort.class);
        Instant now = Instant.parse("2026-08-01T09:00:00Z");
        Email email = new Email("user@example.com");
        User user = User.restore(
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
        var provider = new IdentityAuthenticationProvider(
                loadUserPort,
                saveUserPort,
                passwordHasherPort,
                eventPublisher,
                Clock.fixed(now, ZoneOffset.UTC)
        );

        when(loadUserPort.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordHasherPort.matches(any(), any())).thenReturn(false);

        assertThrows(
                BadCredentialsException.class,
                () -> provider.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                "user@example.com",
                                "wrong-password-value"
                        )
                )
        );

        verify(eventPublisher).publish(any());
    }
}
