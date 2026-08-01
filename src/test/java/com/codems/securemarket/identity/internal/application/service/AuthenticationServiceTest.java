package com.codems.securemarket.identity.internal.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.codems.securemarket.identity.internal.application.port.in.command.LoginCommand;
import com.codems.securemarket.identity.internal.application.port.out.AuthenticateCredentialsPort;
import com.codems.securemarket.identity.internal.application.port.out.IdentityEventPublisherPort;
import com.codems.securemarket.identity.internal.application.port.out.IssuedToken;
import com.codems.securemarket.identity.internal.application.port.out.SaveUserPort;
import com.codems.securemarket.identity.internal.application.port.out.TokenIssuerPort;
import com.codems.securemarket.identity.internal.domain.exception.InvalidCredentialsException;
import com.codems.securemarket.identity.internal.domain.model.AccountStatus;
import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.Role;
import com.codems.securemarket.identity.internal.domain.model.User;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationService unit tests")
class AuthenticationServiceTest {

        private final Instant now = Instant.parse("2026-08-01T09:00:00Z");
        @Mock
        private AuthenticateCredentialsPort authenticateCredentialsPort;
        @Mock
        private SaveUserPort saveUserPort;
        @Mock
        private TokenIssuerPort tokenIssuerPort;
        @Mock
        private IdentityEventPublisherPort eventPublisher;
        @InjectMocks
        private AuthenticationService service;

        @Mock
        private Clock clock;

        private User user;

        @BeforeEach
        void setUp() {
                when(clock.instant()).thenReturn(now);

                user = User.restore(
                                1L,
                                new Email("user@example.com"),
                                "encoded-password",
                                AccountStatus.ACTIVE,
                                Set.of(Role.CUSTOMER),
                                0,
                                null,
                                now,
                                now);

        }

        @Test
        @DisplayName("When correct credentials given then return access token")
        void whenCorrectCredentialsGivenThenReturnAccessToken() {
                when(authenticateCredentialsPort.authenticate(any(), any())).thenReturn(user);
                when(saveUserPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
                when(tokenIssuerPort.issue(user))
                                .thenReturn(new IssuedToken("jwt-token", now.plusSeconds(900)));

                var result = service.login(
                                new LoginCommand("user@example.com", "very-secure-password"));

                assertEquals("jwt-token", result.accessToken());
                assertEquals("Bearer", result.tokenType());
        }

        @Test
        @DisplayName("When authentication port rejects credentials then propagate exception")
        void whenAuthenticationPortRejectsCredentialsThenPropagateException() {
                when(authenticateCredentialsPort.authenticate(any(), any()))
                                .thenThrow(new InvalidCredentialsException());

                assertThrows(
                                InvalidCredentialsException.class,
                                () -> service.login(new LoginCommand("user@example.com", "wrong-password-value")));

                verifyNoInteractions(eventPublisher);
        }
}
