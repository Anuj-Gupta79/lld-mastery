package com.lld.problems.atm.code.models;

public class Card {
    private int accountNumber;
    private String accountName;
    private int cardNumber;
    private int pin;

    public Card(int accountNumber, String accountName, int pin, int cardNumber) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.cardNumber = cardNumber;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public int getPin() {
        return this.pin;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public int getCardNumber() {
        return this.cardNumber;
    }
}
