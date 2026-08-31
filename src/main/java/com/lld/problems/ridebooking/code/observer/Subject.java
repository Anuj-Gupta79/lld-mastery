package com.lld.problems.ridebooking.code.observer;

public interface Subject {

    public void notifyObservers();

    public void addObserver(Observer observer);

    public void removeObserver(Observer observer);
}
