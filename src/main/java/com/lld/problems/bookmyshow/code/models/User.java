package com.lld.problems.bookmyshow.code.models;

import com.lld.problems.bookmyshow.code.observers.Observer;

public class User implements Observer {
    private String userId;
    private String name;
    private String email;
    private boolean active;

    public User(String id, String name, String email) {
        this.userId = id;
        this.name = name;
        this.email = email;
        this.active = true;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isActive() {
        return this.active;
    }

    public void deactivate() {
        this.active = false;
    }

    @Override
    public void update(Notification notification) {
        System.out.println(notification.toString());
    }

    public String toString() {
        return "{ name: " + this.name + ",\n" +
                "email: " + this.email + "\n" +
                "}";
    }
}
