package com.codems.securemarket.identity.internal.application.port.in.query;

import com.codems.securemarket.identity.internal.domain.model.AccountStatus;
import com.codems.securemarket.identity.internal.domain.model.Role;
import com.codems.securemarket.identity.internal.domain.model.User;
import java.time.Instant;
import java.util.Set;

public record UserView(
        Long id,
        String email,
        AccountStatus status,
        Set<Role> roles,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserView from(User user) {
        return new UserView(
                user.getId(),
                user.getEmail().value(),
                user.getStatus(),
                user.getRoles(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

