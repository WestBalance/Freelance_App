package com.tasklink.patterns.behavioral;

import com.tasklink.model.TaskOrder;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculate(TaskOrder order);
}
