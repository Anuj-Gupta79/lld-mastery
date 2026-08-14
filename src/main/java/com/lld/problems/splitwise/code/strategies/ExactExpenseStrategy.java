package com.lld.problems.splitwise.code.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.lld.problems.splitwise.code.exceptions.InvalidParticipantException;
import com.lld.problems.splitwise.code.exceptions.InvalidSplitException;
import com.lld.problems.splitwise.code.models.Split;

public class ExactExpenseStrategy implements ExpenseStrategy {
    Map<String, Double> exactMap;

    public ExactExpenseStrategy(Map<String, Double> mp) {
        this.exactMap = mp;
    }

    @Override
    public List<Split> applyStrategy(double amount, List<String> participantIds, String expenseId) {
        List<Split> splits = new ArrayList<>();
        for (String participantId : participantIds) {
            if (Objects.isNull(this.exactMap.get(participantId))) {
                throw new InvalidParticipantException(
                        "ParticipantId: " + participantId + " is not found in exact map");
            }

            splits.add(new Split(participantId, expenseId, this.exactMap.get(participantId)));
            amount -= this.exactMap.get(participantId);
        }

        if (Math.abs(amount) > 0.01) {
            throw new InvalidSplitException("Split Amount is not equal to expense amount!");
        }

        return splits;
    }

}
