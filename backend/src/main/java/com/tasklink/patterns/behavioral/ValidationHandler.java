package com.tasklink.patterns.behavioral;

import com.tasklink.dto.CreateOrderRequest;

public abstract class ValidationHandler {
    private ValidationHandler next;

    public ValidationHandler setNext(ValidationHandler next) {
        this.next = next;
        return next;
    }

    public void validate(CreateOrderRequest request) {
        check(request);
        if (next != null) next.validate(request);
    }

    protected abstract void check(CreateOrderRequest request);
}
