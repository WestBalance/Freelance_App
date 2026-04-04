package com.tasklink.controller;

import com.tasklink.dto.PaymentRequest;
import com.tasklink.dto.PaymentCheckoutRequest;
import com.tasklink.model.UserAccount;
import com.tasklink.repository.UserAccountRepository;
import com.tasklink.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final UserAccountRepository userRepository;

    public PaymentController(PaymentService paymentService, UserAccountRepository userRepository) {
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public Map<String, String> pay(@Valid @RequestBody PaymentRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required to pay");
        }
        UserAccount user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));
        String transactionId = paymentService.payOrder(request.orderId(), user.getId());
        return Map.of("transactionId", transactionId);
    }

    @PostMapping("/checkout-session")
    public Map<String, String> createCheckoutSession(@Valid @RequestBody PaymentCheckoutRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required to pay");
        }
        UserAccount user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));
        String url = paymentService.createCheckoutSession(
                request.orderId(),
                user.getId(),
                request.successUrl(),
                request.cancelUrl()
        );
        return Map.of("url", url);
    }
}
