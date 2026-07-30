package com.lld.problems.atm.code.state;

import com.lld.problems.atm.code.ATM;
import com.lld.problems.atm.code.models.Card;

public interface ATMState {
    public void insert(Card card, ATM atm);

    public void enterPin(int pin, ATM atm);

    public void enterAmount(int amount, ATM atm);

    default void eject(ATM atm) {
        atm.setCard(null);
        atm.setState(new IdleState());
        atm.setRetryAttempt(0);
        System.out.println("Please take out your card!");
    }
}
