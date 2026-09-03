package com.lld.problems.dropbox.code.observer;

import com.lld.problems.dropbox.code.Events.ChangeEvent;

public interface Observer {
    public void update(ChangeEvent event);
}
