package com.codems.securemarket.identity.internal.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.codems.securemarket.identity.internal.application.port.in.command.RegisterUserCommand;
import com.codems.securemarket.identity.internal.application.port.out.IdentityEventPublisherPort;
import com.codems.securemarket.identity.internal.application.port.out.LoadUserPort;
import com.codems.securemarket.identity.internal.application.port.out.PasswordHasherPort;
import com.codems.securemarket.identity.internal.application.port.out.SaveUserPort;
import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.Role;
import com.codems.securemarket.identity.internal.domain.model.User;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrationService unit tests")
class RegistrationServiceTest {

        private final Instant now = Instant.parse("2026-08-01T09:00:00Z");

        @Mock
        private LoadUserPort loadUserPort;
        @Mock
        private SaveUserPort saveUserPort;
        @Mock
        private PasswordHasherPort passwordHasherPort;
        @Mock
        private IdentityEventPublisherPort eventPublisher;
        @Mock
        private Clock clock;

        @InjectMocks
        private RegistrationService service;

        @BeforeEach
        void setup() {
                when(clock.instant()).thenReturn(now);
        }

        @Test
        @DisplayName("When email is already registered then throw exception")
        void whenEmailIsAlreadyRegisteredThenThrowException() {

                when(loadUserPort.existsByEmail(new Email("user@example.com"))).thenReturn(false);
                when(passwordHasherPort.hash(any())).thenReturn("encoded-password");

                when(saveUserPort.save(any())).thenReturn(User.restore(
                                1L,
                                new Email("user@example.com"),
                                "encoded-password",
                                com.codems.securemarket.identity.internal.domain.model.AccountStatus.ACTIVE,
                                Set.of(Role.CUSTOMER),
                                0,
                                null,
                                now,
                                now));

                var result = service.register(
                                new RegisterUserCommand("user@example.com", "very-secure-password"));

                assertEquals(1L, result.id());
                assertEquals(Set.of(Role.CUSTOMER), result.roles());
                verify(eventPublisher).publish(any());
        }

}
