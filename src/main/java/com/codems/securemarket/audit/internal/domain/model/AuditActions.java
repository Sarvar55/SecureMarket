package com.codems.securemarket.audit.internal.domain.model;

public final class AuditActions {

    private AuditActions() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String LOGIN_SUCCESS = "IDENTITY.LOGIN_SUCCESS";
    public static final String LOGIN_FAILURE = "IDENTITY.LOGIN_FAILURE";
    public static final String REGISTER = "IDENTITY.REGISTER";
    public static final String UPDATE_ROLES = "IDENTITY.UPDATE_ROLES";
    public static final String ROLE_ASSIGNED = "IDENTITY.ROLE_ASSIGNED";
    public static final String CATEGORY_CREATED = "CATALOG.CATEGORY_CREATED";
    public static final String CATEGORY_STATUS_CHANGED = "CATALOG.CATEGORY_STATUS_CHANGED";
    public static final String PRODUCT_CREATED = "CATALOG.PRODUCT_CREATED";
    public static final String PRODUCT_PRICE_CHANGED = "CATALOG.PRODUCT_PRICE_CHANGED";
    public static final String PRODUCT_STOCK_CHANGED = "CATALOG.PRODUCT_STOCK_CHANGED";
    public static final String PRODUCT_STATUS_CHANGED = "CATALOG.PRODUCT_STATUS_CHANGED";
    public static final String CHECKOUT_STARTED = "ORDER.CHECKOUT_STARTED";
    public static final String ORDER_CREATED = "ORDER.ORDER_CREATED";
    public static final String ORDER_STATUS_CHANGED = "ORDER.STATUS_CHANGED";
    public static final String PAYMENT_SUCCEEDED = "PAYMENT.PAYMENT_SUCCEEDED";
    public static final String PAYMENT_FAILED = "PAYMENT.PAYMENT_FAILED";
    public static final String UNEXPECTED_ERROR = "SYSTEM.UNEXPECTED_ERROR";
}
