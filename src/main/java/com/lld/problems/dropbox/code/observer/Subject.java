package com.lld.problems.dropbox.code.observer;

import java.util.Set;

import com.lld.problems.dropbox.code.models.User;

public interface Subject {
    public void notifyObservers();

    public void addObserver(User user);

    public void removeObserver(User user);

    public Set<User> getObservers();

    public Set<User> getDirectObservers();
}
