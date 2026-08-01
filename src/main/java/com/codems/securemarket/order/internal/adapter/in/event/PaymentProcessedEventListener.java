package com.codems.securemarket.order.internal.adapter.in.event;

import com.codems.securemarket.order.internal.application.port.in.HandlePaymentResultUseCase;
import com.codems.securemarket.payment.api.event.PaymentProcessedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class PaymentProcessedEventListener {
    private final HandlePaymentResultUseCase handlePaymentResultUseCase;

    PaymentProcessedEventListener(HandlePaymentResultUseCase handlePaymentResultUseCase) {
        this.handlePaymentResultUseCase = handlePaymentResultUseCase;
    }

    @EventListener
    public void on(PaymentProcessedEvent event) {
        handlePaymentResultUseCase.handle(
                event.orderId(), event.customerId(), event.status(), event.occurredAt()
        );
    }
}
