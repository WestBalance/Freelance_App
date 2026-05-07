package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

public class PaymentReceiptSubsystem {
    public String buildDescription(TaskOrder order, Long payerId, PaymentDescriptionBridge bridge) {
        return bridge.build(order, payerId);
    }
}
