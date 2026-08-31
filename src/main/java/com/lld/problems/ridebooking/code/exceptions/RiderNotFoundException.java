package com.lld.problems.ridebooking.code.exceptions;

public class RiderNotFoundException extends RuntimeException {
    public RiderNotFoundException(String message) {
        super(message);
    }
}
