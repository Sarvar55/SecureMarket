package com.codems.securemarket.identity.internal.application.port.in;

import com.codems.securemarket.identity.internal.application.port.in.query.UserView;

import com.codems.securemarket.identity.internal.application.port.in.command.UpdateUserRolesCommand;
import com.codems.securemarket.identity.internal.application.port.in.command.UpdateUserStatusCommand;
import java.util.List;

public interface ManageUsersUseCase {

    UserView getById(Long userId);

    List<UserView> getAll();

    UserView updateStatus(UpdateUserStatusCommand command);

    UserView updateRoles(UpdateUserRolesCommand command);
}
