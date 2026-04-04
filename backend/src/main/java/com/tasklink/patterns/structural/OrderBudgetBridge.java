package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

import java.math.BigDecimal;

public abstract class OrderBudgetBridge {
    protected final PromotionPolicyImplementor promotionPolicy;

    protected OrderBudgetBridge(PromotionPolicyImplementor promotionPolicy) {
        this.promotionPolicy = promotionPolicy;
    }

    public abstract BigDecimal calculate(TaskOrder order);

    public int priorityBoost(TaskOrder order) {
        return promotionPolicy.priorityBoost(order);
    }
}
