package com.lld.problems.splitwise.code.observer;

import com.lld.problems.splitwise.code.models.Notification;

public interface Observer {
    public void update(Notification notification);
}
