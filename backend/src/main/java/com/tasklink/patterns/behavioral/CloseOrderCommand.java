package com.tasklink.patterns.behavioral;

import com.tasklink.service.OrderService;

public record CloseOrderCommand(OrderService service) implements OrderCommand {
    @Override
    public void execute(Long orderId, Long proposalId) {
        service.close(orderId);
    }
}
