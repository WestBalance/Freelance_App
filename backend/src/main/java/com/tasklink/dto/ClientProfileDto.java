package com.tasklink.dto;

import java.util.List;

public record ClientProfileDto(
        Long clientId,
        List<ClientOrderWithProposalsDto> openOrders,
        List<ClientOrderWithProposalsDto> inProgressOrders,
        List<ClientOrderWithProposalsDto> completedOrders
) {}
