package com.lld.patterns.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

// LEARNING: observable provide contract to subject that it have to add and remove observer
interface Observable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);
}

interface Observer {
    void update(String stockName, double price);
}

// Subject
class Stock implements Observable {
    private String stockName;
    private double price;
    // LEARNING: Subject must have the reference of the observers.
    private List<Observer> observers;

    public Stock(String stockName, double price) {
        this.stockName = stockName;
        this.price = price;
        this.observers = new ArrayList<Observer>();
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    // LEARNING: If there is any update subject will notify to all observer.
    public void updatePrice(double price) {
        this.price = price;
        notifyObservers();
    }

    // LEARNING: Keeping notifying private as it should not be accessible any other object, only subject is responsible for notifying.
    private void notifyObservers() {
        for (Observer observer : observers) {
            // LEARNING: Wrapping up process into try catch, just to avoid breaking during any failure while receiving notification.
            try {
                observer.update(stockName, price);
            } catch (Exception e) {
                System.err.println("There is a issue while receiving update to observer due to " + e.getMessage());
            }
        }
    }

}

// Observer
class EmailAlertObserver implements Observer {

    @Override
    public void update(String stockName, double price) {
        System.out.println("Email: " + stockName + " hit " + price);
    }

}

// Observer
class MobileAlertObserver implements Observer {
    @Override
    public void update(String stockName, double price) {
        System.out.println("Mobile: " + stockName + " hit " + price);
    }
}

public class ObserverDemo {
    public static void main(String[] args) {
        Stock stock = new Stock("Nippon", 325);
        Observer emailObserver = new EmailAlertObserver();
        Observer mobileObserver = new MobileAlertObserver();
        stock.addObserver(emailObserver);
        stock.addObserver(mobileObserver);

        stock.updatePrice(245);

        // LEARNING: After removing mobileObserver only EmailObserver get notify for further update
        stock.removeObserver(mobileObserver);

        stock.updatePrice(290);

    }
}
