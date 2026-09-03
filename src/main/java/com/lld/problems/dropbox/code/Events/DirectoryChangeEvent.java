package com.lld.problems.dropbox.code.Events;

public class DirectoryChangeEvent extends ChangeEvent {

    public DirectoryChangeEvent(String name) {
        super(name);
    }

    public String toString() {
        return "There is an updated in Directory Name = " + getComponentName();
    }
}
