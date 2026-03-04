package com.tasklink.patterns.behavioral;

import com.tasklink.model.TaskOrder;

import java.math.BigDecimal;

public class FixedPricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculate(TaskOrder order) {
        return order.getBudget();
    }
}
