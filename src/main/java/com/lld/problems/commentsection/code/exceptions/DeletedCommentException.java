package com.lld.problems.commentsection.code.exceptions;

public class DeletedCommentException extends RuntimeException {
    public DeletedCommentException(String message) {
        super(message);
    }
}
