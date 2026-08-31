package com.lld.problems.ridebooking.code.exceptions;

public class NoDriverAvailableException extends RuntimeException {
    public NoDriverAvailableException(String message) {
        super(message);
    }
}
