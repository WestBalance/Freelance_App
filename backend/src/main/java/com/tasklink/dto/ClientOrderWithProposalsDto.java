package com.tasklink.dto;

import java.util.List;

public record ClientOrderWithProposalsDto(
        OrderDto order,
        List<ProposalDto> proposals
) {}
