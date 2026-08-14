package com.lld.problems.splitwise.code.models;

public class SuggestedSettlement {
    private String payerId;
    private String payeeId;
    private double amount;

    public SuggestedSettlement(String payerId, String payeeId, double amount) {
        this.payerId = payerId;
        this.payeeId = payeeId;
        this.amount = amount;
    }

    public String getPayerId() {
        return this.payerId;
    }

    public String getPayeeId() {
        return this.payeeId;
    }

    public double getAmount() {
        return this.amount;
    }
}
