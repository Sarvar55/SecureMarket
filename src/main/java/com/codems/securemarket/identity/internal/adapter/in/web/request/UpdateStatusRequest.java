package com.codems.securemarket.identity.internal.adapter.in.web.request;

import com.codems.securemarket.identity.internal.domain.model.AccountStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(@NotNull AccountStatus status) {
}
