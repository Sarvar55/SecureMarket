package com.codems.securemarket.identity.internal.adapter.out.authentication;

import com.codems.securemarket.identity.api.event.LoginFailedEvent;
import com.codems.securemarket.identity.internal.application.port.out.IdentityEventPublisherPort;
import com.codems.securemarket.identity.internal.application.port.out.LoadUserPort;
import com.codems.securemarket.identity.internal.application.port.out.PasswordHasherPort;
import com.codems.securemarket.identity.internal.application.port.out.SaveUserPort;
import com.codems.securemarket.identity.internal.domain.model.AccountStatus;
import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.Password;
import com.codems.securemarket.identity.internal.domain.model.User;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@Component
class IdentityAuthenticationProvider implements AuthenticationProvider {

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    private final PasswordHasherPort passwordHasherPort;
    private final IdentityEventPublisherPort eventPublisher;
    private final Clock clock;
    private final UserDetailsChecker userDetailsChecker = new AccountStatusChecker();

    IdentityAuthenticationProvider(
            LoadUserPort loadUserPort,
            SaveUserPort saveUserPort,
            PasswordHasherPort passwordHasherPort,
            IdentityEventPublisherPort eventPublisher,
            Clock clock) {
        this.loadUserPort = loadUserPort;
        this.saveUserPort = saveUserPort;
        this.passwordHasherPort = passwordHasherPort;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String attemptedEmail = String.valueOf(authentication.getPrincipal());
        String rawPassword = String.valueOf(authentication.getCredentials());
        Instant now = clock.instant();

        try {
            User user = authenticateUser(attemptedEmail, rawPassword, now);
            UserDetails userDetails = toUserDetails(user);

            return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    userDetails.getAuthorities());
        } catch (AuthenticationException exception) {
            eventPublisher.publish(new LoginFailedEvent(
                    UUID.randomUUID(),
                    attemptedEmail,
                    now));
            throw exception;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private User authenticateUser(String rawEmail, String rawPassword, Instant now) {
        Email email;
        Password password;

        try {
            email = new Email(rawEmail);
            password = new Password(rawPassword);
        } catch (RuntimeException exception) {
            throw new BadCredentialsException("Invalid credentials");
        }

        User user = loadUserPort.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        unlockExpiredAccount(user, now);
        userDetailsChecker.check(toUserDetails(user));

        if (!passwordHasherPort.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return user;
    }

    private void unlockExpiredAccount(User user, Instant now) {
        boolean expired = user.getStatus() == AccountStatus.LOCKED
                && user.getLockedUntil() != null
                && !now.isBefore(user.getLockedUntil());

        if (expired) {
            user.ensureCanAuthenticate(now);
            saveUserPort.save(user);
        }
    }

    private UserDetails toUserDetails(User user) {
        String[] authorities = user.getRoles().stream()
                .map(role -> "ROLE_" + role.name())
                .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail().value())
                .password(user.getPassword())
                .authorities(authorities)
                .accountLocked(user.getStatus() == AccountStatus.LOCKED)
                .disabled(user.getStatus() != AccountStatus.ACTIVE)
                .build();
    }

    private static final class AccountStatusChecker implements UserDetailsChecker {

        @Override
        public void check(UserDetails userDetails) {
            if (!userDetails.isAccountNonLocked()) {
                throw new LockedException("Account is locked");
            }
            if (!userDetails.isEnabled()) {
                throw new DisabledException("Account is not active");
            }
            if (!userDetails.isAccountNonExpired()) {
                throw new AccountExpiredException("Account is expired");
            }
            if (!userDetails.isCredentialsNonExpired()) {
                throw new CredentialsExpiredException("Credentials are expired");
            }
        }
    }
}
