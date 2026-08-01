package com.codems.securemarket.order.internal.adapter.in.web;

import com.codems.securemarket.order.internal.adapter.in.web.request.ChangeOrderStatusRequest;
import com.codems.securemarket.order.internal.application.port.in.ManageOrderFulfillmentUseCase;
import com.codems.securemarket.order.internal.application.port.in.command.ChangeOrderStatusCommand;
import com.codems.securemarket.order.internal.application.port.in.query.OrderView;
import com.codems.securemarket.shared.constants.ApplicationConstants;
import com.codems.securemarket.shared.web.response.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/orders", version = ApplicationConstants.DEFAULT_API_VERSION)
class AdminOrderController {

    private final ManageOrderFulfillmentUseCase fulfillmentUseCase;

    AdminOrderController(ManageOrderFulfillmentUseCase fulfillmentUseCase) {
        this.fulfillmentUseCase = fulfillmentUseCase;
    }

    @PatchMapping("/{orderId}/status")
    BaseResponse<OrderView> changeStatus(
            @AuthenticationPrincipal(expression = "userId") Long actorId,
            @PathVariable Long orderId,
            @Valid @RequestBody ChangeOrderStatusRequest request
    ) {
        return BaseResponse.success(fulfillmentUseCase.changeStatus(
                new ChangeOrderStatusCommand(orderId, request.status(), actorId)
        ));
    }
}
