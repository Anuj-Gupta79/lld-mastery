package com.lld.problems.cashdispenser.code;

import java.util.Map;

import com.lld.problems.cashdispenser.code.constants.Denomination;
import com.lld.problems.cashdispenser.code.core.CashDispenser;
import com.lld.problems.cashdispenser.code.exceptions.InsufficientDenominationException;
import com.lld.problems.cashdispenser.code.models.DenominationHandler;

public class Main {
    public static void main(String[] args) {
        // Build handlers: denomination + stock
        DenominationHandler h2000 = new DenominationHandler(Denomination.TWO_THOUSAND, 5);
        DenominationHandler h500 = new DenominationHandler(Denomination.FIVE_HUNDRED, 5);
        DenominationHandler h100 = new DenominationHandler(Denomination.ONE_HUNDRED, 5);
        DenominationHandler h50 = new DenominationHandler(Denomination.FIFTY, 5);
        DenominationHandler h20 = new DenominationHandler(Denomination.TWENTY, 5);
        DenominationHandler h10 = new DenominationHandler(Denomination.TEN, 5);
        DenominationHandler h5 = new DenominationHandler(Denomination.FIVE, 5);
        DenominationHandler h2 = new DenominationHandler(Denomination.TWO, 1); // deliberately low stock
        DenominationHandler h1 = new DenominationHandler(Denomination.ONE, 1);

        // Wire in forward order
        h2000.setNext(h500);
        h500.setNext(h100);
        h100.setNext(h50);
        h50.setNext(h20);
        h20.setNext(h10);
        h10.setNext(h5);
        h5.setNext(h2);
        h2.setNext(h1);

        CashDispenser dispenser = new CashDispenser(h2000);

        // Scenario 1: expected SUCCESS
        int successAmount = 2680;
        try {
            Map<Denomination, Integer> result = dispenser.withdraw(successAmount);
            System.out.println("Withdraw " + successAmount + " succeeded: " + result);
        } catch (InsufficientDenominationException e) {
            System.out.println("Withdraw " + successAmount + " FAILED unexpectedly: " + e.getMessage());
        }

        // Scenario 2: expected FAILURE (TWO has only 1 in stock, forcing a remainder
        // that can't resolve)
        int failAmount = 4;
        try {
            Map<Denomination, Integer> result = dispenser.withdraw(failAmount);
            System.out.println("Withdraw " + failAmount + " succeeded unexpectedly: " + result);
        } catch (InsufficientDenominationException e) {
            System.out.println("Withdraw " + failAmount + " failed as expected: " + e.getMessage());
        }
    }
}