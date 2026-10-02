package com.lld.problems.notificationsystem.code.handlers;

import com.lld.problems.notificationsystem.code.constants.Severity;
import com.lld.problems.notificationsystem.code.decorators.BodyFormatDecorator;
import com.lld.problems.notificationsystem.code.decorators.SubjectDecorator;
import com.lld.problems.notificationsystem.code.models.Notification;

public class EmailHandler extends NotificationHandler {
    @Override
    boolean matches(Severity severity) {
        return severity.equals(Severity.MEDIUM);
    }

    @Override
    void send(String formattedString) {
        System.out.println("Sending Email notification: " + formattedString);
    }

    @Override
    Notification decorate(Notification notification) {
        Notification bodyFormatted = new BodyFormatDecorator(notification);
        Notification subjected = new SubjectDecorator(bodyFormatted);
        return subjected;
    }
}
