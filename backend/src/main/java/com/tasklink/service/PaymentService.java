package com.tasklink.service;

import com.tasklink.model.TaskOrder;
import com.tasklink.patterns.creational.singleton.PlatformRuntimeManager;
import com.tasklink.patterns.structural.PaymentFacade;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final OrderService orderService;
    private final PaymentFacade facade;
    private final PlatformRuntimeManager runtimeManager = PlatformRuntimeManager.getInstance();

    public PaymentService(OrderService orderService, PaymentFacade facade) {
        this.orderService = orderService;
        this.facade = facade;
    }

    public String payOrder(Long orderId) {
        TaskOrder order = orderService.get(orderId);
        String transactionId = facade.pay(order.getBudget(), order.getCurrency(), "Payment for order #" + order.getId());
        runtimeManager.singletonOperation("PAYMENT:" + transactionId + ":order=" + orderId);
        return transactionId;
    }
}
