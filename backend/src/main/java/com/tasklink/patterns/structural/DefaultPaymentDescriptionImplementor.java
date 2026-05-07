package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

public class DefaultPaymentDescriptionImplementor implements PaymentDescriptionImplementor {
    @Override
    public String forInstantCharge(TaskOrder order, String pricingLabel) {
        return "Payment for order #" + order.getId() + " (" + order.getTitle() + ", " + pricingLabel + ")";
    }

    @Override
    public String forCheckout(TaskOrder order, Long payerId, String pricingLabel) {
        return "Checkout for order #" + order.getId() + " by client #" + payerId + " (" + pricingLabel + ")";
    }
}
