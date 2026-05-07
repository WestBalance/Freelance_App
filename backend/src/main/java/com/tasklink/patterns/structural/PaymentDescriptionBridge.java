package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

public abstract class PaymentDescriptionBridge {
    protected final PaymentDescriptionImplementor implementor;

    protected PaymentDescriptionBridge(PaymentDescriptionImplementor implementor) {
        this.implementor = implementor;
    }

    public abstract String build(TaskOrder order, Long payerId);

    protected String pricingLabel(TaskOrder order) {
        return order.getPricingMode().name();
    }
}
