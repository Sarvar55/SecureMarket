package com.codems.securemarket.notification.internal.adapter.in.web;

import com.codems.securemarket.notification.internal.application.port.in.ManageNotificationsUseCase;
import com.codems.securemarket.notification.internal.application.port.in.query.NotificationView;
import com.codems.securemarket.shared.constants.ApplicationConstants;
import com.codems.securemarket.shared.web.response.BaseResponse;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/notifications",
        version = ApplicationConstants.DEFAULT_API_VERSION
)
class NotificationController {

    private final ManageNotificationsUseCase manageNotificationsUseCase;

    NotificationController(ManageNotificationsUseCase manageNotificationsUseCase) {
        this.manageNotificationsUseCase = manageNotificationsUseCase;
    }

    @GetMapping
    BaseResponse<List<NotificationView>> getMyNotifications(
            @AuthenticationPrincipal(expression = "userId") Long userId
    ) {
        return BaseResponse.success(
                manageNotificationsUseCase.getForUser(userId)
        );
    }

    @PatchMapping("/{notificationId}/read")
    BaseResponse<NotificationView> markAsRead(
            @AuthenticationPrincipal(expression = "userId") Long userId,
            @PathVariable Long notificationId
    ) {
        return BaseResponse.success(
                manageNotificationsUseCase.markAsRead(
                        notificationId,
                        userId
                )
        );
    }
}
