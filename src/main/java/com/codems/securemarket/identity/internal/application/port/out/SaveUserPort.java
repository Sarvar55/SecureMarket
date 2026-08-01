package com.codems.securemarket.identity.internal.application.port.out;

import com.codems.securemarket.identity.internal.domain.model.User;

public interface SaveUserPort {

    User save(User user);
}

