package com.lld.problems.library.code.models;

import java.util.Objects;

import com.lld.problems.library.code.services.PaymentService;

public class User {
    private String userId;
    private String userName;
    private Pass pass;
    private int currentBooksHeld;

    public User(String id, String name) {
        this.userId = id;
        this.userName = name;
        this.currentBooksHeld = 0;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public Pass getPass() {
        return this.pass;
    }

    public int getCurrentBooksHeld() {
        return this.currentBooksHeld;
    }

    public void updateCurrentBooksHeld(int counter) {
        this.currentBooksHeld += counter;
    }

    public void createPass(int validityDays, double passFees, PaymentService paymentService) {
        Pass pass = new Pass(validityDays);
        System.out.println("Processing payment for pass creation");
        paymentService.processPayment(passFees);
        this.pass = pass;
    }

    public void renewPass(int validityDays, double passFees, PaymentService paymentService) {
        if (this.pass.isActive()) {
            this.pass.extendExpirationDate(validityDays);
        } else {
            this.pass.setExpirationDate(validityDays);
        }

        System.out.println("Processing payment for pass renewable");
        paymentService.processPayment(passFees);
    }

    public boolean isPassValid() {
        return !Objects.isNull(this.pass) && this.pass.isActive();
    }
}
