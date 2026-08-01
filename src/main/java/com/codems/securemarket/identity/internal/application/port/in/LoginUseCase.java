package com.codems.securemarket.identity.internal.application.port.in;

import com.codems.securemarket.identity.internal.application.port.in.query.AccessTokenView;

import com.codems.securemarket.identity.internal.application.port.in.command.LoginCommand;

public interface LoginUseCase {

    AccessTokenView login(LoginCommand command);
}
