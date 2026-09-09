package com.lld.problems.commentsection.code.models;

import java.util.UUID;

public class User {
    private String userId;
    private String userName;

    public User(String name) {
        this.userId = UUID.randomUUID().toString();
        this.userName = name;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }
}
