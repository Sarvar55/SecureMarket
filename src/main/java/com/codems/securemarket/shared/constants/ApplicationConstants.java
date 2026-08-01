package com.codems.securemarket.shared.constants;

public final class ApplicationConstants {

    public static final String API_PREFIX = "/api";
    public static final String API_VERSION_HEADER = "X-VERSION-API";
    public static final String DEFAULT_API_VERSION = "1.0";
    public static final String APPLICATION_PACKAGE = "com.codems.securemarket";

    private ApplicationConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}

