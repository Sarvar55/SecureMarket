package com.codems.securemarket.catalog.internal.config;

import com.codems.securemarket.catalog.internal.application.port.out.CatalogEventPublisherPort;
import com.codems.securemarket.catalog.internal.application.port.out.DecreaseProductStockPort;
import com.codems.securemarket.catalog.internal.application.port.out.LoadCategoryPort;
import com.codems.securemarket.catalog.internal.application.port.out.LoadProductPort;
import com.codems.securemarket.catalog.internal.application.port.out.SaveCategoryPort;
import com.codems.securemarket.catalog.internal.application.port.out.SaveProductPort;
import com.codems.securemarket.catalog.internal.application.service.CatalogCheckoutService;
import com.codems.securemarket.catalog.internal.application.service.CatalogQueryService;
import com.codems.securemarket.catalog.internal.application.service.CategoryService;
import com.codems.securemarket.catalog.internal.application.service.ProductManagementService;
import java.time.Clock;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CatalogConfiguration {

    @Bean
    CategoryService categoryService(
            LoadCategoryPort loadCategoryPort,
            SaveCategoryPort saveCategoryPort,
            CatalogEventPublisherPort eventPublisher,
            Clock clock
    ) {
        return new CategoryService(loadCategoryPort, saveCategoryPort, eventPublisher, clock);
    }

    @Bean
    ProductManagementService productManagementService(
            LoadCategoryPort loadCategoryPort,
            LoadProductPort loadProductPort,
            SaveProductPort saveProductPort,
            CatalogEventPublisherPort eventPublisher,
            Clock clock
    ) {
        return new ProductManagementService(
                loadCategoryPort, loadProductPort, saveProductPort, eventPublisher, clock
        );
    }

    @Bean
    CatalogQueryService catalogQueryService(
            LoadCategoryPort loadCategoryPort,
            LoadProductPort loadProductPort
    ) {
        return new CatalogQueryService(loadCategoryPort, loadProductPort);
    }

    @Bean
    CatalogCheckoutService catalogCheckoutService(
            LoadProductPort loadProductPort,
            LoadCategoryPort loadCategoryPort,
            DecreaseProductStockPort decreaseProductStockPort,
            SaveProductPort saveProductPort,
            CatalogEventPublisherPort eventPublisher,
            Clock clock
    ) {
        return new CatalogCheckoutService(
                loadProductPort, loadCategoryPort,
                decreaseProductStockPort, saveProductPort, eventPublisher, clock
        );
    }
}
