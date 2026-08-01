package com.codems.securemarket.identity.internal.adapter.out.authentication;

import com.codems.securemarket.identity.internal.application.port.out.AuthenticateCredentialsPort;
import com.codems.securemarket.identity.internal.domain.exception.InvalidCredentialsException;
import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.Password;
import com.codems.securemarket.identity.internal.domain.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
class SpringAuthenticationAdapter implements AuthenticateCredentialsPort {

    private final AuthenticationManager authenticationManager;

    SpringAuthenticationAdapter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User authenticate(Email email, Password password) {
        try {
            var result = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email.value(), password.value())
            );

            if (result.getPrincipal() instanceof User user) {
                return user;
            }
            throw new InvalidCredentialsException();
        } catch (AuthenticationException exception) {
            throw new InvalidCredentialsException();
        }
    }
}
