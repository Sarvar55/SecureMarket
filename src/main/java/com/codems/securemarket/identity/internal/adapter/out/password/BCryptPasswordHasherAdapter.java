package com.codems.securemarket.identity.internal.adapter.out.password;

import com.codems.securemarket.identity.internal.application.port.out.PasswordHasherPort;
import com.codems.securemarket.identity.internal.domain.model.Password;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class BCryptPasswordHasherAdapter implements PasswordHasherPort {

    private final PasswordEncoder passwordEncoder;

    BCryptPasswordHasherAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(Password password) {
        return passwordEncoder.encode(password.value());
    }

    @Override
    public boolean matches(Password password, String passwordHash) {
        return passwordEncoder.matches(password.value(), passwordHash);
    }
}

