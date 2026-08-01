package com.codems.securemarket.catalog.internal.domain.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

public final class Category implements Serializable {

    private final Long id;
    private String name;
    private final String slug;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;

    private Category(
            Long id,
            String name,
            String slug,
            boolean active,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.name = requireText(name, "name");
        this.slug = normalizeSlug(slug);
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static Category create(String name, String slug, Instant now) {
        return new Category(null, name, slug, true, now, now);
    }

    public static Category restore(
            Long id,
            String name,
            String slug,
            boolean active,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Category(
                Objects.requireNonNull(id),
                name,
                slug,
                active,
                createdAt,
                updatedAt
        );
    }

    public void changeStatus(boolean active, Instant now) {
        this.active = active;
        this.updatedAt = Objects.requireNonNull(now);
    }

    private static String normalizeSlug(String slug) {
        return requireText(slug, "slug")
                .trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
