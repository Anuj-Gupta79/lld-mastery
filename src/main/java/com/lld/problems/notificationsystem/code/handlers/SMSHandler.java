package com.lld.problems.notificationsystem.code.handlers;

import com.lld.problems.notificationsystem.code.constants.Severity;
import com.lld.problems.notificationsystem.code.decorators.TagDecorator;
import com.lld.problems.notificationsystem.code.decorators.TrimDecorator;
import com.lld.problems.notificationsystem.code.models.Notification;

public class SMSHandler extends NotificationHandler {
    @Override
    boolean matches(Severity severity) {
        return severity.equals(Severity.HIGH);
    }

    @Override
    void send(String formattedString) {
        System.out.println("Sending SMS notification: " + formattedString);
    }

    @Override
    Notification decorate(Notification notification) {
        Notification trimmed = new TrimDecorator(notification);
        Notification tagged = new TagDecorator(trimmed);
        return tagged;
    }
}
