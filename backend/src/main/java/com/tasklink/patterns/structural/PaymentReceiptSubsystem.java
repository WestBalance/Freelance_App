package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

public class PaymentReceiptSubsystem {
    public String buildDescription(TaskOrder order) {
        return "Payment for order #" + order.getId() + " (" + order.getTitle() + ")";
    }
}
