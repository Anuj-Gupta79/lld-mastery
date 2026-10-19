package com.lld.problems.discountcouponengine.code.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lld.problems.discountcouponengine.code.exceptions.CouponAlreadyExistsException;
import com.lld.problems.discountcouponengine.code.exceptions.CouponNotFoundException;
import com.lld.problems.discountcouponengine.code.exceptions.ItemAlreadyExistException;
import com.lld.problems.discountcouponengine.code.exceptions.ItemNotFoundException;

public class Cart {
    private Map<String, Item> items;
    private List<Coupon> appliedCoupons;

    public Cart() {
        this.items = new HashMap<>();
        this.appliedCoupons = new ArrayList<>();
    }

    public void addItem(Item item) {
        if (this.items.containsKey(item.getItemId())) {
            throw new ItemAlreadyExistException("[Error Add Item] Item is already exist.");
        }

        this.items.put(item.getItemId(), item);
    }

    public void removeItem(String itemId) {
        if (this.items.containsKey(itemId)) {
            this.items.remove(itemId);
            return;
        }

        throw new ItemNotFoundException("[Error Remove Item] Item is not exist with ItemId " + itemId);
    }

    public void addCoupon(Coupon coupon) {
        if (this.appliedCoupons.contains(coupon)) {
            throw new CouponAlreadyExistsException("[Error Add Coupon] Coupon is already applied.");
        }

        this.appliedCoupons.add(coupon);
    }

    public void removeCoupon(Coupon coupon) {
        if (this.appliedCoupons.contains(coupon)) {
            this.appliedCoupons.remove(coupon);
            return;
        }

        throw new CouponNotFoundException("[Error Remove Coupon] Coupon is not found");
    }

    public double getOriginalTotal() {
        double amount = 0.0;

        for (Item item : items.values()) {
            amount += item.getPrice();
        }

        return amount;
    }

    public List<Coupon> getCoupons() {
        return Collections.unmodifiableList(this.appliedCoupons);
    }

    public Map<String, Item> getItems() {
        return Collections.unmodifiableMap(this.items);
    }
}
