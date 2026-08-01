package com.codems.securemarket.order.internal.application.port.in;

import com.codems.securemarket.payment.api.event.PaymentStatus;
import java.time.Instant;

public interface HandlePaymentResultUseCase {
    void handle(Long orderId, Long customerId, PaymentStatus paymentStatus, Instant occurredAt);
}
