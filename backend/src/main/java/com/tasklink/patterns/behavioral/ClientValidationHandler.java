package com.tasklink.patterns.behavioral;

import com.tasklink.dto.CreateOrderRequest;

public class ClientValidationHandler extends ValidationHandler {
    @Override
    protected void check(CreateOrderRequest request) {
        if (request.clientId() == null) {
            throw new IllegalArgumentException("Client id is required");
        }
    }
}
