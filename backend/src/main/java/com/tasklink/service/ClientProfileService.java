package com.tasklink.service;

import com.tasklink.dto.ClientOrderWithProposalsDto;
import com.tasklink.dto.ClientProfileDto;
import com.tasklink.dto.OrderDto;
import com.tasklink.dto.ProposalDto;
import com.tasklink.model.OrderStatus;
import com.tasklink.model.TaskOrder;
import com.tasklink.repository.ProposalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientProfileService {
    private final OrderService orderService;
    private final ProposalRepository proposalRepository;

    public ClientProfileService(OrderService orderService, ProposalRepository proposalRepository) {
        this.orderService = orderService;
        this.proposalRepository = proposalRepository;
    }

    public ClientProfileDto get(Long clientId) {
        List<TaskOrder> orders = orderService.listByClient(clientId);
        List<Long> orderIds = orders.stream().map(TaskOrder::getId).toList();
        Map<Long, List<ProposalDto>> proposalsByOrder = proposalRepository.findByOrderIdIn(orderIds).stream()
                .map(Mapper::toDto)
                .collect(Collectors.groupingBy(ProposalDto::orderId));

        List<ClientOrderWithProposalsDto> open = mapByStatuses(orders, List.of(OrderStatus.OPEN, OrderStatus.DRAFT, OrderStatus.PUBLISHED), proposalsByOrder);
        List<ClientOrderWithProposalsDto> inProgress = mapByStatuses(orders, List.of(OrderStatus.IN_PROGRESS), proposalsByOrder);
        List<ClientOrderWithProposalsDto> completed = mapByStatuses(orders, List.of(OrderStatus.COMPLETED, OrderStatus.CLOSED), proposalsByOrder);
        return new ClientProfileDto(clientId, open, inProgress, completed);
    }

    private List<ClientOrderWithProposalsDto> mapByStatuses(List<TaskOrder> orders, List<OrderStatus> statuses, Map<Long, List<ProposalDto>> proposalsByOrder) {
        return orders.stream()
                .filter(order -> statuses.contains(order.getStatus()))
                .map(order -> {
                    OrderDto orderDto = Mapper.toDto(order);
                    List<ProposalDto> proposals = proposalsByOrder.getOrDefault(order.getId(), List.of());
                    return new ClientOrderWithProposalsDto(orderDto, proposals);
                })
                .toList();
    }
}
