package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

public class InstantChargeDescriptionBridge extends PaymentDescriptionBridge {
    public InstantChargeDescriptionBridge(PaymentDescriptionImplementor implementor) {
        super(implementor);
    }

    @Override
    public String build(TaskOrder order, Long payerId) {
        return implementor.forInstantCharge(order, pricingLabel(order));
    }
}
