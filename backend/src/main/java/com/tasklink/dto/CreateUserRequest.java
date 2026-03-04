package com.tasklink.dto;

import com.tasklink.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotNull Role role
) {}
