package com.lld.problems.notificationsystem.code.decorators;

import com.lld.problems.notificationsystem.code.models.Notification;

public abstract class NotificationDecorator implements Notification {
    private Notification wrapped;

    public NotificationDecorator(Notification notification) {
        this.wrapped = notification;
    }

    public Notification getNotification() {
        return this.wrapped;
    }

    @Override
    public abstract String format();
}
