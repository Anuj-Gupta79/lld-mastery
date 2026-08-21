package com.lld.problems.bookmyshow.code.exceptions;

public class BookingNotFoundException extends RuntimeException{
    public BookingNotFoundException(String message) {
        super(message);
    }
}
