package com.lld.problems.dropbox.code.models;

import com.lld.problems.dropbox.code.Events.ChangeEvent;
import com.lld.problems.dropbox.code.observer.Observer;

public class User implements Observer {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void update(ChangeEvent event) {
        System.out.println("User: " + this.name + " Notification: " + event.toString());
    }
}
