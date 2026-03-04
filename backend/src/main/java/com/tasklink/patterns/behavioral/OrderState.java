package com.tasklink.patterns.behavioral;

import com.tasklink.model.OrderStatus;

public interface OrderState {
    OrderStatus status();
    OrderState publish();
    OrderState start();
    OrderState close();
}
