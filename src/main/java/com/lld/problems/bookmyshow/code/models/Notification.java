package com.lld.problems.bookmyshow.code.models;

import java.time.LocalDateTime;

public class Notification {
    private String notificationId;
    private String message;
    private User recipient;
    private LocalDateTime timestamp;

    public Notification(String id, String message, User recipient) {
        this.notificationId = id;
        this.message = message;
        this.recipient = recipient;
        this.timestamp = LocalDateTime.now();
    }

    public String getNotificationId() {
        return this.notificationId;
    }

    public String getMessage() {
        return this.message;
    }

    public User getRecipient() {
        return this.recipient;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String toString() {
        return this.timestamp + ": id= " + this.notificationId + ", message= " + this.message + ", to user= "
                + this.recipient;
    }
}
