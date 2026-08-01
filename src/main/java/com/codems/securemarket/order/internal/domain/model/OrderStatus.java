package com.codems.securemarket.order.internal.domain.model;

public enum OrderStatus {
    PENDING_PAYMENT,
    PAID,
    PAYMENT_FAILED,
    PROCESSING,
    SHIPPED,
    DELIVERED
}
