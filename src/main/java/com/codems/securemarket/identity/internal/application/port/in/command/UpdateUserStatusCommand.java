package com.codems.securemarket.identity.internal.application.port.in.command;

import com.codems.securemarket.identity.internal.domain.model.AccountStatus;

public record UpdateUserStatusCommand(
        Long actorId,
        Long userId,
        AccountStatus status
) {
}
