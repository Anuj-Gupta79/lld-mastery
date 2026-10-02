package com.lld.problems.notificationsystem.code.handlers;

import com.lld.problems.notificationsystem.code.constants.Severity;
import com.lld.problems.notificationsystem.code.decorators.HeadingDecorator;
import com.lld.problems.notificationsystem.code.decorators.ShortDescriptionDecorator;
import com.lld.problems.notificationsystem.code.models.Notification;

public class PushHandler extends NotificationHandler {

    @Override
    boolean matches(Severity severity) {
        return severity.equals(Severity.LOW);
    }

    @Override
    void send(String formattedString) {
        System.out.println("Sending PUSH notification: " + formattedString);
    }

    @Override
    Notification decorate(Notification notification) {
        Notification shortened = new ShortDescriptionDecorator(notification);
        Notification headed = new HeadingDecorator(shortened);

        return headed;
    }

}
