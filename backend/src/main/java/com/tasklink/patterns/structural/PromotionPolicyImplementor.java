package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;

import java.math.BigDecimal;

public interface PromotionPolicyImplementor {
    BigDecimal applyPromotion(BigDecimal currentBudget, TaskOrder order);
    int priorityBoost(TaskOrder order);
}
