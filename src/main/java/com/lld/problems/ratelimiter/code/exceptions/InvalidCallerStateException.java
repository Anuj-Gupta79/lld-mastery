package com.lld.problems.ratelimiter.code.exceptions;

public class InvalidCallerStateException extends RuntimeException {
    public InvalidCallerStateException(String message) {
        super(message);
    }
}
