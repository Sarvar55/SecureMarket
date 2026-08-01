package com.codems.securemarket.order.internal.adapter.in.web;

import com.codems.securemarket.order.internal.application.port.in.CheckoutOrderUseCase;
import com.codems.securemarket.order.internal.application.port.in.query.OrderView;
import com.codems.securemarket.order.internal.application.port.in.QueryOrdersUseCase;
import com.codems.securemarket.shared.constants.ApplicationConstants;
import com.codems.securemarket.shared.web.response.BaseResponse;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders", version = ApplicationConstants.DEFAULT_API_VERSION)
class OrderController {
    private final CheckoutOrderUseCase checkoutOrderUseCase;
    private final QueryOrdersUseCase queryOrdersUseCase;

    OrderController(CheckoutOrderUseCase checkoutOrderUseCase, QueryOrdersUseCase queryOrdersUseCase) {
        this.checkoutOrderUseCase = checkoutOrderUseCase;
        this.queryOrdersUseCase = queryOrdersUseCase;
    }

    @PostMapping("/checkout")
    BaseResponse<OrderView> checkout(
            @AuthenticationPrincipal(expression = "userId") Long customerId
    ) {
        return BaseResponse.success(checkoutOrderUseCase.checkout(customerId));
    }

    @GetMapping
    BaseResponse<List<OrderView>> getMyOrders(
            @AuthenticationPrincipal(expression = "userId") Long customerId
    ) {
        return BaseResponse.success(queryOrdersUseCase.getForCustomer(customerId));
    }

    @GetMapping("/{orderId}")
    BaseResponse<OrderView> getById(
            @AuthenticationPrincipal(expression = "userId") Long customerId,
            @PathVariable Long orderId
    ) {
        return BaseResponse.success(queryOrdersUseCase.getById(customerId, orderId));
    }
}
