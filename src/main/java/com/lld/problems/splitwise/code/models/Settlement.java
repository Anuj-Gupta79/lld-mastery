package com.lld.problems.splitwise.code.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lld.problems.splitwise.code.constants.PaymentType;

public class Settlement {
    private String settlementId;
    private String payer;
    private String payee;
    private double amount;
    private PaymentType paymentType;
    private LocalDateTime timestamp;

    public Settlement(String payer, String payee, double amount, PaymentType paymentType) {
        this.settlementId = UUID.randomUUID().toString();
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
        this.paymentType = paymentType;
        this.timestamp = LocalDateTime.now();
    }

    public String getSettlementId() {
        return this.settlementId;
    }

    public String getPayer() {
        return this.payer;
    }

    public String getPayee() {
        return this.payee;
    }

    public double getAmount() {
        return this.amount;
    }

    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }
}
