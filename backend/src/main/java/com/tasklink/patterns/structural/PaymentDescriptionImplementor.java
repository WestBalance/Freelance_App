package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

public interface PaymentDescriptionImplementor {
    String forInstantCharge(TaskOrder order, String pricingLabel);
    String forCheckout(TaskOrder order, Long payerId, String pricingLabel);
}
