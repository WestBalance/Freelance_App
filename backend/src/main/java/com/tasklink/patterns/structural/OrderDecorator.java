package com.tasklink.patterns.structural;

import java.math.BigDecimal;

public abstract class OrderDecorator implements OrderCostComponent {
    protected final OrderCostComponent component;

    protected OrderDecorator(OrderCostComponent component) {
        this.component = component;
    }

    @Override
    public BigDecimal budget() { return component.budget(); }

    @Override
    public int priorityBoost() { return component.priorityBoost(); }
}
