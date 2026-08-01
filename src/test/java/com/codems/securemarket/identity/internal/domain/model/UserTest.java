package com.codems.securemarket.identity.internal.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codems.securemarket.identity.internal.domain.exception.AccountNotActiveException;
import java.time.Instant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    private static final Instant NOW = Instant.parse("2026-08-01T09:00:00Z");

    @Test
    @DisplayName("When user is registered then status is active")
    void whenUserIsRegisteredThenStatusIsActive() {
        User user = User.register(
                new Email("Customer@Example.com"),
                "encoded-password",
                NOW);

        assertEquals(AccountStatus.ACTIVE, user.getStatus());
        assertEquals("customer@example.com", user.getEmail().value());
        assertEquals(java.util.Set.of(Role.CUSTOMER), user.getRoles());
    }

    @Test
    @DisplayName("When user has too many failed logins then status is locked")
    void whenUserHasTooManyFailedLoginsThenStatusIsLocked() {
        User user = User.register(new Email("user@example.com"), "encoded-password", NOW);

        for (int attempt = 0; attempt < 5; attempt++) {
            user.recordFailedLogin(5, NOW.plusSeconds(900), NOW);
        }

        assertEquals(AccountStatus.LOCKED, user.getStatus());
        assertThrows(
                AccountNotActiveException.class,
                () -> user.ensureCanAuthenticate(NOW.plusSeconds(60)));
    }

    @Test
    @DisplayName("When expired lock is removed when user authenticates again")
    void whenExpiredLockIsRemovedWhenUserAuthenticatesAgain() {
        User user = User.register(new Email("user@example.com"), "encoded-password", NOW);
        user.recordFailedLogin(1, NOW.plusSeconds(60), NOW);

        user.ensureCanAuthenticate(NOW.plusSeconds(61));

        assertEquals(AccountStatus.ACTIVE, user.getStatus());
        assertEquals(0, user.getFailedLoginAttempts());
        assertNull(user.getLockedUntil());
    }
}
