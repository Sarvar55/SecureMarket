package com.codems.securemarket.order.internal.application.port.in;

import com.codems.securemarket.order.internal.application.port.in.command.ChangeOrderStatusCommand;
import com.codems.securemarket.order.internal.application.port.in.query.OrderView;

public interface ManageOrderFulfillmentUseCase {
    OrderView changeStatus(ChangeOrderStatusCommand command);
}
