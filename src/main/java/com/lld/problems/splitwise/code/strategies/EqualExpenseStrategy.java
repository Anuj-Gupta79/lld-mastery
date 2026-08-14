package com.lld.problems.splitwise.code.strategies;

import java.util.ArrayList;
import java.util.List;

import com.lld.problems.splitwise.code.exceptions.InvalidParticipantException;
import com.lld.problems.splitwise.code.models.Split;

public class EqualExpenseStrategy implements ExpenseStrategy {

    @Override
    public List<Split> applyStrategy(double amount, List<String> participantIds, String expenseId) {
        if (participantIds.isEmpty()) {
            throw new InvalidParticipantException("There is no person to divide the expense!");
        }

        List<Split> splits = new ArrayList<>();
        int len = participantIds.size();
        double amountOwed = amount / len;
        double rem = amount - (amountOwed * len);

        for (String participantId : participantIds) {

            if (len == 1) {
                amountOwed += rem;
            }

            splits.add(new Split(participantId, expenseId, amountOwed));
            len--;
        }

        return splits;
    }
}
