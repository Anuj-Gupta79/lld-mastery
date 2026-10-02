package com.lld.problems.notificationsystem.code.handlers;

import java.util.Objects;

import com.lld.problems.notificationsystem.code.constants.Severity;
import com.lld.problems.notificationsystem.code.exceptions.UnhandledSeverityException;
import com.lld.problems.notificationsystem.code.models.Notification;

public abstract class NotificationHandler {
    private NotificationHandler next;

    public void setNext(NotificationHandler next) {
        this.next = next;
    }

    public void handle(Severity severity, Notification notification) {
        if (matches(severity)) {
            Notification decoratedNotification = decorate(notification);
            send(decoratedNotification.format());
        } else if (!Objects.isNull(this.next)) {
            this.next.handle(severity, notification);
        } else {
            throw new UnhandledSeverityException("[Error Handle] There is no severity matched as " + severity);
        }
    }

    abstract boolean matches(Severity severity);

    abstract void send(String formattedString);

    abstract Notification decorate(Notification notification);

}
