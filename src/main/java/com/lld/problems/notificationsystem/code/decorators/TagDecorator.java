package com.lld.problems.notificationsystem.code.decorators;

import com.lld.problems.notificationsystem.code.models.Notification;

public class TagDecorator extends NotificationDecorator {
    public TagDecorator(Notification notification) {
        super(notification);
    }

    public String format() {
        return "[URGENT TASK]" + "\n"
                + getNotification().format();
    }
}
