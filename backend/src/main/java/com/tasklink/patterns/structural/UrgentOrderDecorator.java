package com.tasklink.patterns.structural;

import java.math.BigDecimal;

public class UrgentOrderDecorator extends OrderDecorator {
    public UrgentOrderDecorator(OrderCostComponent component) { super(component); }

    @Override
    public BigDecimal budget() { return component.budget().multiply(BigDecimal.valueOf(1.25)); }

    @Override
    public int priorityBoost() { return component.priorityBoost() + 3; }
}
