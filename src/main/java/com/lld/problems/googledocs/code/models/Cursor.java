package com.lld.problems.googledocs.code.models;

public class Cursor {
    private int position;
    private final String userId;

    public Cursor(int position, String userId) {
        this.position = position;
        this.userId = userId;
    }

    public int getPosition() {
        return this.position;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setPosition(int position){
        this.position =  position;
    }
}
