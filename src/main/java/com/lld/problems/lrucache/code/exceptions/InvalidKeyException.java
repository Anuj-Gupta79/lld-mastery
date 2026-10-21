package com.lld.problems.lrucache.code.exceptions;

public class InvalidKeyException extends RuntimeException {
    public InvalidKeyException(String message){
        super(message);
    }
}
