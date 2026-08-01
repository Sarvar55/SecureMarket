package com.codems.securemarket.identity.internal.adapter.out.persistence;

import com.codems.securemarket.identity.internal.domain.model.AccountStatus;
import com.codems.securemarket.identity.internal.domain.model.Email;
import com.codems.securemarket.identity.internal.domain.model.Role;
import com.codems.securemarket.identity.internal.domain.model.User;
import com.codems.securemarket.shared.base.BaseEntity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")

class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 40)
    private Set<Role> roles = new HashSet<>();

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    @Version
    private long version;

    protected UserEntity() {
    }

    static UserEntity create(User user) {
        var entity = new UserEntity();
        entity.updateFrom(user);
        return entity;
    }

    void updateFrom(User user) {
        email = user.getEmail().value();
        password = user.getPassword();
        status = user.getStatus();
        roles = new HashSet<>(user.getRoles());
        failedLoginAttempts = user.getFailedLoginAttempts();
        lockedUntil = user.getLockedUntil();
    }

    User toDomain() {
        return User.restore(
                getId(),
                new Email(email),
                password,
                status,
                roles,
                failedLoginAttempts,
                lockedUntil,
                getCreatedAt(),
                getUpdatedAt());
    }
}
