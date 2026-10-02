package com.lld.problems.notificationsystem.code.decorators;

import com.lld.problems.notificationsystem.code.models.Notification;

public class ShortDescriptionDecorator extends NotificationDecorator {
    public ShortDescriptionDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public String format() {
        String message = getNotification().format();

        if (message.length() > 100) {
            message = message.substring(0, 100);
        }
        return "[Short Description for the problem]:" + "\n" +
                message;
    }
}
