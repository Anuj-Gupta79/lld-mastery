package com.lld.problems.paymentgateway.code.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lld.problems.paymentgateway.code.constants.PaymentMethod;
import com.lld.problems.paymentgateway.code.constants.PaymentStatus;

public class Transaction {
    private final String transactionId;
    private final Customer customer;
    private final Merchant merchant;
    private final String orderId;
    private final long amount;
    private final PaymentMethod method;
    private final LocalDateTime createdAt;
    private volatile PaymentStatus status;
    private volatile LocalDateTime paidAt;
    private volatile String failureReason;
    private volatile long refundedAmount;
    private volatile LocalDateTime refundTime;

    public Transaction(Customer customer, Merchant merchant, String orderId, long amount, PaymentMethod method) {
        this.transactionId = UUID.randomUUID().toString();
        this.customer = customer;
        this.merchant = merchant;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.refundedAmount = 0;
    }

    public void updateStatus(PaymentStatus status) {
        this.status = status;
    }

    public void updateRefundedAmount(long amount) {
        this.refundedAmount = amount;
    }

    public void updatePaidAt() {
        this.paidAt = LocalDateTime.now();
    }

    public void updateRefundTime() {
        this.refundTime = LocalDateTime.now().plusDays(2);
    }

    public void updateFailureReason(String reason) {
        this.failureReason = reason;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public Merchant getMerchant() {
        return this.merchant;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public long getAmount() {
        return this.amount;
    }

    public PaymentMethod getMethod() {
        return this.method;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public long getRefundedAmount() {
        return this.refundedAmount;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getPaidAt() {
        return this.paidAt;
    }

    public LocalDateTime getRefundTime() {
        return this.refundTime;
    }

    public String getFailureReason() {
        return this.failureReason;
    }

}
