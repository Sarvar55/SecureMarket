package com.codems.securemarket.identity.internal.application.service;

import com.codems.securemarket.identity.api.event.UserRegisteredEvent;
import com.codems.securemarket.identity.internal.application.port.in.RegisterUserUseCase;
import com.codems.securemarket.identity.internal.application.port.in.query.UserView;
import com.codems.securemarket.identity.internal.application.port.in.command.RegisterUserCommand;
import com.codems.securemarket.identity.internal.application.port.out.IdentityEventPublisherPort;
import com.codems.securemarket.identity.internal.application.port.out.LoadUserPort;
import com.codems.securemarket.identity.internal.application.port.out.PasswordHasherPort;
import com.codems.securemarket.identity.internal.application.port.out.SaveUserPort;
import com.codems.securemarket.identity.internal.domain.exception.EmailAlreadyExistsException;
import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.Password;
import com.codems.securemarket.identity.internal.domain.model.User;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

public final class RegistrationService implements RegisterUserUseCase {

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    private final PasswordHasherPort passwordHasherPort;
    private final IdentityEventPublisherPort eventPublisher;
    private final Clock clock;

    public RegistrationService(
            LoadUserPort loadUserPort,
            SaveUserPort saveUserPort,
            PasswordHasherPort passwordHasherPort,
            IdentityEventPublisherPort eventPublisher,
            Clock clock
    ) {
        this.loadUserPort = loadUserPort;
        this.saveUserPort = saveUserPort;
        this.passwordHasherPort = passwordHasherPort;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    public UserView register(RegisterUserCommand command) {
        Email email = new Email(command.email());
        Password password = new Password(command.password());

        if (loadUserPort.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        Instant now = clock.instant();
        String passwordHash = passwordHasherPort.hash(password);
        User savedUser = saveUserPort.save(User.register(email, passwordHash, now));

        eventPublisher.publish(new UserRegisteredEvent(
                UUID.randomUUID(),
                savedUser.getId(),
                savedUser.getEmail().value(),
                now
        ));

        return UserView.from(savedUser);
    }
}

