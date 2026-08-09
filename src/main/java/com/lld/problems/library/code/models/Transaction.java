package com.lld.problems.library.code.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

import com.lld.problems.library.code.constants.BookStatus;
import com.lld.problems.library.code.constants.RentalType;
import com.lld.problems.library.code.services.PaymentService;

public class Transaction {
    private String transactionId;
    private PaymentService paymentService;
    private Receipt receipt;

    public Transaction(PaymentService paymentService) {
        this.transactionId = UUID.randomUUID().toString();
        this.paymentService = paymentService;
    }

    public Receipt recordBorrowBook(RentalType rentalType, BookItem bookItem) {
        this.receipt = new Receipt(bookItem.getBookItemId(), this.transactionId, rentalType);
        return this.receipt;
    }

    public Receipt recordReturnBook(Pass pass, BookItem bookItem, double latePenaltyFee, double damagePenaltyFee) {
        double penaltyAmount = getTotalPenaltyAmount(pass, bookItem, latePenaltyFee, damagePenaltyFee);

        if (penaltyAmount > 0) {
            this.paymentService.processPayment(penaltyAmount);
        }

        this.receipt.updatePenaltyAmount(penaltyAmount);
        this.receipt.setReturnTime();

        return this.receipt;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public PaymentService getPaymentService() {
        return this.paymentService;
    }

    public Receipt getReceipt() {
        if (Objects.isNull(this.receipt)) {
            throw new NoSuchElementException("There is no receipt exit!");
        }

        return this.receipt;
    }

    private double getTotalPenaltyAmount(Pass pass, BookItem bookItem, double latePenaltyFee, double damagePenaltyFee) {
        double amount = 0;

        if (!pass.isActive() && this.receipt.getRentalType() == RentalType.BORROW) {
            amount += getLatePenaltyAmount(pass.getExpirationDate(), latePenaltyFee);
        }

        if (bookItem.getCurrStatus() == BookStatus.DAMAGE) {
            amount += damagePenaltyFee;
        }

        return amount;
    }

    private double getLatePenaltyAmount(LocalDate expirationDate, double latePenaltyFee) {
        long days = ChronoUnit.DAYS.between(expirationDate, LocalDate.now());
        return days * latePenaltyFee;
    }
}
