package com.codems.securemarket.catalog.internal.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {

    boolean existsBySlug(String slug);

    List<CategoryEntity> findAllByActiveTrueOrderByNameAsc();
}
