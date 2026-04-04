package com.tasklink.patterns.behavioral;

import com.tasklink.model.OrderStatus;
import com.tasklink.patterns.creational.singleton.PlatformRuntimeManager;

public class RuntimeOrderObserver implements OrderObserver {
    private final PlatformRuntimeManager runtimeManager;

    public RuntimeOrderObserver(PlatformRuntimeManager runtimeManager) {
        this.runtimeManager = runtimeManager;
    }

    @Override
    public void update(OrderEvent event) {
        if (event.newStatus() == OrderStatus.OPEN || event.newStatus() == OrderStatus.IN_PROGRESS) {
            runtimeManager.markActive(event.orderId(), event.newStatus());
            return;
        }
        runtimeManager.markInactive(event.orderId());
    }
}
