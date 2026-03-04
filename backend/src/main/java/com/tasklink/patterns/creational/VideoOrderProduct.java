package com.tasklink.patterns.creational;

import com.tasklink.model.TaskOrder;

import java.math.BigDecimal;

public class VideoOrderProduct implements OrderProduct {
    @Override
    public TaskOrder createOrder(TaskOrder draft) {
        return draft.toBuilder().budget(draft.getBudget().add(BigDecimal.valueOf(120))).priorityScore(5).build();
    }
}
