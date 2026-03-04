package com.tasklink.patterns.behavioral;

import com.tasklink.dto.CreateOrderRequest;

public class TitleValidationHandler extends ValidationHandler {
    @Override
    protected void check(CreateOrderRequest request) {
        if (request.title() == null || request.title().length() < 5) {
            throw new IllegalArgumentException("Title must be at least 5 chars");
        }
    }
}
