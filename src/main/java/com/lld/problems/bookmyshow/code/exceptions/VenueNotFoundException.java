package com.lld.problems.bookmyshow.code.exceptions;

public class VenueNotFoundException extends RuntimeException {
    public VenueNotFoundException(String message) {
        super(message);
    }
}
