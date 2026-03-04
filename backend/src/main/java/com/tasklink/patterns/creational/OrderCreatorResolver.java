package com.tasklink.patterns.creational;

import com.tasklink.model.OrderCategory;

public class OrderCreatorResolver {
    public static OrderFactoryMethod resolve(OrderCategory category) {
        return switch (category) {
            case CODING -> new CodingOrderFactory();
            case DESIGN -> new DesignOrderFactory();
            case MARKETING -> new MarketingOrderFactory();
            case WRITING -> new WritingOrderFactory();
            case VIDEO_PRODUCTION -> new VideoOrderFactory();
        };
    }
}
