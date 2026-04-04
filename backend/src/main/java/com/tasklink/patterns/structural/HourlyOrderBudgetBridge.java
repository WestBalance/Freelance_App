package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

import java.math.BigDecimal;

public class HourlyOrderBudgetBridge extends OrderBudgetBridge {
    public HourlyOrderBudgetBridge(PromotionPolicyImplementor promotionPolicy) {
        super(promotionPolicy);
    }

    @Override
    public BigDecimal calculate(TaskOrder order) {
        BigDecimal hourlyCoordinationFee = order.getBudget().multiply(BigDecimal.valueOf(1.05));
        return promotionPolicy.applyPromotion(hourlyCoordinationFee, order);
    }
}
