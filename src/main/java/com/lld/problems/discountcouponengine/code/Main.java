package com.lld.problems.discountcouponengine.code;

import java.util.List;
import java.util.stream.Collectors;

import com.lld.problems.discountcouponengine.code.constants.DiscountType;
import com.lld.problems.discountcouponengine.code.core.DiscountManager;
import com.lld.problems.discountcouponengine.code.models.Cart;
import com.lld.problems.discountcouponengine.code.models.Coupon;
import com.lld.problems.discountcouponengine.code.models.Item;
import com.lld.problems.discountcouponengine.code.models.Result;

public class Main {
    public static void main(String[] args) {
        DiscountManager manager = DiscountManager.getInstance();

        // Scenario 1: eligibility (run first, the registry is shared and grows)
        System.out.println("=== Scenario 1: eligibility ===");
        Cart cart1 = cartOf(200, 100);
        Coupon flat50 = new Coupon("FLAT50", DiscountType.FLAT, 50, null, 0);
        Coupon save20 = new Coupon("SAVE20", DiscountType.PERCENTAGE, 20, null, 200);
        Coupon big100 = new Coupon("BIG100", DiscountType.FLAT, 100, null, 500);
        Coupon exact300 = new Coupon("EXACT300", DiscountType.FLAT, 10, null, 300);
        for (Coupon c : List.of(flat50, save20, big100, exact300)) {
            manager.registerCoupon(c);
        }
        System.out.println("Eligible for 300 cart: " + names(manager.getEligibleCoupons(cart1)));
        tryApply(manager, cart1, flat50);
        System.out.println("Eligible after applying FLAT50: " + names(manager.getEligibleCoupons(cart1)));
        tryApply(manager, cart1, big100);
        tryApply(manager, cart1, flat50);

        // Scenario 2: 150 cart, flat 100 then 10%
        System.out.println("\n=== Scenario 2: flat 100 -> 10% on 150 ===");
        Cart cart2 = cartOf(100, 50);
        Coupon flat100 = register(manager, new Coupon("FLAT100", DiscountType.FLAT, 100, null, 0));
        Coupon pct10 = register(manager, new Coupon("PCT10", DiscountType.PERCENTAGE, 10, null, 0));
        manager.applyCoupon(cart2, flat100);
        manager.applyCoupon(cart2, pct10);
        printResult(manager.calculate(cart2));

        // Scenario 3: same cart, reverse order
        System.out.println("\n=== Scenario 3: 10% -> flat 100 on 150 ===");
        Cart cart3 = cartOf(100, 50);
        manager.applyCoupon(cart3, pct10);
        manager.applyCoupon(cart3, flat100);
        printResult(manager.calculate(cart3));

        // Scenario 4: skip behaviour
        System.out.println("\n=== Scenario 4: 10% -> flat 100 on 110 ===");
        Cart cart4 = cartOf(110);
        manager.applyCoupon(cart4, pct10);
        manager.applyCoupon(cart4, flat100);
        printResult(manager.calculate(cart4));

        // Scenario 5: capped percentage
        System.out.println("\n=== Scenario 5: 20% capped at 30 on 400 ===");
        Cart cart5 = cartOf(250, 150);
        Coupon capped = register(manager, new Coupon("CAP20", DiscountType.PERCENTAGE_WITH_CAP, 20, 30.0, 0));
        manager.applyCoupon(cart5, capped);
        printResult(manager.calculate(cart5));

        // Scenario 6: no coupons
        System.out.println("\n=== Scenario 6: no coupons on 250 ===");
        printResult(manager.calculate(cartOf(250)));

        // Scenario 7: remove a coupon and recompute
        System.out.println("\n=== Scenario 7: remove and recompute on 150 ===");
        Cart cart7 = cartOf(150);
        manager.applyCoupon(cart7, flat100);
        manager.applyCoupon(cart7, pct10);
        printResult(manager.calculate(cart7));
        manager.removeCoupon(cart7, flat100);
        printResult(manager.calculate(cart7));
    }

    private static Cart cartOf(double... prices) {
        Cart cart = new Cart();
        int n = 1;
        for (double price : prices) {
            cart.addItem(new Item("Item" + n++, price));
        }
        return cart;
    }

    private static Coupon register(DiscountManager manager, Coupon coupon) {
        manager.registerCoupon(coupon);
        return coupon;
    }

    private static void tryApply(DiscountManager manager, Cart cart, Coupon coupon) {
        try {
            manager.applyCoupon(cart, coupon);
            System.out.println("Applied " + coupon.getName());
        } catch (RuntimeException e) {
            System.out.println("Rejected " + coupon.getName() + " -> " + e.getClass().getSimpleName());
        }
    }

    private static String names(List<Coupon> coupons) {
        return coupons.stream().map(Coupon::getName).sorted().collect(Collectors.toList()).toString();
    }

    private static void printResult(Result r) {
        System.out.println("Original: " + r.getOriginalAmount());
        System.out.println("Final:    " + r.getFinalAmount());
        System.out.println("Saved:    " + r.getSavedAmount());
        System.out.println("Applied:  " + names(r.getAppliedCoupons()));
        System.out.println("Skipped:  " + names(r.getSkippedCoupons()));
    }
}