package com.lld.problems.atm.code.state;

import com.lld.problems.atm.code.ATM;
import com.lld.problems.atm.code.models.Card;

public class IdleState implements ATMState {

    @Override
    public void insert(Card card, ATM atm) {
        atm.setCard(card);
        atm.setState(new PinEntryState());
    }

    @Override
    public void enterPin(int pin, ATM atm) {
        throw new UnsupportedOperationException("Unsupported operation as Card is yet to be inserted");
    }

    @Override
    public void enterAmount(int amount, ATM atm) {
        throw new UnsupportedOperationException("Unsupported operation as Card is yet to be inserted");
    }

    @Override
    public void eject(ATM atm) {
        throw new UnsupportedOperationException("Unsupported operation as Card is yet to be inserted");
    }

}
