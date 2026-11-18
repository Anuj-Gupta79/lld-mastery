package com.lld.problems.googledocs.code.exceptions;

public class DocumentNotFoundException extends RuntimeException{
    public  DocumentNotFoundException(String message){
        super(message);
    }
}
