package com.tasklink.controller;

import com.tasklink.dto.CreateUserRequest;
import com.tasklink.dto.auth.*;
import com.tasklink.model.UserAccount;
import com.tasklink.repository.UserAccountRepository;
import com.tasklink.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserAccountRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(UserAccountRepository userRepository,
                          AuthenticationManager authenticationManager,
                          UserService userService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/register")
    public AuthSuccessResponse register(@Valid @RequestBody AuthRegisterRequest request) {
        try {
            userService.create(new CreateUserRequest(request.email(), request.password(), request.role()));
            return new AuthSuccessResponse(true);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
    }

    @PostMapping("/login")
    public AuthSuccessResponse login(@Valid @RequestBody AuthLoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.email().toLowerCase(), request.password())
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        httpRequest.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return new AuthSuccessResponse(true);
    }

    @GetMapping("/me")
    public AuthUserResponse me(Authentication authentication) {
        String email = authentication.getName();
        UserAccount user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return new AuthUserResponse(user.getId(), user.getEmail(), user.getRole());
    }

    @PostMapping("/logout")
    public AuthSuccessResponse logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new AuthSuccessResponse(true);
    }
}
