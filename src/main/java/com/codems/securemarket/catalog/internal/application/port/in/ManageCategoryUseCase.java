package com.codems.securemarket.catalog.internal.application.port.in;

import com.codems.securemarket.catalog.internal.application.port.in.query.CategoryView;

import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeCategoryStatusCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.CreateCategoryCommand;

public interface ManageCategoryUseCase {

    CategoryView create(CreateCategoryCommand command);

    CategoryView changeStatus(ChangeCategoryStatusCommand command);
}

