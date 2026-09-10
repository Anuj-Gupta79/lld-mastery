package com.lld.problems.cashdispenser.code.models;

import java.util.Map;

import com.lld.problems.cashdispenser.code.constants.Denomination;

public class DispenseResult {
    private boolean success;
    private int remainder;
    private Map<Denomination, Integer> dispensedNotes;

    public DispenseResult(boolean success, int remainder, Map<Denomination, Integer> dispensedNotes) {
        this.success = success;
        this.remainder = remainder;
        this.dispensedNotes = dispensedNotes;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public int getRemainder() {
        return this.remainder;
    }

    public Map<Denomination, Integer> getDispensedNotes() {
        return this.dispensedNotes;
    }
}
