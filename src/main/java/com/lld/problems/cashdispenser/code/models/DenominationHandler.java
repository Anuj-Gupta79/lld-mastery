package com.lld.problems.cashdispenser.code.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.lld.problems.cashdispenser.code.constants.Denomination;

public class DenominationHandler {
    private Denomination denomination;
    private int stock;
    private DenominationHandler next;

    public DenominationHandler(Denomination denomination, int stock) {
        this.denomination = denomination;
        this.stock = stock;
    }

    public DispenseResult dispense(int remainder) {
        int notesToGive = Math.min(remainder / denomination.getValue(), this.stock);
        stock -= notesToGive;
        int newRemainder = remainder - notesToGive * this.denomination.getValue();

        Map<Denomination, Integer> ownNotes = new HashMap<>();

        if (notesToGive != 0) {
            ownNotes.put(this.denomination, notesToGive);
        }

        if (newRemainder == 0) {
            return new DispenseResult(true, 0, ownNotes);
        }

        if (Objects.isNull(next)) {
            this.stock += notesToGive;
            return new DispenseResult(false, newRemainder, null);
        }

        DispenseResult childDispenseResult = this.next.dispense(newRemainder);

        if (childDispenseResult.getSuccess()) {
            ownNotes.putAll(childDispenseResult.getDispensedNotes());
            return new DispenseResult(true, 0, ownNotes);
        } else {
            this.stock += notesToGive;
            return new DispenseResult(false, childDispenseResult.getRemainder(), null);
        }
    }

    public void setNext(DenominationHandler next) {
        this.next = next;
    }

    public Denomination getDenomination() {
        return this.denomination;
    }

    public int getStock() {
        return this.stock;
    }

    public DenominationHandler getNext() {
        return this.next;
    }
}
