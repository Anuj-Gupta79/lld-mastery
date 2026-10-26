package com.lld.problems.ratelimiter.code.exceptions;

public class InvalidClockException extends RuntimeException {
    public InvalidClockException(String message){
        super(message);
    }
}
