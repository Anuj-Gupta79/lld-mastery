package com.lld.problems.discountcouponengine.code.models;

import java.util.UUID;

public class Item {
    private String itemId;
    private String name;
    private double price;

    public Item(String name, double price) {
        this.itemId = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
    }

    public String getItemId() {
        return this.itemId;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }
}
