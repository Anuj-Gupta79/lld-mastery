package com.lld.problems.atm.code;

import java.security.InvalidParameterException;
import java.util.Objects;

import com.lld.problems.atm.code.models.Card;
import com.lld.problems.atm.code.models.CashInventory;
import com.lld.problems.atm.code.models.Receipt;
import com.lld.problems.atm.code.models.Transaction;
import com.lld.problems.atm.code.state.ATMState;
import com.lld.problems.atm.code.state.IdleState;

public class ATM {
    private Card card;
    private ATMState currentState;
    private CashInventory inventory;
    private int retryAttempt;
    private Transaction transaction;
    private Receipt receipt;

    public ATM(CashInventory cashInventory) {
        if (Objects.isNull(cashInventory)) {
            throw new InvalidParameterException("CashInventory cannot be null");
        }

        this.inventory = cashInventory;
        this.currentState = new IdleState();
    }

    public void insert(Card card) {
        this.currentState.insert(card, this);
    }

    public void enterPin(int pin) {
        this.currentState.enterPin(pin, this);
    }

    public void enterAmount(int amount) {
        this.currentState.enterAmount(amount, this);
    }

    public void eject() {
        this.currentState.eject(this);
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setState(ATMState state) {
        this.currentState = state;
    }

    public int getRetryAttempt() {
        return this.retryAttempt;
    }

    public void setRetryAttempt(int attempt) {
        this.retryAttempt = attempt;
    }

    public Card getCard() {
        return this.card;
    }

    public CashInventory getCashInventory() {
        return this.inventory;
    }

    public ATMState getCurrentState() {
        return this.currentState;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public Receipt getReceipt() {
        return this.receipt;
    }
}
