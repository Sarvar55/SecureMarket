CREATE TABLE orders (
    id            BIGSERIAL PRIMARY KEY,
    customer_id   BIGINT NOT NULL REFERENCES users(id),
    total_amount  NUMERIC(19, 2) NOT NULL CHECK (total_amount > 0),
    currency      VARCHAR(3) NOT NULL,
    status        VARCHAR(30) NOT NULL CHECK (status IN (
        'PENDING_PAYMENT', 'PAID', 'PAYMENT_FAILED',
        'PROCESSING', 'SHIPPED', 'DELIVERED'
    )),
    created_at    TIMESTAMPTZ NOT NULL,
    updated_at    TIMESTAMPTZ NOT NULL,
    version       BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE order_items (
    order_id      BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    line_number   INTEGER NOT NULL,
    product_id    BIGINT NOT NULL,
    product_name  VARCHAR(180) NOT NULL,
    unit_price    NUMERIC(19, 2) NOT NULL CHECK (unit_price > 0),
    currency      VARCHAR(3) NOT NULL,
    quantity      INTEGER NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, line_number)
);

CREATE INDEX idx_orders_customer_created ON orders(customer_id, created_at DESC);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);
