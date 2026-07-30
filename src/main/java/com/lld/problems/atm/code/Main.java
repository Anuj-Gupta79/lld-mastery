package com.lld.problems.atm.code;

import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.Scanner;

import com.lld.problems.atm.code.models.Card;
import com.lld.problems.atm.code.models.CashInventory;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CashInventory cashInventory = new CashInventory(10000);
        ATM atm = new ATM(cashInventory);

        Card card = new Card(9827364, "Alex", 1234, 90872364);

        atm.insert(card);

        while (true) {
            System.out.print("Enter Pin: ");
            int pin = scanner.nextInt();

            try {
                atm.enterPin(pin);
                break;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        while (!Objects.isNull(atm.getCard())) {
            System.out.print("Enter Amount: ");
            int amount = scanner.nextInt();

            try {
                atm.enterAmount(amount);
                break;
            } catch (InvalidParameterException e) {
                System.out.println("Error: " + e.getMessage());

                System.out.print("Do you want to eject the card? (Y/N): ");
                String userWantsToEject = scanner.next();

                if (userWantsToEject.equals("Y")) {
                    atm.eject();
                }
            }
        }

        if (!Objects.isNull(atm.getCard())) {
            atm.eject();
        }

        scanner.close();
    }
}
