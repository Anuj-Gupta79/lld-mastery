package com.lld.problems.splitwise.code.models;

import java.util.List;
import java.util.UUID;

import com.lld.problems.splitwise.code.strategies.ExpenseStrategy;

public class Expense {
    private String expenseId;
    private double amount;
    private String paidById;
    private String description;
    private ExpenseStrategy strategy;
    private List<Split> splits;

    public Expense(double amount, String paidById, String description, ExpenseStrategy strategy,
            List<String> participantsIds) {
        this.expenseId = UUID.randomUUID().toString();
        this.amount = amount;
        this.paidById = paidById;
        this.description = description;
        this.strategy = strategy;

        this.splits = strategy.applyStrategy(amount, participantsIds, this.expenseId);
    }

    public String getExpenseId() {
        return this.expenseId;
    }

    public List<Split> getAllSplits() {
        return this.splits;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getPaidById() {
        return this.paidById;
    }

    public String getDescription() {
        return this.description;
    }
}
