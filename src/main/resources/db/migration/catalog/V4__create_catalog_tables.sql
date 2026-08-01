CREATE TABLE categories (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(120) NOT NULL,
    slug        VARCHAR(120) NOT NULL UNIQUE,
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL,
    version     BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE products (
    id           BIGSERIAL PRIMARY KEY,
    category_id  BIGINT NOT NULL REFERENCES categories(id),
    sku          VARCHAR(64) NOT NULL UNIQUE,
    name         VARCHAR(180) NOT NULL,
    description  VARCHAR(1000),
    unit_price   NUMERIC(19, 2) NOT NULL CHECK (unit_price > 0),
    currency     VARCHAR(3) NOT NULL,
    stock        INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    status       VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'INACTIVE')),
    created_at   TIMESTAMPTZ NOT NULL,
    updated_at   TIMESTAMPTZ NOT NULL,
    version      BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_categories_active ON categories(active);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_public_catalog ON products(status, name);
