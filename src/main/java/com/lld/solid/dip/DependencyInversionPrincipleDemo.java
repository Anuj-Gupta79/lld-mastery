package com.lld.solid.dip;

class EmailSenderViolation {

    public void sendEmail(String message) {
        System.out.println("Sending email: " + message);
    }
}

// LEARNING: High-level modules should not depend directly on low-level modules.
// WHY: Changing the notification mechanism forces changes in this class.
class NotificationServiceViolation {

    private EmailSenderViolation emailSender;

    public NotificationServiceViolation() {
        this.emailSender = new EmailSenderViolation();
    }

    public void sendNotification(String message) {
        emailSender.sendEmail(message);
    }
}

interface MessageSender {

    // LEARNING: DIP is commonly achieved through abstractions.
    void sendMessage(String message);
}

class EmailSenderFix implements MessageSender {

    @Override
    public void sendMessage(String message) {
        System.out.println("Sending email: " + message);
    }
}

class SMSSenderFix implements MessageSender {

    @Override
    public void sendMessage(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

class WhatsappSenderFix implements MessageSender {

    @Override
    public void sendMessage(String message) {
        System.out.println("Sending WhatsApp message: " + message);
    }
}

class NotificationServiceFix {

    private MessageSender messageSender;

    // LEARNING: Dependency is supplied from outside (Dependency Injection).
    // WHY: NotificationService depends on a contract, not a concrete
    // implementation.
    public NotificationServiceFix(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void sendNotification(String message) {
        messageSender.sendMessage(message);
    }
}

public class DependencyInversionPrincipleDemo {

    public static void main(String[] args) {

        NotificationServiceViolation notificationServiceViolation = new NotificationServiceViolation();

        notificationServiceViolation.sendNotification(
                "Hello, this is a violation of DIP!");

        MessageSender emailSender = new EmailSenderFix();

        NotificationServiceFix notificationServiceFix = new NotificationServiceFix(emailSender);

        notificationServiceFix.sendNotification(
                "Hello, this adheres to DIP!");

        // LEARNING: New senders can be introduced without modifying
        // NotificationService.
        MessageSender smsSender = new SMSSenderFix();

        notificationServiceFix = new NotificationServiceFix(smsSender);

        notificationServiceFix.sendNotification(
                "Hello, this is an SMS notification!");

        MessageSender whatsappSender = new WhatsappSenderFix();

        notificationServiceFix = new NotificationServiceFix(whatsappSender);

        notificationServiceFix.sendNotification(
                "Hello, this is a WhatsApp notification!");
    }
}