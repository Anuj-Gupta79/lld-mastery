package com.lld.problems.splitwise.code.models;

import java.time.LocalDateTime;

public class Notification {
    private String message;
    private String groupId;
    private LocalDateTime timestamp;

    public Notification(String message, String groupId) {
        this.message = message;
        this.groupId = groupId;
        this.timestamp = LocalDateTime.now();
    }

    public String toString() {
        return "Group: " + this.groupId + ", message: " + this.message + ", time: " + this.timestamp;
    }

    public String getMessage() {
        return this.message;
    }

    public String getGroupId() {
        return this.groupId;
    }
}
