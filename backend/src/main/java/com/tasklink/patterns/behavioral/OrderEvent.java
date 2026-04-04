package com.tasklink.patterns.behavioral;

import com.tasklink.model.OrderStatus;

import java.time.Instant;

public record OrderEvent(
        Long orderId,
        String action,
        OrderStatus previousStatus,
        OrderStatus newStatus,
        Instant occurredAt
) {}
