package com.lld.problems.cashdispenser.code.constants;

public enum Denomination {
    TWO_THOUSAND(2000),
    FIVE_HUNDRED(500),
    ONE_HUNDRED(100),
    FIFTY(50),
    TWENTY(20),
    TEN(10),
    FIVE(5),
    TWO(2),
    ONE(1);

    private int value;

    Denomination(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
