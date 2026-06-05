package com.lld.oops.Encapsulation;

// LEARNING: Encapsulation = private fields + controlled public methods. Outside code never touches state directly.
class BankAccount {

    private String accountHolder;
    private double balance;

    public BankAccount(String accountHolder) {
        this.accountHolder = accountHolder;
        this.balance = 0.0;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    // LEARNING: Validation lives inside the method — caller can't deposit negative
    // or withdraw beyond balance.
    public void deposit(double amount) {
        if (amount > 0.0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount < 0.0) {
            System.out.println("Invalid withdrawal amount.");
        } else if (amount > balance) {
            System.out.println("Insufficient funds.");
        } else {
            balance -= amount;
            System.out.println("Withdrew: " + amount);
        }
    }

    // LEARNING: Getter can derive computed state — status is behaviour, not a
    // stored field.
    public String getAccountStatus() {
        if (balance >= 15000.0)
            return "Platinum";
        else if (balance >= 5000.0)
            return "Gold";
        else
            return "Silver";
    }
}

public class EncapsulationDemo {

    public static void main(String[] args) {
        BankAccount account = new BankAccount("John Doe");
        System.out.println("Account Holder: " + account.getAccountHolder());
        System.out.println("Initial Balance: " + account.getBalance());
        System.out.println("Account Status: " + account.getAccountStatus());

        account.deposit(5000);
        System.out.println("Balance after deposit: " + account.getBalance());
        System.out.println("Account Status: " + account.getAccountStatus());

        account.withdraw(200);
        System.out.println("Balance after withdrawal: " + account.getBalance());
        System.out.println("Account Status: " + account.getAccountStatus());

        account.withdraw(15000);
    }
}