package com.tasklink.patterns.behavioral;

import java.util.ArrayList;
import java.util.List;

public class OrderSubject {
    private final List<OrderObserver> observers = new ArrayList<>();

    public void attach(OrderObserver observer) {
        observers.add(observer);
    }

    public void detach(OrderObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(OrderEvent event) {
        for (OrderObserver observer : observers) {
            observer.update(event);
        }
    }
}
