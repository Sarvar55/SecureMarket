package com.codems.securemarket.notification.internal.adapter.in.event;

import com.codems.securemarket.notification.internal.application.port.in.CreateNotificationUseCase;
import com.codems.securemarket.notification.internal.application.port.in.command.CreateNotificationCommand;
import com.codems.securemarket.notification.internal.domain.model.NotificationType;
import com.codems.securemarket.order.api.event.OrderStatusChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class OrderNotificationEventListener {

    private final CreateNotificationUseCase createNotificationUseCase;

    OrderNotificationEventListener(CreateNotificationUseCase createNotificationUseCase) {
        this.createNotificationUseCase = createNotificationUseCase;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(OrderStatusChangedEvent event) {
        NotificationContent content = contentFor(event.currentStatus());
        if (content == null) {
            return;
        }

        createNotificationUseCase.create(new CreateNotificationCommand(
                event.customerId(),
                content.type(),
                content.title(),
                content.message() + " Order: " + event.orderId(),
                event.occurredAt()
        ));
    }

    private NotificationContent contentFor(String status) {
        return switch (status) {
            case "PROCESSING" -> new NotificationContent(
                    NotificationType.ORDER_PROCESSING,
                    "Order is being prepared",
                    "Your order is now being prepared."
            );
            case "SHIPPED" -> new NotificationContent(
                    NotificationType.ORDER_SHIPPED,
                    "Order shipped",
                    "Your order has been shipped."
            );
            case "DELIVERED" -> new NotificationContent(
                    NotificationType.ORDER_DELIVERED,
                    "Order delivered",
                    "Your order has been delivered."
            );
            default -> null;
        };
    }

    private record NotificationContent(
            NotificationType type,
            String title,
            String message
    ) {
    }
}
