package com.lld.problems.splitwise.code.services;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.lld.problems.splitwise.code.core.Group;
import com.lld.problems.splitwise.code.models.SuggestedSettlement;
import com.lld.problems.splitwise.code.models.User;

public class DebtSimplificationService {
    private static class Balance {
        String userId;
        double amount;

        Balance(String userId, double amount) {
            this.userId = userId;
            this.amount = amount;
        }
    }

    public List<SuggestedSettlement> simplify(Group group) {
        List<SuggestedSettlement> suggestedSettlements = new ArrayList<>();
        PriorityQueue<Balance> creditorHeap = new PriorityQueue<>((a, b) -> Double.compare(b.amount, a.amount));
        PriorityQueue<Balance> debtorsHeap = new PriorityQueue<>((a, b) -> Double.compare(b.amount, a.amount));

        for (User user : group.getUsers()) {
            double netAmount = group.getExpenseLedgerService().getNetBalance(user.getUserId());
            if (Math.abs(netAmount) > 0.01) {
                if (netAmount < 0.0) {
                    debtorsHeap.add(new Balance(user.getUserId(), Math.abs(netAmount)));
                } else {
                    creditorHeap.add(new Balance(user.getUserId(), netAmount));
                }
            }
        }

        while (!debtorsHeap.isEmpty() && !creditorHeap.isEmpty()) {
            Balance creditBalance = creditorHeap.poll();
            Balance debitBalance = debtorsHeap.poll();

            double settleAmount = Math.min(creditBalance.amount, debitBalance.amount);

            suggestedSettlements.add(new SuggestedSettlement(debitBalance.userId, creditBalance.userId, settleAmount));

            if (creditBalance.amount > settleAmount) {
                creditorHeap.add(new Balance(creditBalance.userId, creditBalance.amount - settleAmount));
            }

            if (debitBalance.amount > settleAmount) {
                debtorsHeap.add(new Balance(debitBalance.userId, debitBalance.amount - settleAmount));
            }
        }

        return suggestedSettlements;
    }
}
