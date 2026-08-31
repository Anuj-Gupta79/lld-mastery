package com.lld.problems.ridebooking.code.exceptions;

public class RideNotFoundException extends RuntimeException{
    public RideNotFoundException(String message) {
        super(message);
    }
}
