package com.tasklink.patterns.creational;

import com.tasklink.dto.OrderCloneDraftDto;
import com.tasklink.model.OrderStatus;
import com.tasklink.model.TaskOrder;

import java.time.LocalDate;

public class ClosedOrderPrototype implements OrderPrototype {
    private final TaskOrder source;

    public ClosedOrderPrototype(TaskOrder source) {
        this.source = source;
    }

    @Override
    public OrderCloneDraftDto cloneOrder() {
        return new OrderCloneDraftDto(
                null,
                source.getTitle() + " (Relaunch)",
                source.getDescription(),
                source.getCategory(),
                source.getBudget(),
                source.getDeadline(),
                source.getMinRating(),
                source.getPricingMode(),
                source.getEstimatedHours(),
                source.isUrgent(),
                source.isFeatured(),
                OrderStatus.OPEN,
                LocalDate.now()
        );
    }
}
