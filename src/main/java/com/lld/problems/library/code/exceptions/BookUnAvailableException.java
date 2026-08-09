package com.lld.problems.library.code.exceptions;

public class BookUnAvailableException extends RuntimeException {
    public BookUnAvailableException(String message) {
        super(message);
    }
}