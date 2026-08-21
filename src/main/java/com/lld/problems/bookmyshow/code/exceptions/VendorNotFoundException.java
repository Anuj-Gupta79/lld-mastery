package com.lld.problems.bookmyshow.code.exceptions;

public class VendorNotFoundException extends RuntimeException {
    public VendorNotFoundException(String message){
        super(message);
    }
}
