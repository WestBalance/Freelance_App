package com.tasklink.patterns.structural;

import java.math.BigDecimal;

public record BaseOrderCost(BigDecimal baseBudget) implements OrderCostComponent {
    @Override
    public BigDecimal budget() { return baseBudget; }

    @Override
    public int priorityBoost() { return 0; }
}
