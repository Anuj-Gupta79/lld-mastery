package com.lld.problems.splitwise.code;

import java.util.List;
import java.util.Map;

import com.lld.problems.splitwise.code.constants.PaymentType;
import com.lld.problems.splitwise.code.core.Group;
import com.lld.problems.splitwise.code.core.Splitwise;
import com.lld.problems.splitwise.code.exceptions.InvalidParticipantException;
import com.lld.problems.splitwise.code.exceptions.InvalidSplitException;
import com.lld.problems.splitwise.code.exceptions.UnSettledAmountException;
import com.lld.problems.splitwise.code.models.SuggestedSettlement;
import com.lld.problems.splitwise.code.models.User;
import com.lld.problems.splitwise.code.strategies.EqualExpenseStrategy;
import com.lld.problems.splitwise.code.strategies.ExactExpenseStrategy;
import com.lld.problems.splitwise.code.strategies.PercentageExpenseStrategy;

public class Main {
    public static void main(String[] args) {
        Splitwise splitwise = new Splitwise();

        User john = new User("1", "John", "John@email.com", "9090908989");
        User haram = new User("2", "Haram", "Haram@gamail.com", "12234563211");
        User lee = new User("3", "Lee", "Lee@gmail.com", "7637645289");

        splitwise.registerUser(john);
        splitwise.registerUser(haram);
        splitwise.registerUser(lee);

        Group group = splitwise.createGroup("1", "Homies");

        group.addMember(john);
        group.addMember(haram);
        group.addMember(lee);

        System.out.println("\n--- Expense 1: Haram buys pork, equal split ---");
        EqualExpenseStrategy equalStrategy = new EqualExpenseStrategy();
        group.addExpense("2", "2", 200.00, "Pork meat", List.of("1", "2", "3"), equalStrategy);

        System.out.println("\n--- Expense 2: Lee buys medicine, exact split ---");
        ExactExpenseStrategy exactStrategy = new ExactExpenseStrategy(Map.of(
                "1", 20.00,
                "3", 50.00));
        group.addExpense("3", "3", 70.00, "Medicine", List.of("1", "3"), exactStrategy);

        System.out.println("\n--- Expense 3: John buys milk, percentage split ---");
        PercentageExpenseStrategy percentageStrategy = new PercentageExpenseStrategy(Map.of(
                "1", 60.0,
                "2", 20.0,
                "3", 20.0));
        group.addExpense("1", "1", 120.00, "Milk", List.of("1", "2", "3"), percentageStrategy);

        System.out.println("\n--- Settlement: Haram pays John back ---");
        double owed = group.getExpenseLedgerService().getBalance("1", "2");
        System.out.println("Haram owes John: " + owed);
        group.recordSettlement("1", "1", "2", owed, PaymentType.UPI);

        System.out.println("\n--- Debt Simplification ---");
        List<SuggestedSettlement> suggestions = group.simplifyDebt();
        for (SuggestedSettlement s : suggestions) {
            System.out.println(s.getPayerId() + " should pay " + s.getPayeeId() + ": " + s.getAmount());
        }

        System.out.println("\n--- Failure case 1: removeMember with outstanding balance ---");
        try {
            group.removeMember(lee);
        } catch (UnSettledAmountException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n--- Failure case 2: addExpense with non-member participant ---");
        try {
            EqualExpenseStrategy badStrategy = new EqualExpenseStrategy();
            group.addExpense("1", "1", 50.00, "Snacks", List.of("1", "99"), badStrategy);
        } catch (InvalidParticipantException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n--- Failure case 3: percentage split not summing to 100 ---");
        try {
            PercentageExpenseStrategy badPercentage = new PercentageExpenseStrategy(Map.of(
                    "1", 50.0,
                    "2", 30.0));
            group.addExpense("1", "1", 100.00, "Bad split", List.of("1", "2"), badPercentage);
        } catch (InvalidSplitException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
    }
}