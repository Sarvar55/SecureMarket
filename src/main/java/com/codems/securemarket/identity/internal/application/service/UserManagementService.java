package com.codems.securemarket.identity.internal.application.service;

import com.codems.securemarket.identity.api.event.UserStatusChangedEvent;
import com.codems.securemarket.identity.internal.application.port.in.ManageUsersUseCase;
import com.codems.securemarket.identity.internal.application.port.in.query.UserView;
import com.codems.securemarket.identity.internal.application.port.in.command.UpdateUserRolesCommand;
import com.codems.securemarket.identity.internal.application.port.in.command.UpdateUserStatusCommand;
import com.codems.securemarket.identity.internal.application.port.out.IdentityEventPublisherPort;
import com.codems.securemarket.identity.internal.application.port.out.LoadUserPort;
import com.codems.securemarket.identity.internal.application.port.out.SaveUserPort;
import com.codems.securemarket.identity.internal.domain.exception.UserNotFoundException;
import com.codems.securemarket.identity.internal.domain.model.User;
import java.time.Clock;
import java.util.List;
import java.util.UUID;

public final class UserManagementService implements ManageUsersUseCase {

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    private final IdentityEventPublisherPort eventPublisher;
    private final Clock clock;

    public UserManagementService(
            LoadUserPort loadUserPort,
            SaveUserPort saveUserPort,
            IdentityEventPublisherPort eventPublisher,
            Clock clock
    ) {
        this.loadUserPort = loadUserPort;
        this.saveUserPort = saveUserPort;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    public UserView getById(Long userId) {
        return UserView.from(loadRequired(userId));
    }

    @Override
    public List<UserView> getAll() {
        return loadUserPort.findAll().stream().map(UserView::from).toList();
    }

    @Override
    public UserView updateStatus(UpdateUserStatusCommand command) {
        User user = loadRequired(command.userId());
        var previousStatus = user.getStatus();
        var now = clock.instant();

        user.changeStatus(command.status(), now);
        User savedUser = saveUserPort.save(user);

        eventPublisher.publish(new UserStatusChangedEvent(
                UUID.randomUUID(),
                command.actorId(),
                savedUser.getId(),
                previousStatus.name(),
                savedUser.getStatus().name(),
                now
        ));

        return UserView.from(savedUser);
    }

    @Override
    public UserView updateRoles(UpdateUserRolesCommand command) {
        User user = loadRequired(command.userId());
        user.replaceRoles(command.roles(), clock.instant());
        return UserView.from(saveUserPort.save(user));
    }

    private User loadRequired(Long userId) {
        return loadUserPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}

