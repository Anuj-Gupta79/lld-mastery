package com.lld.problems.dropbox.code.Events;

public abstract class ChangeEvent {
    private final String ComponentName;

    public ChangeEvent(String name) {
        this.ComponentName = name;
    }

    public String getComponentName() {
        return this.ComponentName;
    }
}
