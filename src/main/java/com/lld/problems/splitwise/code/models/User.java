package com.lld.problems.splitwise.code.models;

import com.lld.problems.splitwise.code.observer.Observer;

public class User implements Observer {
    private String userId;
    private String name;
    private String email;
    private String phone;

    public User(String id, String name, String email, String phone) {
        this.userId = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getNumber() {
        return this.email;
    }

    @Override
    public void update(Notification notification) {
        System.out.println("User: " + getName() + " => " + notification.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User other = (User) obj;

        return this.userId.equals(other.getUserId());
    }

    @Override
    public int hashCode() {
        return this.userId.hashCode();
    }

}
