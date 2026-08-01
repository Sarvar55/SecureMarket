package com.codems.securemarket.catalog.internal.adapter.in.decorator;

import com.codems.securemarket.catalog.internal.application.port.in.ManageCategoryUseCase;
import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeCategoryStatusCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.CreateCategoryCommand;
import com.codems.securemarket.catalog.internal.application.port.in.query.CategoryView;
import com.codems.securemarket.catalog.internal.application.service.CategoryService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Component
@Transactional
class TransactionalCategoryManagementDecorator implements ManageCategoryUseCase {

    private final CategoryService delegate;

    TransactionalCategoryManagementDecorator(CategoryService delegate) {
        this.delegate = delegate;
    }

    @Override
    public CategoryView create(CreateCategoryCommand command) {
        return delegate.create(command);
    }

    @Override
    public CategoryView changeStatus(ChangeCategoryStatusCommand command) {
        return delegate.changeStatus(command);
    }
}
