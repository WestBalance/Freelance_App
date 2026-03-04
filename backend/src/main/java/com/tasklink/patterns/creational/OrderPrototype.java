package com.tasklink.patterns.creational;

import com.tasklink.dto.OrderCloneDraftDto;

public interface OrderPrototype {
    OrderCloneDraftDto cloneOrder();
}
