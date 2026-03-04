package com.tasklink.patterns.behavioral;

import com.tasklink.model.OrderStatus;

public class InProgressState implements OrderState {
    @Override
    public OrderStatus status() { return OrderStatus.IN_PROGRESS; }

    @Override
    public OrderState publish() { return this; }

    @Override
    public OrderState start() { return this; }

    @Override
    public OrderState close() { return new ClosedState(); }
}
