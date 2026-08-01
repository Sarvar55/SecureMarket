package com.codems.securemarket.catalog.internal.adapter.in.web;

import com.codems.securemarket.catalog.internal.application.port.in.query.CategoryView;
import com.codems.securemarket.catalog.internal.application.port.in.query.ProductView;
import com.codems.securemarket.catalog.internal.application.port.in.QueryCatalogUseCase;
import com.codems.securemarket.shared.constants.ApplicationConstants;
import com.codems.securemarket.shared.web.response.BaseResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(version = ApplicationConstants.DEFAULT_API_VERSION)
class CatalogController {

    private final QueryCatalogUseCase queryCatalogUseCase;

    CatalogController(QueryCatalogUseCase queryCatalogUseCase) {
        this.queryCatalogUseCase = queryCatalogUseCase;
    }

    @GetMapping("/categories")
    BaseResponse<List<CategoryView>> getCategories() {
        return BaseResponse.success(queryCatalogUseCase.getCategories());
    }

    @GetMapping("/products")
    BaseResponse<List<ProductView>> getProducts() {
        return BaseResponse.success(queryCatalogUseCase.getProducts());
    }

    @GetMapping("/products/{productId}")
    BaseResponse<ProductView> getProduct(@PathVariable Long productId) {
        return BaseResponse.success(queryCatalogUseCase.getProduct(productId));
    }
}
