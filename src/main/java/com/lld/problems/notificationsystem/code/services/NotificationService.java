package com.lld.problems.notificationsystem.code.services;

import com.lld.problems.notificationsystem.code.constants.Severity;
import com.lld.problems.notificationsystem.code.handlers.NotificationHandler;
import com.lld.problems.notificationsystem.code.models.RawNotification;

public class NotificationService {
    private NotificationHandler firstHandler;

    public NotificationService(NotificationHandler handler) {
        this.firstHandler = handler;
    }
    
    public void sendAlert(String message, Severity severity) {
        this.firstHandler.handle(severity, new RawNotification(message));
    }
    
}
