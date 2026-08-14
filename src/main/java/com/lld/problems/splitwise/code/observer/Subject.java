package com.lld.problems.splitwise.code.observer;

import com.lld.problems.splitwise.code.models.Notification;

public interface Subject {
    public void notifyObservers(Notification notification);
}
