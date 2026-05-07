package com.tasklink.service;

import com.tasklink.dto.CreateOrderRequest;
import com.tasklink.dto.OrderCloneDraftDto;
import com.tasklink.model.*;
import com.tasklink.patterns.behavioral.*;
import com.tasklink.patterns.creational.*;
import com.tasklink.patterns.creational.singleton.PlatformRuntimeManager;
import com.tasklink.repository.TaskOrderRepository;
import com.tasklink.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final TaskOrderRepository orderRepo;
    private final UserAccountRepository userRepo;
    private final PlatformRuntimeManager runtimeManager = PlatformRuntimeManager.getInstance();
    private final OrderSubject orderSubject = new OrderSubject();

    public OrderService(TaskOrderRepository orderRepo, UserAccountRepository userRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        orderSubject.attach(new RuntimeOrderObserver(runtimeManager));
        orderSubject.attach(new OrderAuditObserver());
    }

    public TaskOrder create(CreateOrderRequest request) {
        buildValidationChain().validate(request);

        UserAccount client = userRepo.findById(request.clientId()).orElseThrow();
        OrderFactoryMethod creator = OrderCreatorResolver.resolve(request.category());
        TaskOrder order = creator.anOperation(request, client);

        TaskOrder saved = orderRepo.save(order);
        emitOrderEvent(saved.getId(), "ORDER_CREATED", null, saved.getStatus());
        return saved;
    }

    public List<TaskOrder> list() { return orderRepo.findAllWithClient(); }

    public TaskOrder get(Long id) { return orderRepo.findByIdWithClient(id).orElseThrow(); }

    public OrderCloneDraftDto cloneOrderDraft(Long id) {
        TaskOrder source = get(id);
        OrderPrototype prototype = (source.getStatus() == OrderStatus.COMPLETED || source.getStatus() == OrderStatus.CLOSED)
                ? new ClosedOrderPrototype(source)
                : new OpenOrderPrototype(source);
        return prototype.cloneOrder();
    }

    public TaskOrder publish(Long id) {
        TaskOrder order = get(id);
        OrderStatus previousStatus = order.getStatus();
        OrderState state = mapState(order);
        order.setStatus(state.publish().status());
        TaskOrder saved = orderRepo.save(order);
        emitOrderEvent(saved.getId(), "ORDER_PUBLISHED", previousStatus, saved.getStatus());
        return saved;
    }

    public TaskOrder close(Long id) {
        TaskOrder order = get(id);
        OrderStatus previousStatus = order.getStatus();
        OrderState state = mapState(order);
        order.setStatus(state.close().status());
        TaskOrder saved = orderRepo.save(order);
        emitOrderEvent(saved.getId(), "ORDER_CLOSED", previousStatus, saved.getStatus());
        return saved;
    }

    public TaskOrder complete(Long id, Long clientId) {
        TaskOrder order = get(id);
        if (!order.getClient().getId().equals(clientId)) {
            throw new IllegalArgumentException("Only order owner can complete order");
        }
        OrderStatus previousStatus = order.getStatus();
        order.setStatus(OrderStatus.COMPLETED);
        TaskOrder saved = orderRepo.save(order);
        emitOrderEvent(saved.getId(), "ORDER_COMPLETED", previousStatus, saved.getStatus());
        return saved;
    }

    public TaskOrder updateBudget(Long id, Long clientId, BigDecimal budget) {
        TaskOrder order = get(id);
        if (!order.getClient().getId().equals(clientId)) {
            throw new IllegalArgumentException("Only order owner can change budget");
        }
        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Budget can be changed only for in-progress order");
        }
        order.setBudget(budget);
        return orderRepo.save(order);
    }

    public List<TaskOrder> listByClient(Long clientId) {
        return orderRepo.findByClientId(clientId);
    }

    public TaskOrder startProgress(Long id) {
        TaskOrder order = get(id);
        OrderStatus previousStatus = order.getStatus();
        OrderState state = mapState(order);
        order.setStatus(state.start().status());
        TaskOrder saved = orderRepo.save(order);
        emitOrderEvent(saved.getId(), "ORDER_STARTED", previousStatus, saved.getStatus());
        return saved;
    }

    private ValidationHandler buildValidationChain() {
        ValidationHandler title = new TitleValidationHandler();
        ValidationHandler description = new DescriptionValidationHandler();
        ValidationHandler budget = new BudgetValidationHandler();
        ValidationHandler deadline = new DeadlineValidationHandler();
        ValidationHandler client = new ClientValidationHandler();
        title.setNext(description).setNext(budget).setNext(deadline).setNext(client);
        return title;
    }

    private OrderState mapState(TaskOrder order) {
        return switch (order.getStatus()) {
            case OPEN, DRAFT, PUBLISHED -> new PublishedState();
            case IN_PROGRESS -> new InProgressState();
            case COMPLETED, CLOSED -> new ClosedState();
        };
    }

    private void emitOrderEvent(Long orderId, String action, OrderStatus previousStatus, OrderStatus newStatus) {
        orderSubject.notifyObservers(new OrderEvent(orderId, action, previousStatus, newStatus, Instant.now()));
    }
}
