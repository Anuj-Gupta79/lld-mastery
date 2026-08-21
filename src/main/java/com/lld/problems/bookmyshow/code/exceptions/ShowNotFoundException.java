package com.lld.problems.bookmyshow.code.exceptions;

public class ShowNotFoundException extends RuntimeException {
    public ShowNotFoundException(String message){
        super(message);
    }
}
