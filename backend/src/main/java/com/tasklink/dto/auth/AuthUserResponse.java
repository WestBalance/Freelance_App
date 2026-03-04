package com.tasklink.dto.auth;

import com.tasklink.model.Role;

public record AuthUserResponse(Long id, String email, Role role) {}
