package com.lld.problems.lrucache.code.exceptions;

public class InvalidCapacityException extends RuntimeException {
    public InvalidCapacityException(String message){
        super(message);
    }
}
