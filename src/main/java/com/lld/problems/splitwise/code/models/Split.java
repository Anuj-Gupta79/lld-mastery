package com.lld.problems.splitwise.code.models;

public class Split {
    private String userId;
    private String expenseId;
    private double amountOwed;

    public Split(String userId, String expenseId, double amount) {
        this.userId = userId;
        this.expenseId = expenseId;
        this.amountOwed = amount;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getExpenseId() {
        return this.expenseId;
    }

    public double getAmountOwed() {
        return this.amountOwed;
    }
}
