package com.lld.problems.atm.code.models;

public class CashInventory {
    private int availableAmount;

    public CashInventory(int amount) {
        this.availableAmount = amount;
    }

    public boolean isSufficientAmount(int amount) {
        return this.availableAmount >= amount;
    }

    public void deductAmount(int amount) {
        this.availableAmount -= amount;
    }

    public int getAvailableAmount() {
        return this.availableAmount;
    }
}
