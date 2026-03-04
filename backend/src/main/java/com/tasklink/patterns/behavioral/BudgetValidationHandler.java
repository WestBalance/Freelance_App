package com.tasklink.patterns.behavioral;

import com.tasklink.dto.CreateOrderRequest;

import java.math.BigDecimal;

public class BudgetValidationHandler extends ValidationHandler {
    @Override
    protected void check(CreateOrderRequest request) {
        if (request.budget() == null || request.budget().compareTo(BigDecimal.TEN) < 0) {
            throw new IllegalArgumentException("Budget must be >= 10");
        }
    }
}
