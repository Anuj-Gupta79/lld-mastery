package com.lld.problems.atm.code.models;

import java.time.LocalDate;

public class Receipt {
    private String accountName;
    private int accountNumber;
    private int amount;
    private LocalDate date;

    public Receipt(int amount, Card card) {
        this.amount = amount;
        this.accountName = card.getAccountName();
        this.accountNumber = card.getAccountNumber();
        this.date = LocalDate.now();
    }

    public int getAmount() {
        return this.amount;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String toString() {
        return "Receipt{\n" +
                "Account name: " + this.accountName + ",\n" +
                "Account number: " + this.accountNumber + ",\n" +
                "Amount: " + this.amount + ",\n" +
                "Date: " + this.date + ",\n" +
                "}";
    }
}
