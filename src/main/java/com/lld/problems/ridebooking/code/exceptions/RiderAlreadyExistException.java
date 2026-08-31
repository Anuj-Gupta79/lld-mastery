package com.lld.problems.ridebooking.code.exceptions;

public class RiderAlreadyExistException extends RuntimeException {
    public RiderAlreadyExistException(String message) {
        super(message);
    }
}
