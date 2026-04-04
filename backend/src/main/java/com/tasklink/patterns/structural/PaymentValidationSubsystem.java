package com.tasklink.patterns.structural;

import com.tasklink.model.OrderStatus;
import com.tasklink.model.TaskOrder;

public class PaymentValidationSubsystem {
    public void validatePayable(TaskOrder order, Long payerId) {
        if (!order.getClient().getId().equals(payerId)) {
            throw new IllegalArgumentException("Only order owner can pay for the order");
        }
        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only IN_PROGRESS orders can be paid");
        }
    }
}
