package com.tasklink.patterns.behavioral;

import com.tasklink.model.OrderStatus;

public class DraftState implements OrderState {
    @Override
    public OrderStatus status() { return OrderStatus.OPEN; }

    @Override
    public OrderState publish() { return new PublishedState(); }

    @Override
    public OrderState start() { return this; }

    @Override
    public OrderState close() { return this; }
}
