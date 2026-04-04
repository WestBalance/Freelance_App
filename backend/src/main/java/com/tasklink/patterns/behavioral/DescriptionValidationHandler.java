package com.tasklink.patterns.behavioral;

import com.tasklink.dto.CreateOrderRequest;

public class DescriptionValidationHandler extends ValidationHandler {
    @Override
    protected void check(CreateOrderRequest request) {
        if (request.description() == null || request.description().length() < 20) {
            throw new IllegalArgumentException("Description must be at least 20 chars");
        }
    }
}
