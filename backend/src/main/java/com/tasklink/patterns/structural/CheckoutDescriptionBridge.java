package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

public class CheckoutDescriptionBridge extends PaymentDescriptionBridge {
    public CheckoutDescriptionBridge(PaymentDescriptionImplementor implementor) {
        super(implementor);
    }

    @Override
    public String build(TaskOrder order, Long payerId) {
        return implementor.forCheckout(order, payerId, pricingLabel(order));
    }
}
