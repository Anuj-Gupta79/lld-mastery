package com.lld.problems.paymentgateway.code.models;

import java.util.Objects;

public class TransactionKey {
    private final String customerId;
    private final String orderId;

    public TransactionKey(String customerId, String orderId) {
        this.customerId = customerId;
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public String getOrderId() {
        return this.orderId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof TransactionKey tkObj)) {
            return false;
        }

        return Objects.equals(this.customerId, tkObj.getCustomerId())
                && Objects.equals(this.orderId, tkObj.getOrderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.customerId, this.orderId);
    }
}
