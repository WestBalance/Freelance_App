package com.tasklink.controller;

import com.tasklink.dto.CreateProposalRequest;
import com.tasklink.dto.ProposalDto;
import com.tasklink.model.UserAccount;
import com.tasklink.repository.UserAccountRepository;
import com.tasklink.service.Mapper;
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
@RequestMapping("/api/proposals")
public class ProposalController {
    private static final Logger log = LoggerFactory.getLogger(ProposalController.class);
    private final ProposalService service;
    private final UserAccountRepository userRepository;

    public ProposalController(ProposalService service, UserAccountRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ProposalDto create(@Valid @RequestBody CreateProposalRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required to send proposal");
        }

        UserAccount user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));
        if (user.getRole() != com.tasklink.model.Role.FREELANCER) {
            log.warn("Forbidden proposal create attempt by userId={} role={}", user.getId(), user.getRole());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only freelancers can send proposals");
        }
        CreateProposalRequest securedRequest = new CreateProposalRequest(
                request.orderId(),
                user.getId(),
                request.price(),
                request.message(),
                request.attachments()
        );
        return Mapper.toDto(service.create(securedRequest));
    }

    @GetMapping("/order/{orderId}")
    public List<ProposalDto> list(@PathVariable Long orderId) {
        return service.listByOrder(orderId).stream().map(Mapper::toDto).toList();
    }
}
