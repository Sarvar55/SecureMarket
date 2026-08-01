package com.codems.securemarket.identity.internal.application.port.in.command;

import com.codems.securemarket.identity.internal.domain.model.Role;
import java.util.Set;

public record UpdateUserRolesCommand(
                Long actorId,
                Long userId,
                Set<Role> roles) {
}
