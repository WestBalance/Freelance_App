package com.tasklink.service;

import com.tasklink.model.TaskOrder;
import com.tasklink.model.PricingMode;
import com.tasklink.patterns.behavioral.FixedPricingStrategy;
import com.tasklink.patterns.behavioral.HourlyPricingStrategy;
import com.tasklink.patterns.behavioral.PricingStrategy;
import com.tasklink.patterns.creational.singleton.PlatformRuntimeManager;
import com.tasklink.patterns.structural.CheckoutDescriptionBridge;
import com.tasklink.patterns.structural.DefaultPaymentDescriptionImplementor;
import com.tasklink.patterns.structural.PaymentFacade;
import com.tasklink.patterns.structural.PaymentDescriptionBridge;
import com.tasklink.patterns.structural.StripeAdapter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    private final OrderService orderService;
    private final PaymentFacade facade;
    private final StripeAdapter stripeAdapter;
    private final PlatformRuntimeManager runtimeManager = PlatformRuntimeManager.getInstance();

    public PaymentService(OrderService orderService, PaymentFacade facade, StripeAdapter stripeAdapter) {
        this.orderService = orderService;
        this.facade = facade;
        this.stripeAdapter = stripeAdapter;
    }

    public String payOrder(Long orderId, Long payerId) {
        TaskOrder order = orderService.get(orderId);
        if (runtimeManager.isPaid(orderId)) {
            throw new IllegalStateException("Order is already paid");
        }
        BigDecimal payableAmount = resolvePricingStrategy(order.getPricingMode()).calculate(order);
        String transactionId = facade.pay(order, payerId, payableAmount);
        runtimeManager.markPaid(orderId, transactionId);
        runtimeManager.singletonOperation("PAYMENT:" + transactionId + ":order=" + orderId);
        return transactionId;
    }

    public String createCheckoutSession(Long orderId, Long payerId, String successUrl, String cancelUrl) {
        TaskOrder order = orderService.get(orderId);
        if (!order.getClient().getId().equals(payerId)) {
            throw new IllegalStateException("Only the client-owner can pay for this order");
        }
        if (runtimeManager.isPaid(orderId)) {
            throw new IllegalStateException("Order is already paid");
        }

        BigDecimal payableAmount = resolvePricingStrategy(order.getPricingMode()).calculate(order);
        PaymentDescriptionBridge descriptionBridge = new CheckoutDescriptionBridge(new DefaultPaymentDescriptionImplementor());
        return stripeAdapter.createCheckoutSession(
                payableAmount,
                order.getCurrency(),
                descriptionBridge.build(order, payerId),
                successUrl,
                cancelUrl
        );
    }

    private PricingStrategy resolvePricingStrategy(PricingMode pricingMode) {
        return pricingMode == PricingMode.HOURLY ? new HourlyPricingStrategy() : new FixedPricingStrategy();
    }
}
