package com.tasklink.patterns.creational;

import com.tasklink.dto.CreateOrderRequest;
import com.tasklink.model.TaskOrder;
import com.tasklink.model.UserAccount;

import java.time.LocalDate;

public abstract class OrderFactoryMethod {
    protected abstract OrderProduct factoryMethod();

    public TaskOrder anOperation(CreateOrderRequest request, UserAccount client) {
        TaskOrder draft = TaskOrder.builder()
                .title(request.title())
                .description(request.description())
                .category(request.category())
                .budget(request.budget())
                .currency("USD")
                .deadline(request.deadline() == null ? LocalDate.now().plusDays(7) : request.deadline())
                .minRating(request.minRating() == null ? 0.0 : request.minRating())
                .status(com.tasklink.model.OrderStatus.OPEN)
                .pricingMode(request.pricingMode())
                .estimatedHours(request.estimatedHours())
                .urgent(request.urgent())
                .featured(request.featured())
                .client(client)
                .priorityScore(1)
                .build();
        return factoryMethod().createOrder(draft);
    }
}
