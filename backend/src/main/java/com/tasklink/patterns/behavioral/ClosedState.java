package com.tasklink.patterns.behavioral;

import com.tasklink.model.OrderStatus;

public class ClosedState implements OrderState {
    @Override
    public OrderStatus status() { return OrderStatus.CLOSED; }

    @Override
    public OrderState publish() { return this; }

    @Override
    public OrderState start() { return this; }

    @Override
    public OrderState close() { return this; }
}
