package com.codems.securemarket.identity.internal.application.port.out;

import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface LoadUserPort {

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(Email email);

    boolean existsByEmail(Email email);

    List<User> findAll();
}

