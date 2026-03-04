package com.tasklink.patterns.behavioral;

import com.tasklink.dto.CreateOrderRequest;

import java.time.LocalDate;

public class DeadlineValidationHandler extends ValidationHandler {
    @Override
    protected void check(CreateOrderRequest request) {
        if (request.deadline() == null || !request.deadline().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline must be in future");
        }
    }
}
