package com.tasklink.patterns.structural;

import java.math.BigDecimal;

public class FeaturedOrderDecorator extends OrderDecorator {
    public FeaturedOrderDecorator(OrderCostComponent component) { super(component); }

    @Override
    public BigDecimal budget() { return component.budget().add(BigDecimal.valueOf(40)); }

    @Override
    public int priorityBoost() { return component.priorityBoost() + 2; }
}
