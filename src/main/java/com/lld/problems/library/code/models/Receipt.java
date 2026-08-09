package com.lld.problems.library.code.models;

import java.time.LocalDateTime;

import com.lld.problems.library.code.constants.RentalType;

public class Receipt {
    private String bookItemId;
    private String transactionId;
    private RentalType rentalType;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    private double penaltyAmount;

    public Receipt(String bookItemId, String transactionId, RentalType rentalType) {
        this.bookItemId = bookItemId;
        this.transactionId = transactionId;
        this.rentalType = rentalType;
        this.borrowTime = LocalDateTime.now();
        this.penaltyAmount = 0;
    }

    public void setReturnTime() {
        this.returnTime = LocalDateTime.now();
    }

    public void updatePenaltyAmount(double amount) {
        this.penaltyAmount = amount;
    }

    public String getBookItemId() {
        return this.bookItemId;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public RentalType getRentalType() {
        return this.rentalType;
    }

    public LocalDateTime getBorrowTime() {
        return this.borrowTime;
    }

    public LocalDateTime getReturnTime() {
        return this.returnTime;
    }

    public double getPenaltyAmount() {
        return this.penaltyAmount;
    }

    public String toString() {
        return "Receipt { " +
                "BookItemId: " + this.bookItemId + ",\n" +
                "TransactionId: " + this.transactionId + ",\n" +
                "RentalType: " + this.rentalType + ",\n" +
                "BookTime: " + this.borrowTime + ",\n" +
                "penaltyAmount: " + this.penaltyAmount + ",\n" +
                "ReturnTime: " + this.returnTime + ",\n" +
                "}";
    }
}
