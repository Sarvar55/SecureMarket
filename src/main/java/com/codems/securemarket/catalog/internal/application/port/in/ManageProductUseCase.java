package com.codems.securemarket.catalog.internal.application.port.in;

import com.codems.securemarket.catalog.internal.application.port.in.query.ProductView;

import com.codems.securemarket.catalog.internal.application.port.in.command.AdjustProductStockCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeProductPriceCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeProductStatusCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.CreateProductCommand;

public interface ManageProductUseCase {

    ProductView create(CreateProductCommand command);

    ProductView changePrice(ChangeProductPriceCommand command);

    ProductView adjustStock(AdjustProductStockCommand command);

    ProductView changeStatus(ChangeProductStatusCommand command);
}

