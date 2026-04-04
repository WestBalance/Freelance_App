package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

import java.math.BigDecimal;

public class StandardPromotionPolicy implements PromotionPolicyImplementor {
    @Override
    public BigDecimal applyPromotion(BigDecimal currentBudget, TaskOrder order) {
        BigDecimal budget = currentBudget;
        if (order.isUrgent()) {
            budget = budget.multiply(BigDecimal.valueOf(1.25));
        }
        if (order.isFeatured()) {
            budget = budget.add(BigDecimal.valueOf(40));
        }
        return budget;
    }

    @Override
    public int priorityBoost(TaskOrder order) {
        int boost = 0;
        if (order.isUrgent()) {
            boost += 3;
        }
        if (order.isFeatured()) {
            boost += 2;
        }
        return boost;
    }
}
