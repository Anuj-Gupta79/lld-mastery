package com.lld.problems.paymentgateway.code.models;

import java.util.UUID;

import com.lld.problems.paymentgateway.code.exceptions.InsufficientBalanceException;

public class Account {
    private final String accountId;
    private final String accountNumber;
    private long balance;

    public Account(String accountNumber, long balance) {
        this.accountId = UUID.randomUUID().toString();
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public synchronized void debit(long amount) {
        if (this.balance < amount) {
            throw new InsufficientBalanceException("[Error Debit] Insufficient Balance!");
        }

        this.balance -= amount;
    }

    public synchronized void credit(long amount) {
        this.balance += amount;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public synchronized long getBalance() {
        return this.balance;
    }
}
