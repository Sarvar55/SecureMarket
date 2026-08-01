package com.codems.securemarket.identity.internal.application.port.out;

import com.codems.securemarket.identity.internal.domain.model.Password;

public interface PasswordHasherPort {

    String hash(Password password);

    boolean matches(Password password, String passwordHash);
}

