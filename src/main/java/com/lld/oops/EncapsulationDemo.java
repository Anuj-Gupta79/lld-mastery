package com.lld.oops;

// LEARNING : Encapsulation is a fundamental principle of object-oriented programming that promotes data hiding and abstraction. 
// It allows us to restrict direct access to an object's internal state and only expose a controlled interface through public methods (getters and setters). 
// This helps to maintain the integrity of the data and prevents unauthorized modifications.
class BankAccount {

    // LEARNING : The BankAccount class encapsulates the details of a bank account,
    // including the account holder's name and balance.
    private String accountHolder;
    private double balance;

    // LEARNING : Constructor to initialize the account holder's name and initial
    // balance when a BankAccount object is created.
    public BankAccount(String accountHolder) {
        this.accountHolder = accountHolder;
        this.balance = 0.0;
    }

    // LEARNING : Getter methods to provide controlled access to the private
    // attributes of the BankAccount class.
    // AccountHolder and Balance has only getter methods to prevent direct
    // modification from outside the class, ensuring encapsulation.
    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    // LEARNING : Public methods to perform operations on the bank account, such as
    // depositing and withdrawing money.
    // These methods include validation to ensure that the operations are performed
    // correctly and that the integrity of the account balance is maintained.
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

    // Learning: Getters can also include logic to provide additional information
    // based on the internal state of the object.
    public String getAccountStatus() {
        if (balance >= 15000.0) {
            return "Platinum";
        } else if (balance >= 5000.0) {
            return "Gold";
        } else {
            return "Silver";
        }
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

        account.withdraw(15000); // Attempt to withdraw more than the balance
    }
}
