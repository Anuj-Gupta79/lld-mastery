package com.lld.problems.atm.code.models;

import java.time.LocalDateTime;

import com.lld.problems.atm.code.constants.Status;

public class Transaction {
    private int amount;
    private LocalDateTime timestamp;
    private Status status;

    public void record(Status status, int amount) {
        this.amount = amount;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public int getAmount() {
        return this.amount;
    }

    public Status getStatus() {
        return this.status;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }
}