package com.tasklink.patterns.behavioral;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderAuditObserver implements OrderObserver {
    private static final Logger log = LoggerFactory.getLogger(OrderAuditObserver.class);

    @Override
    public void update(OrderEvent event) {
        log.info(
                "Order event: orderId={}, action={}, previousStatus={}, newStatus={}, at={}",
                event.orderId(),
                event.action(),
                event.previousStatus(),
                event.newStatus(),
                event.occurredAt()
        );
    }
}
