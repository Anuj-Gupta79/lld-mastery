package com.lld.problems.paymentgateway.code.models;

import java.time.LocalDateTime;

import com.lld.problems.paymentgateway.code.constants.PaymentStatus;

public class Response {
    private final String transactionId;
    private final PaymentStatus status;
    private final String message;
    private final long amount;
    private final long refundAmount;
    private final LocalDateTime refundTime;

    private Response(String transactionId, PaymentStatus status, String message, long amount, long refundAmount,
            LocalDateTime refundTime) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
        this.amount = amount;
        this.refundAmount = refundAmount;
        this.refundTime = refundTime;
    }

    public static Response success(String transactionId, long amount) {
        return new Response(transactionId, PaymentStatus.SUCCESS,
                "Payment successful", amount, 0, null);
    }

    public static Response pending(String transactionId, long amount) {
        return new Response(transactionId, PaymentStatus.PENDING,
                "Waiting for UPI confirmation", amount, 0, null);
    }

    public static Response failed(String transactionId, String reason) {
        return new Response(transactionId, PaymentStatus.FAILED,
                reason, 0, 0, null);
    }

    public static Response refunded(String transactionId, long refundAmount,
            LocalDateTime refundTime) {
        return new Response(transactionId, PaymentStatus.REFUNDED,
                "Refund will arrive in 1-2 working days", 0, refundAmount, refundTime);
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public long getAmount() {
        return this.amount;
    }

    public long getRefundAmount() {
        return this.refundAmount;
    }

    public LocalDateTime getRefundTime() {
        return this.refundTime;
    }

}
