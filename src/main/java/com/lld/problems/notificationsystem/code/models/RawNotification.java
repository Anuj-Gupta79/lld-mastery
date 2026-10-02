package com.lld.problems.notificationsystem.code.models;

public class RawNotification implements Notification {
    private String message;

    public RawNotification(String message) {
        this.message = message;
    }

    @Override
    public String format() {
        return this.message;
    }
}
