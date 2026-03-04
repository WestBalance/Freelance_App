package com.tasklink.controller;

import com.tasklink.dto.PaymentRequest;
import com.tasklink.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }

    @PostMapping
    public Map<String, String> pay(@Valid @RequestBody PaymentRequest request) {
        return Map.of("transactionId", paymentService.payOrder(request.orderId()));
    }
}
