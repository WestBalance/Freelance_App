package com.tasklink.controller;

import com.tasklink.dto.CreateOrderRequest;
import com.tasklink.dto.OrderCloneDraftDto;
import com.tasklink.dto.OrderDto;
import com.tasklink.model.UserAccount;
import com.tasklink.patterns.behavioral.*;
import com.tasklink.repository.UserAccountRepository;
import com.tasklink.service.Mapper;
import com.tasklink.service.OrderService;
import com.tasklink.service.ProposalService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final ProposalService proposalService;
    private final UserAccountRepository userRepository;

    public OrderController(OrderService orderService, ProposalService proposalService, UserAccountRepository userRepository) {
        this.orderService = orderService;
        this.proposalService = proposalService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<OrderDto> all() {
        return orderService.list().stream().map(Mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public OrderDto one(@PathVariable Long id) {
        return Mapper.toDto(orderService.get(id));
    }

    @GetMapping("/{id}/clone")
    public OrderCloneDraftDto cloneDraft(@PathVariable Long id) {
        return orderService.cloneOrderDraft(id);
    }

    @PostMapping
    public OrderDto create(@Valid @RequestBody CreateOrderRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required to create order");
        }

        UserAccount user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));
        if (user.getRole() != com.tasklink.model.Role.CLIENT) {
            log.warn("Forbidden order create attempt by userId={} role={}", user.getId(), user.getRole());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only clients can create orders");
        }
        CreateOrderRequest securedRequest = new CreateOrderRequest(
                request.title(),
                request.description(),
                request.category(),
                request.budget(),
                request.deadline(),
                request.minRating(),
                request.pricingMode(),
                request.estimatedHours(),
                request.urgent(),
                request.featured(),
                user.getId()
        );
        return Mapper.toDto(orderService.create(securedRequest));
    }

    @PostMapping("/{id}/publish")
    public OrderDto publish(@PathVariable Long id) {
        OrderCommand cmd = new PublishOrderCommand(orderService);
        cmd.execute(id, null);
        return Mapper.toDto(orderService.get(id));
    }

    @PostMapping("/{id}/close")
    public OrderDto close(@PathVariable Long id) {
        OrderCommand cmd = new CloseOrderCommand(orderService);
        cmd.execute(id, null);
        return Mapper.toDto(orderService.get(id));
    }

    @PostMapping("/{orderId}/accept/{proposalId}")
    public OrderDto accept(@PathVariable Long orderId, @PathVariable Long proposalId, Authentication authentication) {
        UserAccount user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        if (!orderService.get(orderId).getClient().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only order owner can accept proposal");
        }
        OrderCommand cmd = new AcceptProposalCommand(proposalService);
        cmd.execute(orderId, proposalId);
        return Mapper.toDto(orderService.get(orderId));
    }

    @PostMapping("/{id}/complete")
    public OrderDto complete(@PathVariable Long id, Authentication authentication) {
        UserAccount user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return Mapper.toDto(orderService.complete(id, user.getId()));
    }
}
