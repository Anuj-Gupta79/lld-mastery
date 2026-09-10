package com.lld.problems.cashdispenser.code.core;

import java.util.Map;

import com.lld.problems.cashdispenser.code.constants.Denomination;
import com.lld.problems.cashdispenser.code.exceptions.InsufficientDenominationException;
import com.lld.problems.cashdispenser.code.models.DenominationHandler;
import com.lld.problems.cashdispenser.code.models.DispenseResult;

public class CashDispenser {
    private DenominationHandler headHandler;

    public CashDispenser(DenominationHandler handler) {
        this.headHandler = handler;
    }

    public Map<Denomination, Integer> withdraw(int amount) {
        DispenseResult result = headHandler.dispense(amount);

        if (result.getSuccess()) {
            return result.getDispensedNotes();
        }

        throw new InsufficientDenominationException(
                "[Error With Draw] There is not enough denomination to process the complete amount!");
    }

    public DenominationHandler getHeadHandler() {
        return this.headHandler;
    }
}
