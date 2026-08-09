package com.lld.problems.library.code.exceptions;

public class BorrowLimitExceedsException extends RuntimeException {
    public BorrowLimitExceedsException(String message) {
        super(message);
    }
}
