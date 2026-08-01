package com.codems.securemarket.catalog.internal.adapter.in.decorator;

import com.codems.securemarket.catalog.internal.application.port.in.ManageProductUseCase;
import com.codems.securemarket.catalog.internal.application.port.in.command.AdjustProductStockCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeProductPriceCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeProductStatusCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.CreateProductCommand;
import com.codems.securemarket.catalog.internal.application.port.in.query.ProductView;
import com.codems.securemarket.catalog.internal.application.service.ProductManagementService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Component
@Transactional
class TransactionalProductManagementDecorator implements ManageProductUseCase {

    private final ProductManagementService delegate;

    TransactionalProductManagementDecorator(ProductManagementService delegate) {
        this.delegate = delegate;
    }

    @Override
    public ProductView create(CreateProductCommand command) {
        return delegate.create(command);
    }

    @Override
    public ProductView changePrice(ChangeProductPriceCommand command) {
        return delegate.changePrice(command);
    }

    @Override
    public ProductView adjustStock(AdjustProductStockCommand command) {
        return delegate.adjustStock(command);
    }

    @Override
    public ProductView changeStatus(ChangeProductStatusCommand command) {
        return delegate.changeStatus(command);
    }
}
