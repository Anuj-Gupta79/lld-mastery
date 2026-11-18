package com.lld.problems.googledocs.code.models;

import java.util.UUID;

import com.lld.problems.googledocs.code.operations.Operation;

public class User implements DocumentListener {
    private final String userId;
    private final String name;

    public User(String name) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
    }

    @Override
    public void onChange(String docId, Operation op) {
        System.out.println("[On Change] Document has been changed with id = " + docId + ", notify to = "
                + this.name);
    }

    public String getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

}
