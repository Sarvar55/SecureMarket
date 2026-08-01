package com.codems.securemarket.catalog.internal.application.port.out;

public interface DecreaseProductStockPort {

    boolean decrease(Long productId, int quantity);
}

