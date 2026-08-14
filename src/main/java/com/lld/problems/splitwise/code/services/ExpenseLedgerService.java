package com.lld.problems.splitwise.code.services;

import java.util.HashMap;
import java.util.Map;

import com.lld.problems.splitwise.code.models.Expense;
import com.lld.problems.splitwise.code.models.Settlement;
import com.lld.problems.splitwise.code.models.Split;

public class ExpenseLedgerService {
    private Map<String, Double> balance;

    public ExpenseLedgerService() {
        this.balance = new HashMap<>();
    }

    public void updateBalance(Expense expense) {
        for (Split split : expense.getAllSplits()) {
            String key = getKey(split.getUserId(), expense.getPaidById());
            String reverse_key = getKey(expense.getPaidById(), split.getUserId());

            if (this.balance.containsKey(reverse_key) && this.balance.get(reverse_key) > 0.0) {
                double owedAmount = this.balance.get(reverse_key) - split.getAmountOwed();

                if (owedAmount <= 0.0) {
                    this.balance.put(reverse_key, 0.0);
                    if (owedAmount < 0.0) {
                        this.balance.put(key, Math.abs(owedAmount));
                    }
                } else {
                    this.balance.put(reverse_key, Math.abs(owedAmount));
                }
            } else {
                this.balance.merge(key, split.getAmountOwed(),
                        Double::sum);
            }
        }
    }

    public double getBalance(String fromUserId, String toUserId) {
        return this.balance.getOrDefault(getKey(fromUserId, toUserId), 0.0);
    }

    public double getNetBalance(String userId) {
        double amount = 0.0;

        for (String key : this.balance.keySet()) {
            if (key.split("-")[0].equals(userId)) {
                amount -= this.balance.get(key);
            }

            if (key.split("-")[1].equals(userId)) {
                amount += this.balance.get(key);
            }
        }

        return amount;
    }

    public boolean allBalanceZero() {
        for (double value : this.balance.values()) {
            if (Math.abs(value) > 0.01)
                return false;
        }

        return true;
    }

    public boolean hasOutstandingBalance(String userId) {
        for (String key : this.balance.keySet()) {
            if ((key.split("-")[0].equals(userId) || key.split("-")[1].equals(userId))
                    && Math.abs(this.balance.get(key)) > 0.01) {
                return true;
            }
        }

        return false;
    }

    public void updateBalance(Settlement settlement) {
        String key = getKey(settlement.getPayer(), settlement.getPayee());
        this.balance.merge(key, -settlement.getAmount(), Double::sum);
    }

    private String getKey(String id1, String id2) {
        return id1.concat("-").concat(id2);
    }
}
