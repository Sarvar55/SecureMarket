package com.codems.securemarket.catalog.internal.adapter.out.persistence;

import com.codems.securemarket.catalog.internal.domain.model.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;

@Entity
@Table(name = "categories")
class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private long version;

    protected CategoryEntity() {
    }

    static CategoryEntity from(Category category) {
        var entity = new CategoryEntity();
        entity.id = category.getId();
        entity.updateFrom(category);
        return entity;
    }

    void updateFrom(Category category) {
        name = category.getName();
        slug = category.getSlug();
        active = category.isActive();
        createdAt = category.getCreatedAt();
        updatedAt = category.getUpdatedAt();
    }

    Category toDomain() {
        return Category.restore(id, name, slug, active, createdAt, updatedAt);
    }
}
