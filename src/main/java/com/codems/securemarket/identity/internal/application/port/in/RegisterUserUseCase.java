package com.codems.securemarket.identity.internal.application.port.in;

import com.codems.securemarket.identity.internal.application.port.in.query.UserView;

import com.codems.securemarket.identity.internal.application.port.in.command.RegisterUserCommand;

public interface RegisterUserUseCase {

    UserView register(RegisterUserCommand command);
}
