package com.tasklink.patterns.behavioral;

public interface OrderCommand {
    void execute(Long orderId, Long proposalId);
}
