package com.lld.problems.bookmyshow.code.observers;

import com.lld.problems.bookmyshow.code.models.Notification;

public interface Observer {
    public void update(Notification notification);
}
