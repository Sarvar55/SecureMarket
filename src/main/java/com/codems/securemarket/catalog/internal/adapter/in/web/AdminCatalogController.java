package com.codems.securemarket.catalog.internal.adapter.in.web;

import com.codems.securemarket.catalog.internal.adapter.in.web.request.AdjustProductStockRequest;
import com.codems.securemarket.catalog.internal.adapter.in.web.request.ChangeCategoryStatusRequest;
import com.codems.securemarket.catalog.internal.adapter.in.web.request.ChangeProductPriceRequest;
import com.codems.securemarket.catalog.internal.adapter.in.web.request.ChangeProductStatusRequest;
import com.codems.securemarket.catalog.internal.adapter.in.web.request.CreateCategoryRequest;
import com.codems.securemarket.catalog.internal.adapter.in.web.request.CreateProductRequest;
import com.codems.securemarket.catalog.internal.application.port.in.query.CategoryView;
import com.codems.securemarket.catalog.internal.application.port.in.ManageCategoryUseCase;
import com.codems.securemarket.catalog.internal.application.port.in.ManageProductUseCase;
import com.codems.securemarket.catalog.internal.application.port.in.query.ProductView;
import com.codems.securemarket.catalog.internal.application.port.in.command.AdjustProductStockCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeCategoryStatusCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeProductPriceCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeProductStatusCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.CreateCategoryCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.CreateProductCommand;
import com.codems.securemarket.shared.constants.ApplicationConstants;
import com.codems.securemarket.shared.domain.Money;
import com.codems.securemarket.shared.web.response.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/catalog", version = ApplicationConstants.DEFAULT_API_VERSION)
class AdminCatalogController {

    private final ManageCategoryUseCase manageCategoryUseCase;
    private final ManageProductUseCase manageProductUseCase;

    AdminCatalogController(
            ManageCategoryUseCase manageCategoryUseCase,
            ManageProductUseCase manageProductUseCase
    ) {
        this.manageCategoryUseCase = manageCategoryUseCase;
        this.manageProductUseCase = manageProductUseCase;
    }

    @PostMapping("/categories")
    BaseResponse<CategoryView> createCategory(
            @AuthenticationPrincipal(expression = "userId") Long actorId,
            @Valid @RequestBody CreateCategoryRequest request
    ) {
        return BaseResponse.success(manageCategoryUseCase.create(
                new CreateCategoryCommand(request.name(), request.slug(), actorId)
        ));
    }

    @PatchMapping("/categories/{categoryId}/status")
    BaseResponse<CategoryView> changeCategoryStatus(
            @AuthenticationPrincipal(expression = "userId") Long actorId,
            @PathVariable Long categoryId,
            @RequestBody ChangeCategoryStatusRequest request
    ) {
        return BaseResponse.success(manageCategoryUseCase.changeStatus(
                new ChangeCategoryStatusCommand(categoryId, request.active(), actorId)
        ));
    }

    @PostMapping("/products")
    BaseResponse<ProductView> createProduct(
            @AuthenticationPrincipal(expression = "userId") Long actorId,
            @Valid @RequestBody CreateProductRequest request
    ) {
        return BaseResponse.success(manageProductUseCase.create(new CreateProductCommand(
                request.categoryId(), request.sku(), request.name(), request.description(),
                new Money(request.amount(), request.currency()), request.stock(), actorId
        )));
    }

    @PatchMapping("/products/{productId}/price")
    BaseResponse<ProductView> changePrice(
            @AuthenticationPrincipal(expression = "userId") Long actorId,
            @PathVariable Long productId,
            @Valid @RequestBody ChangeProductPriceRequest request
    ) {
        return BaseResponse.success(manageProductUseCase.changePrice(
                new ChangeProductPriceCommand(
                        productId, new Money(request.amount(), request.currency()), actorId
                )
        ));
    }

    @PatchMapping("/products/{productId}/stock")
    BaseResponse<ProductView> adjustStock(
            @AuthenticationPrincipal(expression = "userId") Long actorId,
            @PathVariable Long productId,
            @Valid @RequestBody AdjustProductStockRequest request
    ) {
        return BaseResponse.success(manageProductUseCase.adjustStock(
                new AdjustProductStockCommand(productId, request.quantity(), actorId)
        ));
    }

    @PatchMapping("/products/{productId}/status")
    BaseResponse<ProductView> changeStatus(
            @AuthenticationPrincipal(expression = "userId") Long actorId,
            @PathVariable Long productId,
            @Valid @RequestBody ChangeProductStatusRequest request
    ) {
        return BaseResponse.success(manageProductUseCase.changeStatus(
                new ChangeProductStatusCommand(productId, request.status(), actorId)
        ));
    }
}
