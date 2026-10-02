package com.lld.problems.notificationsystem.code.decorators;

import com.lld.problems.notificationsystem.code.models.Notification;

public class BodyFormatDecorator extends NotificationDecorator {
    public BodyFormatDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public String format() {
        return "Hi" + "\n"
                + getNotification().format()
                + "Thank you!" + "\n"
                + "xyz";
    }

}
