package com.codems.securemarket.catalog.internal.adapter.out.persistence;

import com.codems.securemarket.catalog.internal.application.port.out.DecreaseProductStockPort;
import com.codems.securemarket.catalog.internal.application.port.out.LoadProductPort;
import com.codems.securemarket.catalog.internal.application.port.out.SaveProductPort;
import com.codems.securemarket.catalog.internal.domain.model.Product;
import com.codems.securemarket.catalog.internal.domain.model.ProductStatus;
import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Component
class ProductPersistenceAdapter implements LoadProductPort, SaveProductPort, DecreaseProductStockPort {

    private static final String PRODUCTS_CACHE = "catalog-products";
    private static final String PRODUCT_CACHE = "catalog-product";

    private final JpaProductRepository repository;
    private final Clock clock;

    ProductPersistenceAdapter(JpaProductRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return repository.findById(productId).map(ProductEntity::toDomain);
    }

    @Override
    @Cacheable(cacheNames = PRODUCT_CACHE, key = "#productId")
    public Optional<Product> findPublicById(Long productId) {
        return repository.findPublicById(productId, ProductStatus.ACTIVE)
                .map(ProductEntity::toDomain);
    }

    @Override
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }

    @Override
    @Cacheable(cacheNames = PRODUCTS_CACHE, key = "'all'")
    public List<Product> findAllPublic() {
        return repository.findAllPublic(ProductStatus.ACTIVE).stream()
                .map(ProductEntity::toDomain)
                .toList();
    }

    @Override
    public List<Product> findAllByIds(Set<Long> productIds) {
        return repository.findAllByIdIn(productIds).stream()
                .map(ProductEntity::toDomain)
                .toList();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = PRODUCTS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCT_CACHE, allEntries = true)
    })
    public Product save(Product product) {
        ProductEntity entity = product.getId() == null
                ? ProductEntity.from(product)
                : repository.findById(product.getId())
                        .orElseGet(() -> ProductEntity.from(product));
        entity.updateFrom(product);
        return repository.save(entity).toDomain();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = PRODUCTS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCT_CACHE, key = "#productId")
    })
    public boolean decrease(Long productId, int quantity) {
        return repository.decreaseStock(
                productId, quantity, ProductStatus.ACTIVE, clock.instant()
        ) == 1;
    }
}
