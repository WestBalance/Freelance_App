package com.tasklink.patterns.structural;

import java.math.BigDecimal;

public interface PaymentGateway {
    String charge(BigDecimal amount, String currency, String description);
}
