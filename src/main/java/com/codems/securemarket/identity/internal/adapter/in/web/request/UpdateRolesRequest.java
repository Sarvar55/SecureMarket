package com.codems.securemarket.identity.internal.adapter.in.web.request;

import com.codems.securemarket.identity.internal.domain.model.Role;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record UpdateRolesRequest(@NotEmpty Set<Role> roles) {
}
