package com.lld.solid.dip;

class EmailSenderViolation {
    public void sendEmail(String message) {
        System.out.println("Sending email: " + message);
    }
}

// Violation of DIP because NotificationServiceViolation is tightly coupled to
// EmailSenderViolation. If we want to change the way notifications are sent
// (e.g., using SMS or WhatsApp), we would need to modify the
// NotificationServiceViolation class, which violates the principle of being
// closed for modification.
class NotificationServiceViolation {
    private EmailSenderViolation emailSender;

    public NotificationServiceViolation() {
        this.emailSender = new EmailSenderViolation();
    }

    public void sendNotification(String message) {
        emailSender.sendEmail(message);
    }
}

// Satisfying DIP by introducing an abstraction (MessageSender) that both
// EmailSenderFix and NotificationServiceFix depend on. This way, we can easily
// extend the functionality to support other types of message senders (like SMS
// or WhatsApp) without modifying the NotificationServiceFix class, adhering to
// the principle of being open for extension but closed for modification.
interface MessageSender {
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

    public NotificationServiceFix(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void sendNotification(String message) {
        messageSender.sendMessage(message);
    }
}

// DIP says: "High-level modules should not depend on low-level modules. Both
// should depend on abstractions. Abstractions should not depend on details.
// Details should depend on abstractions." In this example,
// NotificationServiceViolation is a
// high-level module that depends on the low -level module EmailSenderViolation.
// In contrast, NotificationServiceFix depends on the abstraction MessageSender,
// and both EmailSenderFix and NotificationServiceFix depend on this
// abstraction, adhering to the Dependency Inversion Principle.
public class DependencyInversionPrincipleDemo {

    public static void main(String[] args) {
        // Violation example
        NotificationServiceViolation notificationServiceViolation = new NotificationServiceViolation();
        notificationServiceViolation.sendNotification("Hello, this is a violation of DIP!");

        // Fix example
        MessageSender emailSender = new EmailSenderFix();
        NotificationServiceFix notificationServiceFix = new NotificationServiceFix(emailSender);
        notificationServiceFix.sendNotification("Hello, this adheres to DIP!");

        // We can easily switch to SMS or WhatsApp without modifying the
        // NotificationServiceFix class.
        MessageSender smsSender = new SMSSenderFix();
        notificationServiceFix = new NotificationServiceFix(smsSender);
        notificationServiceFix.sendNotification("Hello, this is an SMS notification!");

        MessageSender whatsappSender = new WhatsappSenderFix();
        notificationServiceFix = new NotificationServiceFix(whatsappSender);
        notificationServiceFix.sendNotification("Hello, this is a WhatsApp notification!");
    }
}
