package com.tasklink.patterns.behavioral;

import com.tasklink.service.ProposalService;

public record AcceptProposalCommand(ProposalService service) implements OrderCommand {
    @Override
    public void execute(Long orderId, Long proposalId) {
        service.accept(orderId, proposalId);
    }
}
