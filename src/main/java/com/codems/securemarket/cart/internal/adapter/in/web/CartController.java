package com.codems.securemarket.cart.internal.adapter.in.web;

import com.codems.securemarket.cart.internal.adapter.in.web.request.CartQuantityRequest;
import com.codems.securemarket.cart.internal.application.port.in.query.CartView;
import com.codems.securemarket.cart.internal.application.port.in.ManageCartUseCase;
import com.codems.securemarket.cart.internal.application.port.in.command.AddCartItemCommand;
import com.codems.securemarket.cart.internal.application.port.in.command.ChangeCartItemCommand;
import com.codems.securemarket.shared.constants.ApplicationConstants;
import com.codems.securemarket.shared.web.response.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cart", version = ApplicationConstants.DEFAULT_API_VERSION)
class CartController {
    private final ManageCartUseCase manageCartUseCase;

    CartController(ManageCartUseCase manageCartUseCase) {
        this.manageCartUseCase = manageCartUseCase;
    }

    @GetMapping
    BaseResponse<CartView> get(@AuthenticationPrincipal(expression = "userId") Long customerId) {
        return BaseResponse.success(manageCartUseCase.get(customerId));
    }

    @PostMapping("/items/{productId}")
    BaseResponse<CartView> add(
            @AuthenticationPrincipal(expression = "userId") Long customerId,
            @PathVariable Long productId,
            @Valid @RequestBody CartQuantityRequest request
    ) {
        return BaseResponse.success(manageCartUseCase.add(
                new AddCartItemCommand(customerId, productId, request.quantity())
        ));
    }

    @PatchMapping("/items/{productId}")
    BaseResponse<CartView> change(
            @AuthenticationPrincipal(expression = "userId") Long customerId,
            @PathVariable Long productId,
            @Valid @RequestBody CartQuantityRequest request
    ) {
        return BaseResponse.success(manageCartUseCase.change(
                new ChangeCartItemCommand(customerId, productId, request.quantity())
        ));
    }

    @DeleteMapping("/items/{productId}")
    BaseResponse<CartView> remove(
            @AuthenticationPrincipal(expression = "userId") Long customerId,
            @PathVariable Long productId
    ) {
        return BaseResponse.success(manageCartUseCase.remove(customerId, productId));
    }

    @DeleteMapping
    BaseResponse<Void> clear(@AuthenticationPrincipal(expression = "userId") Long customerId) {
        manageCartUseCase.clear(customerId);
        return BaseResponse.success(null);
    }
}
