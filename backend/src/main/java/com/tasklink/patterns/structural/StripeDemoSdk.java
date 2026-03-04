package com.tasklink.patterns.structural;

import java.math.BigDecimal;

public class StripeDemoSdk {
    public String makePayment(BigDecimal amount, String currency, String note) {
        return "stripe_demo_txn_" + amount + "_" + currency + "_" + note.hashCode();
    }
}
