package com.lld.problems.splitwise.code.strategies;

import java.util.List;

import com.lld.problems.splitwise.code.models.Split;

public interface ExpenseStrategy {
    public List<Split> applyStrategy(double amount, List<String> participantIds, String expenseId);
}
