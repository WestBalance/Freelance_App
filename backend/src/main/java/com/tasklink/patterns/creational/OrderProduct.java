package com.tasklink.patterns.creational;

import com.tasklink.model.TaskOrder;

public interface OrderProduct {
    TaskOrder createOrder(TaskOrder draft);
}
