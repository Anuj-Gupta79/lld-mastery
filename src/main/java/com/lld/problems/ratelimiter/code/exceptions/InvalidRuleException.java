package com.lld.problems.ratelimiter.code.exceptions;

public class InvalidRuleException extends RuntimeException {
    public InvalidRuleException(String message){
        super(message);
    }
}
