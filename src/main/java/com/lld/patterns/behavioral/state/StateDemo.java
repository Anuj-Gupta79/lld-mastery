package com.lld.patterns.behavioral.state;

// State
interface State {
    // LEARNING: Define all methods in interface set boundary to implement these, it
    // is up to concrete state whether to take action or reject.
    void insertMoney();

    void selectItem();

    void dispense();

    void refund();
}

// Concrete State
class Idle implements State {
    private VendingMachine context;

    public Idle(VendingMachine context) {
        this.context = context;
    }

    @Override
    public void insertMoney() {
        System.out.println("Money has been inserted, Now please select your item.");
        context.setState(new HasMoney(context));
    }

    @Override
    public void selectItem() {
        System.out.println("Ops! you can't select item as there is no money for this.");
    }

    @Override
    public void dispense() {
        System.out.println("Ops! We cannot give you item as there is no money for this.");
    }

    @Override
    public void refund() {
        System.out.println("Not Applicable.");
    }

}

// Concrete State
class HasMoney implements State {

    private VendingMachine context;

    public HasMoney(VendingMachine context) {
        this.context = context;
    }

    @Override
    public void insertMoney() {
        System.out.println("There is no need to insert money, You have already insert it.");
    }

    @Override
    public void selectItem() {
        System.out.println("Kindly select your item.");
        this.context.setState(new Dispensing(context));
    }

    @Override
    public void dispense() {
        System.out.println("Ops! we can't dispense the item, there is no item selected");
    }

    @Override
    public void refund() {
        System.out.println("There is nothing to refund.");
    }
}

// Concrete State
class Dispensing implements State {

    private VendingMachine context;

    public Dispensing(VendingMachine context) {
        this.context = context;
    }

    @Override
    public void insertMoney() {
        System.out.println("No Need to add money as you have already insert and selected the item.");
    }

    @Override
    public void selectItem() {
        System.out.println("No Need to select item as you have already selected the item.");
    }

    @Override
    public void dispense() {
        System.out.println("Please wait! we are dispensing your item.");
        int itemCount = this.context.getItemCount();

        // LEARNING: the state owns the transition decision, not the Context
        if (itemCount == 0) {
            this.context.setState(new OutOfStocks(context));
        } else {
            this.context.decreaseItemByOne();
            this.context.setState(new Idle(context));
        }
    }

    @Override
    public void refund() {
        System.out.println("There is nothing to refund.");
    }
}

// Concrete State
class OutOfStocks implements State {

    private VendingMachine context;

    public OutOfStocks(VendingMachine context) {
        this.context = context;
    }

    @Override
    public void insertMoney() {
        System.out.println("There is no need to add Money as selected item is out of stock");
    }

    @Override
    public void selectItem() {
        System.out.println("Ops! You can't select as selected item is out of stock");
    }

    @Override
    public void dispense() {
        System.out.println("Ops! we can not dispense the selected item as it is out of stock");
    }

    @Override
    public void refund() {
        System.out.println("We are refunding your money as selected item is out of stock, Please select another one");
        this.context.setState(new HasMoney(context));
    }
}

// Context
class VendingMachine {

    private State currState;
    private int itemCount;

    public VendingMachine(int initialItemCount) {
        // LEARNING: context has been intialize with first state, ultimately state need context, so context needs to be created independently
        this.currState = new Idle(this);
        this.itemCount = initialItemCount;
    }

    // LEARNING: only Concrete States should drive transitions. Outside code should
    // not be able to force a state change
    void setState(State state) {
        this.currState = state;
    }

    public void insertMoney() {
        this.currState.insertMoney();
    }

    public void selectItem() {
        this.currState.selectItem();
    }

    public void dispense() {
        this.currState.dispense();
    }

    public void refund() {
        this.currState.refund();
    }

    public int getItemCount() {
        return this.itemCount;
    }

    void decreaseItemByOne() {
        this.itemCount--;
    }
}

public class StateDemo {

    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine(12);

        vendingMachine.insertMoney();
        vendingMachine.dispense();
        vendingMachine.selectItem();
        vendingMachine.dispense();
    }

}
