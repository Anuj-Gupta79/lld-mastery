package com.lld.problems.atm.code.state;

import java.security.InvalidParameterException;

import com.lld.problems.atm.code.ATM;
import com.lld.problems.atm.code.models.Card;

public class PinEntryState implements ATMState {

    @Override
    public void insert(Card card, ATM atm) {
        throw new UnsupportedOperationException("Unsupported Operation as Card is already inserted!");
    }

    @Override
    public void enterPin(int pin, ATM atm) {
        boolean isValidPin = validatePin(atm.getCard().getPin(), pin);

        if (isValidPin) {
            atm.setState(new AmountEntryState());
        } else {
            atm.setRetryAttempt(atm.getRetryAttempt() + 1);
            if (atm.getRetryAttempt() >= 3) {
                eject(atm);
            } else {
                throw new InvalidParameterException("Wrong Pin! Try again");
            }
        }

    }

    @Override
    public void enterAmount(int amount, ATM atm) {
        throw new UnsupportedOperationException("Unsupported Operation as User yet to insert the pin!");
    }

    private boolean validatePin(int existPin, int enteredPin) {
        return existPin == enteredPin;
    }

}
