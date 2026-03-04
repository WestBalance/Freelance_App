package com.tasklink.dto.auth;

import com.tasklink.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthRegisterRequest(
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotNull Role role
) {}
