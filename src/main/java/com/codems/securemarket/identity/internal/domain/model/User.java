package com.codems.securemarket.identity.internal.domain.model;

import com.codems.securemarket.identity.internal.domain.exception.AccountNotActiveException;
import com.codems.securemarket.identity.internal.domain.exception.InvalidAccountStatusTransitionException;
import com.codems.securemarket.identity.internal.domain.exception.UserMustHaveRoleException;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public final class User {

    private final Long id;
    private final Email email;
    private final String password;
    private AccountStatus status;
    private Set<Role> roles;
    private int failedLoginAttempts;
    private Instant lockedUntil;
    private final Instant createdAt;
    private Instant updatedAt;

    private User(
            Long id,
            Email email,
            String password,
            AccountStatus status,
            Set<Role> roles,
            int failedLoginAttempts,
            Instant lockedUntil,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.email = Objects.requireNonNull(email);
        this.password = requirePasswordHash(password);
        this.status = Objects.requireNonNull(status);
        this.roles = requireRoles(roles);
        this.failedLoginAttempts = failedLoginAttempts;
        this.lockedUntil = lockedUntil;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static User register(Email email, String passwordHash, Instant now) {
        return new User(
                null,
                email,
                passwordHash,
                AccountStatus.ACTIVE,
                Set.of(Role.CUSTOMER),
                0,
                null,
                now,
                now);
    }

    public static User restore(
            Long id,
            Email email,
            String passwordHash,
            AccountStatus status,
            Set<Role> roles,
            int failedLoginAttempts,
            Instant lockedUntil,
            Instant createdAt,
            Instant updatedAt) {
        return new User(
                Objects.requireNonNull(id), email, passwordHash, status, roles,
                failedLoginAttempts, lockedUntil, createdAt, updatedAt);
    }

    public void ensureCanAuthenticate(Instant now) {
        if (status == AccountStatus.LOCKED && isLockExpired(now)) {
            status = AccountStatus.ACTIVE;
            failedLoginAttempts = 0;
            lockedUntil = null;
            updatedAt = now;
        }

        if (status != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(status);
        }
    }

    public void recordFailedLogin(int maximumAttempts, Instant lockUntil, Instant now) {
        if (maximumAttempts <= 0) {
            throw new IllegalArgumentException("Maximum attempts must be positive");
        }

        failedLoginAttempts++;
        updatedAt = now;

        if (failedLoginAttempts >= maximumAttempts) {
            status = AccountStatus.LOCKED;
            lockedUntil = Objects.requireNonNull(lockUntil);
        }
    }

    public void recordSuccessfulLogin(Instant now) {
        failedLoginAttempts = 0;
        lockedUntil = null;
        updatedAt = now;
    }

    public void changeStatus(AccountStatus targetStatus, Instant now) {
        Objects.requireNonNull(targetStatus);

        if (status == targetStatus || targetStatus == AccountStatus.PENDING) {
            throw new InvalidAccountStatusTransitionException(status, targetStatus);
        }

        status = targetStatus;
        if (targetStatus != AccountStatus.LOCKED) {
            failedLoginAttempts = 0;
            lockedUntil = null;
        }
        updatedAt = now;
    }

    public void replaceRoles(Set<Role> newRoles, Instant now) {
        roles = requireRoles(newRoles);
        updatedAt = now;
    }

    private boolean isLockExpired(Instant now) {
        return lockedUntil != null && !now.isBefore(lockedUntil);
    }

    private static String requirePasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash is required");
        }
        return passwordHash;
    }

    private static Set<Role> requireRoles(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new UserMustHaveRoleException();
        }
        return Set.copyOf(roles);
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public Instant getLockedUntil() {
        return lockedUntil;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
