package com.lld.problems.paymentgateway.code.models;

import java.util.UUID;

public class Customer {
    private final String customerId;
    private final Account account;

    public Customer(Account account) {
        this.customerId = UUID.randomUUID().toString();
        this.account = account;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public Account getAccount() {
        return this.account;
    }
}
