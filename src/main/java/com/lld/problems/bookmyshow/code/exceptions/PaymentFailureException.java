package com.lld.problems.bookmyshow.code.exceptions;

public class PaymentFailureException extends RuntimeException {
    public PaymentFailureException(String message) {
        super(message);
    }
}
