package com.lld.problems.ridebooking.code.exceptions;

public class DriverAlreadyExistException extends RuntimeException {
    public DriverAlreadyExistException(String message){
        super(message);
    }
}
