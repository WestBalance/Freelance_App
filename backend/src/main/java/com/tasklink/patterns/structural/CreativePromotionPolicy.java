package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

import java.math.BigDecimal;

public class CreativePromotionPolicy implements PromotionPolicyImplementor {
    @Override
    public BigDecimal applyPromotion(BigDecimal currentBudget, TaskOrder order) {
        BigDecimal budget = currentBudget;
        if (order.isUrgent()) {
            budget = budget.multiply(BigDecimal.valueOf(1.20));
        }
        if (order.isFeatured()) {
            budget = budget.add(BigDecimal.valueOf(60));
        }
        return budget;
    }

    @Override
    public int priorityBoost(TaskOrder order) {
        int boost = 0;
        if (order.isUrgent()) {
            boost += 2;
        }
        if (order.isFeatured()) {
            boost += 3;
        }
        return boost;
    }
}
