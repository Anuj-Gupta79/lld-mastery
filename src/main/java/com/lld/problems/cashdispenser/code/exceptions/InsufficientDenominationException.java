package com.lld.problems.cashdispenser.code.exceptions;

public class InsufficientDenominationException extends RuntimeException {
    public InsufficientDenominationException(String message) {
        super(message);
    }
}
