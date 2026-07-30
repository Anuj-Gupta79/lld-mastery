package com.lld.problems.atm.code.service;

import java.security.InvalidParameterException;

import com.lld.problems.atm.code.ATM;
import com.lld.problems.atm.code.constants.Status;
import com.lld.problems.atm.code.models.Receipt;
import com.lld.problems.atm.code.models.Transaction;

public class DispenseService {

    public void dispense(int amount, ATM atm) {
        Transaction transaction = new Transaction();
        atm.setTransaction(transaction);

        boolean isSufficientAmountPresent = atm.getCashInventory().isSufficientAmount(amount);

        if (isSufficientAmountPresent) {
            atm.getCashInventory().deductAmount(amount);
            atm.getTransaction().record(Status.SUCCESS, amount);
            Receipt receipt = new Receipt(amount, atm.getCard());
            atm.setReceipt(receipt);
            System.out.println(receipt.toString());
            atm.eject();
        } else {
            atm.getTransaction().record(Status.FAILURE, amount);
            throw new InvalidParameterException(
                    "Ops! ATM doesn't have sufficient amount.Please enter the amount between less than equal to available amount = "
                            + atm.getCashInventory().getAvailableAmount());
        }
    }

}
