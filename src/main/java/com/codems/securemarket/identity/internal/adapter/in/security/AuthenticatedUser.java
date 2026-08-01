package com.codems.securemarket.identity.internal.adapter.in.security;

import com.codems.securemarket.identity.internal.domain.model.Role;
import java.util.Set;

public record AuthenticatedUser(
        Long userId,
        String email,
        Set<Role> roles
) {
}

