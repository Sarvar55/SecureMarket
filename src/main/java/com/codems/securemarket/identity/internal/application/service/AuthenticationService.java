package com.codems.securemarket.identity.internal.application.service;

import com.codems.securemarket.identity.api.event.LoginFailedEvent;
import com.codems.securemarket.identity.api.event.LoginSucceededEvent;
import com.codems.securemarket.identity.internal.application.port.in.query.AccessTokenView;
import com.codems.securemarket.identity.internal.application.port.in.LoginUseCase;
import com.codems.securemarket.identity.internal.application.port.in.command.LoginCommand;
import com.codems.securemarket.identity.internal.application.port.out.AuthenticateCredentialsPort;
import com.codems.securemarket.identity.internal.application.port.out.IdentityEventPublisherPort;
import com.codems.securemarket.identity.internal.application.port.out.SaveUserPort;
import com.codems.securemarket.identity.internal.application.port.out.TokenIssuerPort;
import com.codems.securemarket.identity.internal.domain.exception.InvalidCredentialsException;
import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.Password;
import com.codems.securemarket.identity.internal.domain.model.User;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

public final class AuthenticationService implements LoginUseCase {

    private final AuthenticateCredentialsPort authenticateCredentialsPort;
    private final SaveUserPort saveUserPort;
    private final TokenIssuerPort tokenIssuerPort;
    private final IdentityEventPublisherPort eventPublisher;
    private final Clock clock;

    public AuthenticationService(
            AuthenticateCredentialsPort authenticateCredentialsPort,
            SaveUserPort saveUserPort,
            TokenIssuerPort tokenIssuerPort,
            IdentityEventPublisherPort eventPublisher,
            Clock clock
    ) {
        this.authenticateCredentialsPort = authenticateCredentialsPort;
        this.saveUserPort = saveUserPort;
        this.tokenIssuerPort = tokenIssuerPort;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    public AccessTokenView login(LoginCommand command) {
        Instant now = clock.instant();
        Email email;
        Password password;

        try {
            email = new Email(command.email());
            password = new Password(command.password());
        } catch (RuntimeException exception) {
            publishLoginFailed(safeEmail(command.email()), now);
            throw new InvalidCredentialsException();
        }

        User user = authenticateCredentialsPort.authenticate(email, password);

        user.recordSuccessfulLogin(now);
        User savedUser = saveUserPort.save(user);

        var issuedToken = tokenIssuerPort.issue(savedUser);
        eventPublisher.publish(new LoginSucceededEvent(
                UUID.randomUUID(),
                savedUser.getId(),
                now
        ));

        return new AccessTokenView(issuedToken.value(), issuedToken.expiresAt());
    }

    private void publishLoginFailed(String attemptedEmail, Instant now) {
        eventPublisher.publish(new LoginFailedEvent(
                UUID.randomUUID(),
                attemptedEmail,
                now
        ));
    }

    private String safeEmail(String attemptedEmail) {
        if (attemptedEmail == null || attemptedEmail.isBlank()) {
            return "invalid-email";
        }
        return attemptedEmail.trim().substring(0, Math.min(attemptedEmail.trim().length(), 254));
    }
}
