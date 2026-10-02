package com.lld.problems.notificationsystem.code.decorators;

import com.lld.problems.notificationsystem.code.models.Notification;

public class HeadingDecorator extends NotificationDecorator {

    public HeadingDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public String format() {
        return "Title: " + "\n"
                + getNotification().format();
    }

}
