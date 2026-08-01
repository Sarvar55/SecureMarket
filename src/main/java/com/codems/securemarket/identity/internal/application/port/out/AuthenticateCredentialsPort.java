package com.codems.securemarket.identity.internal.application.port.out;

import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.Password;
import com.codems.securemarket.identity.internal.domain.model.User;

public interface AuthenticateCredentialsPort {

    User authenticate(Email email, Password password);
}

