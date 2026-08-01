CREATE INDEX idx_orders_analytics_period
    ON orders(currency, created_at DESC, status);

CREATE INDEX idx_order_items_analytics_product
    ON order_items(product_id, order_id);
