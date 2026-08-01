package com.codems.securemarket.catalog.internal.adapter.out.persistence;

import com.codems.securemarket.catalog.internal.domain.model.ProductStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsBySku(String sku);

    List<ProductEntity> findAllByIdIn(Set<Long> ids);

    @Query("""
            select p from ProductEntity p, CategoryEntity c
            where p.categoryId = c.id
              and p.status = :status
              and c.active = true
            order by p.name asc
            """)
    List<ProductEntity> findAllPublic(@Param("status") ProductStatus status);

    @Query("""
            select p from ProductEntity p, CategoryEntity c
            where p.id = :id
              and p.categoryId = c.id
              and p.status = :status
              and c.active = true
            """)
    Optional<ProductEntity> findPublicById(
            @Param("id") Long id,
            @Param("status") ProductStatus status
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update ProductEntity p
               set p.stock = p.stock - :quantity,
                   p.updatedAt = :updatedAt,
                   p.version = p.version + 1
             where p.id = :productId
               and p.status = :status
               and p.stock >= :quantity
            """)
    int decreaseStock(
            @Param("productId") Long productId,
            @Param("quantity") int quantity,
            @Param("status") ProductStatus status,
            @Param("updatedAt") Instant updatedAt
    );
}
