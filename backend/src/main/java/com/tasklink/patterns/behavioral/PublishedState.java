package com.tasklink.patterns.behavioral;

import com.tasklink.model.OrderStatus;

public class PublishedState implements OrderState {
    @Override
    public OrderStatus status() { return OrderStatus.PUBLISHED; }

    @Override
    public OrderState publish() { return this; }

    @Override
    public OrderState start() { return new InProgressState(); }

    @Override
    public OrderState close() { return new ClosedState(); }
}
