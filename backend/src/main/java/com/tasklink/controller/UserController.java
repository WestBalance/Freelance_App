package com.tasklink.controller;

import com.tasklink.dto.CreateUserRequest;
import com.tasklink.model.UserAccount;
import com.tasklink.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @PostMapping
    public UserAccount create(@Valid @RequestBody CreateUserRequest request) {
        return service.create(request);
    }
}
