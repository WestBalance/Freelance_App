package com.tasklink.patterns.creational.singleton;

import com.tasklink.model.OrderStatus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlatformRuntimeManager {
    private static PlatformRuntimeManager uniqueInstance;

    private final Map<Long, OrderStatus> activeOrders = new LinkedHashMap<>();
    private final List<String> transactions = new ArrayList<>();

    private PlatformRuntimeManager() {
    }

    public static synchronized PlatformRuntimeManager getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new PlatformRuntimeManager();
        }
        return uniqueInstance;
    }

    public void singletonOperation(String transactionEvent) {
        transactions.add(transactionEvent);
    }

    public Map<String, Object> getSingletonData() {
        return Map.of(
                "activeCount", activeOrders.size(),
                "activeOrders", new LinkedHashMap<>(activeOrders),
                "transactions", new ArrayList<>(transactions)
        );
    }

    public void markActive(Long orderId, OrderStatus status) {
        activeOrders.put(orderId, status);
    }

    public void markInactive(Long orderId) {
        activeOrders.remove(orderId);
    }

    public Map<Long, OrderStatus> getActiveOrders() {
        return new LinkedHashMap<>(activeOrders);
    }

    public int getActiveCount() {
        return activeOrders.size();
    }
}
