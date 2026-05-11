package com.lld.patterns.structural.bridge;

// LEARNING: Implementor interface — defines the "how to send" contract
// WHY: Declared as interface so abstraction side never depends on a concrete channel
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

// LEARNING: Abstraction class — defines the "what to send" contract and holds a
// reference to the Implementor
// WHY: Abstraction is decoupled from the implementation, allowing them to vary
// independently.
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
        NotificationChannel emailChannel = new EmailChannel();
        NotificationChannel pushChannel = new PushChannel();
        NotificationChannel whatsappChannel = new WhatsAppChannel();

        Notification alertEmail = new AlertNotification(emailChannel);
        Notification reminderPush = new ReminderNotification(pushChannel);
        Notification alertWhatsApp = new AlertNotification(whatsappChannel);

        alertEmail.send("This is an alert email!");
        reminderPush.send("This is a reminder push notification.");
        alertWhatsApp.send("This is an alert WhatsApp message!");
    }
}
