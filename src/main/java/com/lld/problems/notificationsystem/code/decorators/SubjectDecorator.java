package com.lld.problems.notificationsystem.code.decorators;

import com.lld.problems.notificationsystem.code.models.Notification;

public class SubjectDecorator extends NotificationDecorator {
    public SubjectDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public String format() {
        return "Subject: " + "\n"
                + getNotification().format();
    }
}
