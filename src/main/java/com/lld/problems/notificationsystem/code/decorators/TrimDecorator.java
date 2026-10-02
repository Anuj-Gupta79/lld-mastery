package com.lld.problems.notificationsystem.code.decorators;

import com.lld.problems.notificationsystem.code.models.Notification;

public class TrimDecorator extends NotificationDecorator {

    public TrimDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public String format() {
        String message = getNotification().format().trim();

        if (message.length() <= 150)
            return message;

        return message.substring(0, 150);
    }
}
