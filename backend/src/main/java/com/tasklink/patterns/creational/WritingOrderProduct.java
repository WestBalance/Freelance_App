package com.tasklink.patterns.creational;

import com.tasklink.model.TaskOrder;

import java.math.BigDecimal;

public class WritingOrderProduct implements OrderProduct {
    @Override
    public TaskOrder createOrder(TaskOrder draft) {
        return draft.toBuilder().budget(draft.getBudget().add(BigDecimal.valueOf(30))).priorityScore(2).build();
    }
}
