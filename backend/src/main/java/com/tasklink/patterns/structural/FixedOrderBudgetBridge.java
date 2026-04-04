package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

import java.math.BigDecimal;

public class FixedOrderBudgetBridge extends OrderBudgetBridge {
    public FixedOrderBudgetBridge(PromotionPolicyImplementor promotionPolicy) {
        super(promotionPolicy);
    }

    @Override
    public BigDecimal calculate(TaskOrder order) {
        return promotionPolicy.applyPromotion(order.getBudget(), order);
    }
}
