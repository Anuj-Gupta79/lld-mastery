package com.lld.problems.paymentgateway.code.models;

import java.util.UUID;

public class Merchant {
    private final String merchantId;
    private final Account account;

    public Merchant(Account account) {
        this.merchantId = UUID.randomUUID().toString();
        this.account = account;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public Account getAccount() {
        return this.account;
    }
}
