package com.lld.problems.bookmyshow.code.observers;

import com.lld.problems.bookmyshow.code.models.User;

public interface Subject {
    public void notifyObservers(String message);

    public void addObserver(User user);

    public void removeObserver(String userId);
}
