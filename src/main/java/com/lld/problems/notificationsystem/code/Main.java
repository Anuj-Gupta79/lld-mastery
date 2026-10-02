package com.lld.problems.notificationsystem.code;

import com.lld.problems.notificationsystem.code.constants.Severity;
import com.lld.problems.notificationsystem.code.handlers.EmailHandler;
import com.lld.problems.notificationsystem.code.handlers.NotificationHandler;
import com.lld.problems.notificationsystem.code.handlers.PushHandler;
import com.lld.problems.notificationsystem.code.handlers.SMSHandler;
import com.lld.problems.notificationsystem.code.services.NotificationService;

public class Main {
    public static void main(String[] args) {

        // ---- Chain assembly (wiring only, no request processing here) ----
        NotificationHandler pushHandler = new PushHandler();
        NotificationHandler emailHandler = new EmailHandler();
        NotificationHandler smsHandler = new SMSHandler();

        pushHandler.setNext(emailHandler);
        emailHandler.setNext(smsHandler);
        // smsHandler.next stays null -> end of chain

        NotificationService notificationService = new NotificationService(pushHandler);

        // ---- Valid paths: one per severity ----
        System.out.println("--- LOW severity alert ---");
        notificationService.sendAlert("Disk usage at 60%", Severity.LOW);

        System.out.println("\n--- MEDIUM severity alert ---");
        notificationService.sendAlert("Disk usage at 85%", Severity.MEDIUM);

        System.out.println("\n--- HIGH severity alert ---");
        notificationService.sendAlert("Server is down", Severity.HIGH);

        // ---- Unmatched severity: force exception with a short chain ----
        System.out.println("\n--- Unmatched severity (short chain, only PushHandler) ---");
        NotificationHandler onlyPushHandler = new PushHandler();
        NotificationService brokenChainService = new NotificationService(onlyPushHandler);

        try {
            brokenChainService.sendAlert("This should fail", Severity.HIGH);
        } catch (RuntimeException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
    }
}