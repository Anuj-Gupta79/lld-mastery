package com.lld.problems.splitwise.code.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.lld.problems.splitwise.code.exceptions.InvalidParticipantException;
import com.lld.problems.splitwise.code.exceptions.InvalidSplitException;
import com.lld.problems.splitwise.code.models.Split;

public class PercentageExpenseStrategy implements ExpenseStrategy {
    Map<String, Double> percentageMap;

    public PercentageExpenseStrategy(Map<String, Double> mp) {
        this.percentageMap = mp;
    }

    @Override
    public List<Split> applyStrategy(double amount, List<String> participantIds, String expenseId) {
        List<Split> splits = new ArrayList<>();
        double percentage = 0.0;
        for (String participantId : participantIds) {
            if (Objects.isNull(this.percentageMap.get(participantId))) {
                throw new InvalidParticipantException(
                        "ParticipantId: " + participantId + " is not found in percentage map");
            }

            double amountOwed = this.percentageMap.get(participantId) * amount / 100.0;
            splits.add(new Split(participantId, expenseId, amountOwed));
            percentage += this.percentageMap.get(participantId);
        }

        if (Math.abs(percentage - 100) > 0.01) {
            throw new InvalidSplitException("Distributed percentage is greater than 100 which is not possible!");
        }

        return splits;
    }

}
