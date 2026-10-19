package com.lld.problems.discountcouponengine.code.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.lld.problems.discountcouponengine.code.constants.DiscountType;
import com.lld.problems.discountcouponengine.code.exceptions.CouponAlreadyExistsException;
import com.lld.problems.discountcouponengine.code.exceptions.CouponNotEligibleException;
import com.lld.problems.discountcouponengine.code.exceptions.CouponNotFoundException;
import com.lld.problems.discountcouponengine.code.handlers.DiscountHandler;
import com.lld.problems.discountcouponengine.code.models.Cart;
import com.lld.problems.discountcouponengine.code.models.Coupon;
import com.lld.problems.discountcouponengine.code.models.Result;
import com.lld.problems.discountcouponengine.code.strategy.DiscountStrategy;
import com.lld.problems.discountcouponengine.code.strategy.FlatDiscountStrategy;
import com.lld.problems.discountcouponengine.code.strategy.PercentageDiscountStrategy;
import com.lld.problems.discountcouponengine.code.strategy.PercentageWithCapDiscountStrategy;

public class DiscountManager {
    private static volatile DiscountManager instance;
    private ConcurrentHashMap<String, Coupon> coupons;
    private Map<DiscountType, DiscountStrategy> strategies;

    private DiscountManager() {
        this.coupons = new ConcurrentHashMap<>();
        this.strategies = Map.of(
                DiscountType.FLAT, new FlatDiscountStrategy(),
                DiscountType.PERCENTAGE, new PercentageDiscountStrategy(),
                DiscountType.PERCENTAGE_WITH_CAP, new PercentageWithCapDiscountStrategy());
    }

    public void registerCoupon(Coupon coupon) {
        if (this.coupons.putIfAbsent(coupon.getCouponId(), coupon) != null) {
            throw new CouponAlreadyExistsException("[Error Register Coupon] Requested Coupon already exists.");
        }
    }

    public List<Coupon> getEligibleCoupons(Cart cart) {
        List<Coupon> eligibleCoupons = new ArrayList<>();
        double totalPrice = cart.getOriginalTotal();

        for (Coupon coupon : this.coupons.values()) {
            if (isEligible(cart.getCoupons(), coupon, totalPrice)) {
                eligibleCoupons.add(coupon);
            }
        }

        return Collections.unmodifiableList(eligibleCoupons);
    }

    public void applyCoupon(Cart cart, Coupon coupon) {
        if (!this.coupons.containsKey(coupon.getCouponId())) {
            throw new CouponNotFoundException("[Error Apply Coupon] Coupon is not found");
        }

        if (isEligible(cart.getCoupons(), coupon, cart.getOriginalTotal())) {
            cart.addCoupon(coupon);
            return;
        }

        throw new CouponNotEligibleException("[Error Apply Coupon] Applied Coupon is not eligible!");

    }

    public void removeCoupon(Cart cart, Coupon coupon) {
        cart.removeCoupon(coupon);
    }

    public Result calculate(Cart cart) {
        double totalPrice = cart.getOriginalTotal();
        List<Coupon> selectedCoupons = new ArrayList<>(cart.getCoupons());
        Result result = new Result(totalPrice);

        if (selectedCoupons.isEmpty()) {
            return result;
        }

        DiscountHandler head = null;
        for (int ind = selectedCoupons.size() - 1; ind >= 0; ind--) {
            Coupon coupon = selectedCoupons.get(ind);
            DiscountHandler handler = new DiscountHandler(coupon, this.strategies.get(coupon.getDiscountType()));
            handler.setNext(head);
            head = handler;
        }

        head.handle(totalPrice, result);

        return result;
    }

    public static DiscountManager getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (DiscountManager.class) {
                if (Objects.isNull(instance)) {
                    instance = new DiscountManager();
                }
            }
        }

        return instance;
    }

    private boolean isEligible(List<Coupon> selectedCoupons, Coupon coupon, double totalPrice) {
        return !selectedCoupons.contains(coupon) && coupon.getMinCartValue() <= totalPrice;
    }
}
