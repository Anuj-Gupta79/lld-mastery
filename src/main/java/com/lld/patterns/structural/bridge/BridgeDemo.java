package com.lld.patterns.structural.bridge;

// LEARNING: Implementor interface — defines the "how to send" contract.
// WHY: Abstraction side depends on this interface, never on a concrete channel.
interface NotificationChannel {
    void send(String message);
}

class EmailChannel implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("Sending email: " + message);
    }
}

class SMSChannel implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

class PushChannel implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("Sending push notification: " + message);
    }
}

class WhatsAppChannel implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("Sending WhatsApp message: " + message);
    }
}

// LEARNING: Abstraction holds a reference to Implementor — this is the bridge.
// WHY: Notification type (what) and channel (how) vary independently; no class
// explosion.
abstract class Notification {
    protected NotificationChannel channel;

    public Notification(NotificationChannel channel) {
        this.channel = channel;
    }

    public abstract void send(String message);
}

class AlertNotification extends Notification {
    public AlertNotification(NotificationChannel channel) {
        super(channel);
    }

    @Override
    public void send(String message) {
        channel.send("URGENT: " + message);
    }
}

class ReminderNotification extends Notification {
    public ReminderNotification(NotificationChannel channel) {
        super(channel);
    }

    @Override
    public void send(String message) {
        channel.send("Reminder: " + message);
    }
}

public class BridgeDemo {
    public static void main(String[] args) {
        Notification alertEmail = new AlertNotification(new EmailChannel());
        Notification reminderPush = new ReminderNotification(new PushChannel());
        Notification alertWhatsApp = new AlertNotification(new WhatsAppChannel());

        alertEmail.send("This is an alert email!");
        reminderPush.send("This is a reminder push notification.");
        alertWhatsApp.send("This is an alert WhatsApp message!");
    }
}