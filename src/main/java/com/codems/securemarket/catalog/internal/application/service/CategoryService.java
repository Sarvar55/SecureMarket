package com.codems.securemarket.catalog.internal.application.service;

import com.codems.securemarket.catalog.api.event.CategoryCreatedEvent;
import com.codems.securemarket.catalog.api.event.CategoryStatusChangedEvent;
import com.codems.securemarket.catalog.internal.application.port.in.query.CategoryView;
import com.codems.securemarket.catalog.internal.application.port.in.ManageCategoryUseCase;
import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeCategoryStatusCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.CreateCategoryCommand;
import com.codems.securemarket.catalog.internal.application.port.out.CatalogEventPublisherPort;
import com.codems.securemarket.catalog.internal.application.port.out.LoadCategoryPort;
import com.codems.securemarket.catalog.internal.application.port.out.SaveCategoryPort;
import com.codems.securemarket.catalog.internal.domain.exception.CategoryNotFoundException;
import com.codems.securemarket.catalog.internal.domain.exception.DuplicateCategorySlugException;
import com.codems.securemarket.catalog.internal.domain.model.Category;
import java.time.Clock;
import java.util.UUID;

public final class CategoryService implements ManageCategoryUseCase {

    private final LoadCategoryPort loadCategoryPort;
    private final SaveCategoryPort saveCategoryPort;
    private final CatalogEventPublisherPort eventPublisher;
    private final Clock clock;

    public CategoryService(
            LoadCategoryPort loadCategoryPort,
            SaveCategoryPort saveCategoryPort,
            CatalogEventPublisherPort eventPublisher,
            Clock clock) {
        this.loadCategoryPort = loadCategoryPort;
        this.saveCategoryPort = saveCategoryPort;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    public CategoryView create(CreateCategoryCommand command) {
        var now = clock.instant();
        Category category = Category.create(command.name(), command.slug(), now);

        if (loadCategoryPort.existsBySlug(category.getSlug())) {
            throw new DuplicateCategorySlugException();
        }

        Category saved = saveCategoryPort.save(category);
        eventPublisher.publish(new CategoryCreatedEvent(
                UUID.randomUUID(),
                saved.getId(),
                saved.getName(),
                command.actorId(),
                now));
        return CategoryView.from(saved);
    }

    @Override
    public CategoryView changeStatus(ChangeCategoryStatusCommand command) {
        Category category = loadCategoryPort.findById(command.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(command.categoryId()));

        var now = clock.instant();
        category.changeStatus(command.active(), now);
        Category saved = saveCategoryPort.save(category);

        eventPublisher.publish(new CategoryStatusChangedEvent(
                UUID.randomUUID(),
                saved.getId(),
                saved.isActive(),
                command.actorId(),
                now));
        return CategoryView.from(saved);
    }
}
