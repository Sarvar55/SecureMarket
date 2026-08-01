package com.codems.securemarket.notification.internal.adapter.in.event;

import com.codems.securemarket.notification.internal.application.port.in.command.*;
import com.codems.securemarket.notification.internal.application.port.in.CreateNotificationUseCase;
import com.codems.securemarket.notification.internal.domain.model.NotificationType;
import com.codems.securemarket.payment.api.event.PaymentProcessedEvent;
import com.codems.securemarket.payment.api.event.PaymentStatus;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class PaymentNotificationEventListener {

    private final CreateNotificationUseCase createNotificationUseCase;

    PaymentNotificationEventListener(CreateNotificationUseCase createNotificationUseCase) {
        this.createNotificationUseCase = createNotificationUseCase;
    }

    @EventListener
    public void on(PaymentProcessedEvent event) {
        boolean succeeded = event.status() == PaymentStatus.SUCCEEDED;

        createNotificationUseCase.create(new CreateNotificationCommand(
                event.customerId(),
                succeeded
                        ? NotificationType.PAYMENT_SUCCEEDED
                        : NotificationType.PAYMENT_FAILED,
                succeeded ? "Payment successful" : "Payment failed",
                succeeded
                        ? "Payment for order " + event.orderId() + " was successful."
                        : "Payment for order " + event.orderId() + " was declined.",
                event.occurredAt()
        ));
    }
}

