package com.tasklink.service;

import com.tasklink.dto.CreateOrderRequest;
import com.tasklink.dto.OrderCloneDraftDto;
import com.tasklink.model.*;
import com.tasklink.patterns.behavioral.ClosedState;
import com.tasklink.patterns.behavioral.DraftState;
import com.tasklink.patterns.behavioral.InProgressState;
import com.tasklink.patterns.behavioral.OrderState;
import com.tasklink.patterns.behavioral.PricingStrategy;
import com.tasklink.patterns.behavioral.FixedPricingStrategy;
import com.tasklink.patterns.behavioral.HourlyPricingStrategy;
import com.tasklink.patterns.creational.*;
import com.tasklink.patterns.creational.singleton.PlatformRuntimeManager;
import com.tasklink.patterns.structural.*;
import com.tasklink.repository.TaskOrderRepository;
import com.tasklink.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final TaskOrderRepository orderRepo;
    private final UserAccountRepository userRepo;
    private final PlatformRuntimeManager runtimeManager = PlatformRuntimeManager.getInstance();

    public OrderService(TaskOrderRepository orderRepo, UserAccountRepository userRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    public TaskOrder create(CreateOrderRequest request) {
        UserAccount client = userRepo.findById(request.clientId()).orElseThrow();
        OrderFactoryMethod creator = OrderCreatorResolver.resolve(request.category());
        TaskOrder order = creator.anOperation(request, client);

        OrderCostComponent decorated = new BaseOrderCost(order.getBudget());
        if (request.urgent()) decorated = new UrgentOrderDecorator(decorated);
        if (request.featured()) decorated = new FeaturedOrderDecorator(decorated);
        order.setBudget(decorated.budget());
        order.setPriorityScore(order.getPriorityScore() + decorated.priorityBoost());

        PricingStrategy strategy = request.pricingMode() == PricingMode.HOURLY ? new HourlyPricingStrategy() : new FixedPricingStrategy();
        order.setBudget(strategy.calculate(order));

        TaskOrder saved = orderRepo.save(order);
        if (saved.getStatus() == OrderStatus.OPEN || saved.getStatus() == OrderStatus.IN_PROGRESS) {
            runtimeManager.markActive(saved.getId(), saved.getStatus());
        }
        return saved;
    }

    public List<TaskOrder> list() { return orderRepo.findAll(); }

    public TaskOrder get(Long id) { return orderRepo.findById(id).orElseThrow(); }

    public OrderCloneDraftDto cloneOrderDraft(Long id) {
        TaskOrder source = get(id);
        OrderPrototype prototype = (source.getStatus() == OrderStatus.COMPLETED || source.getStatus() == OrderStatus.CLOSED)
                ? new ClosedOrderPrototype(source)
                : new OpenOrderPrototype(source);
        return prototype.cloneOrder();
    }

    public TaskOrder publish(Long id) {
        TaskOrder order = get(id);
        OrderState state = switch (order.getStatus()) {
            case OPEN, DRAFT, PUBLISHED -> new DraftState();
            case IN_PROGRESS -> new InProgressState();
            case COMPLETED, CLOSED -> new ClosedState();
        };
        order.setStatus(state.publish().status());
        TaskOrder saved = orderRepo.save(order);
        updateActiveCache(saved);
        return saved;
    }

    public TaskOrder close(Long id) {
        TaskOrder order = get(id);
        OrderState state = switch (order.getStatus()) {
            case OPEN, DRAFT, PUBLISHED -> new DraftState();
            case IN_PROGRESS -> new InProgressState();
            case COMPLETED, CLOSED -> new ClosedState();
        };
        order.setStatus(state.close().status());
        TaskOrder saved = orderRepo.save(order);
        updateActiveCache(saved);
        return saved;
    }

    public TaskOrder complete(Long id, Long clientId) {
        TaskOrder order = get(id);
        if (!order.getClient().getId().equals(clientId)) {
            throw new IllegalArgumentException("Only order owner can complete order");
        }
        order.setStatus(OrderStatus.COMPLETED);
        TaskOrder saved = orderRepo.save(order);
        updateActiveCache(saved);
        return saved;
    }

    public List<TaskOrder> listByClient(Long clientId) {
        return orderRepo.findByClientId(clientId);
    }

    public TaskOrder startProgress(Long id) {
        TaskOrder order = get(id);
        OrderState state = switch (order.getStatus()) {
            case OPEN, DRAFT, PUBLISHED -> new DraftState();
            case IN_PROGRESS -> new InProgressState();
            case COMPLETED, CLOSED -> new ClosedState();
        };
        order.setStatus(state.start().status());
        TaskOrder saved = orderRepo.save(order);
        updateActiveCache(saved);
        return saved;
    }

    private void updateActiveCache(TaskOrder order) {
        if (order.getStatus() == OrderStatus.OPEN || order.getStatus() == OrderStatus.IN_PROGRESS) {
            runtimeManager.markActive(order.getId(), order.getStatus());
        } else {
            runtimeManager.markInactive(order.getId());
        }
    }
}
