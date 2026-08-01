package com.codems.securemarket.catalog.internal.adapter.out.persistence;

import com.codems.securemarket.catalog.internal.application.port.out.LoadCategoryPort;
import com.codems.securemarket.catalog.internal.application.port.out.SaveCategoryPort;
import com.codems.securemarket.catalog.internal.domain.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Component
class CategoryPersistenceAdapter implements LoadCategoryPort, SaveCategoryPort {

    private static final String CATEGORIES_CACHE = "catalog-categories";
    private static final String PRODUCTS_CACHE = "catalog-products";
    private static final String PRODUCT_CACHE = "catalog-product";

    private final JpaCategoryRepository repository;

    CategoryPersistenceAdapter(JpaCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Category> findById(Long categoryId) {
        return repository.findById(categoryId).map(CategoryEntity::toDomain);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return repository.existsBySlug(slug);
    }

    @Override
    @Cacheable(cacheNames = CATEGORIES_CACHE, key = "'all'")
    public List<Category> findAllActive() {
        return repository.findAllByActiveTrueOrderByNameAsc().stream()
                .map(CategoryEntity::toDomain)
                .toList();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CATEGORIES_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCTS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCT_CACHE, allEntries = true)
    })
    public Category save(Category category) {
        CategoryEntity entity = category.getId() == null
                ? CategoryEntity.from(category)
                : repository.findById(category.getId())
                        .orElseGet(() -> CategoryEntity.from(category));
        entity.updateFrom(category);
        return repository.save(entity).toDomain();
    }
}
