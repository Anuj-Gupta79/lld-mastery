package com.lld.problems.paymentgateway.code.models;

import com.lld.problems.paymentgateway.code.constants.PaymentStatus;

public class PaymentProvider {
    private final String name;
    private final PaymentStatus response;
    private volatile boolean active;

    public PaymentProvider(String name, PaymentStatus status) {
        this.name = name;
        this.response = status;
        this.active = true;
    }

    public PaymentStatus charge(long amount) {
        return this.response;
    }

    public void deactivate() {
        this.active = false;
    }

    public String getName() {
        return this.name;
    }

    public boolean isActive() {
        return this.active;
    }
}
