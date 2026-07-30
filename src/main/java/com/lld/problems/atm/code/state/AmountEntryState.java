package com.lld.problems.atm.code.state;

import com.lld.problems.atm.code.ATM;
import com.lld.problems.atm.code.models.Card;
import com.lld.problems.atm.code.service.DispenseService;

public class AmountEntryState implements ATMState {

    @Override
    public void insert(Card card, ATM atm) {
        throw new UnsupportedOperationException("Unsupported operation as Card is already inserted!");
    }

    @Override
    public void enterPin(int pin, ATM atm) {
        throw new UnsupportedOperationException("Unsupported operation as User is already validated");
    }

    @Override
    public void enterAmount(int amount, ATM atm) {
        DispenseService dispenseService = new DispenseService();
        dispenseService.dispense(amount, atm);
    }
}
