package com.tasklink.patterns.behavioral;

import com.tasklink.model.TaskOrder;

import java.math.BigDecimal;

public class HourlyPricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculate(TaskOrder order) {
        int hours = order.getEstimatedHours() == null ? 1 : order.getEstimatedHours();
        return order.getBudget().multiply(BigDecimal.valueOf(hours));
    }
}
