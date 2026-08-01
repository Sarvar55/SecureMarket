package com.codems.securemarket.identity.internal.adapter.in.web;

import com.codems.securemarket.identity.internal.adapter.in.security.AuthenticatedUser;
import com.codems.securemarket.identity.internal.adapter.in.web.request.UpdateRolesRequest;
import com.codems.securemarket.identity.internal.adapter.in.web.request.UpdateStatusRequest;
import com.codems.securemarket.identity.internal.application.port.in.ManageUsersUseCase;
import com.codems.securemarket.identity.internal.application.port.in.query.UserView;
import com.codems.securemarket.identity.internal.application.port.in.command.UpdateUserRolesCommand;
import com.codems.securemarket.identity.internal.application.port.in.command.UpdateUserStatusCommand;
import com.codems.securemarket.shared.constants.ApplicationConstants;
import com.codems.securemarket.shared.web.response.BaseResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/users", version = ApplicationConstants.DEFAULT_API_VERSION)
class AdminUserController {

    private final ManageUsersUseCase manageUsersUseCase;

    AdminUserController(ManageUsersUseCase manageUsersUseCase) {
        this.manageUsersUseCase = manageUsersUseCase;
    }

    @GetMapping
    BaseResponse<List<UserView>> getAll() {
        return BaseResponse.success(manageUsersUseCase.getAll());
    }

    @GetMapping("/{userId}")
    BaseResponse<UserView> getById(@PathVariable Long userId) {
        return BaseResponse.success(manageUsersUseCase.getById(userId));
    }

    @PatchMapping("/{userId}/status")
    BaseResponse<UserView> updateStatus(
            @AuthenticationPrincipal AuthenticatedUser actor,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateStatusRequest request) {
        return BaseResponse.success(manageUsersUseCase.updateStatus(
                new UpdateUserStatusCommand(actor.userId(), userId, request.status())));
    }

    @PatchMapping("/{userId}/roles")
    BaseResponse<UserView> updateRoles(
            @AuthenticationPrincipal AuthenticatedUser actor,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateRolesRequest request) {
        return BaseResponse.success(manageUsersUseCase.updateRoles(
                new UpdateUserRolesCommand(actor.userId(), userId, request.roles())));
    }
}
