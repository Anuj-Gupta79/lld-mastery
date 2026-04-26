package com.lld.solid.ocp;

public class DiscountCalculator {

    // LEARNING : This implementation violates the Open/Closed Principle because
    // every time we want to add a new discount type, we need to modify the
    // applyDiscount method. This can lead to bugs and maintenance issues as the
    // codebase grows.
    // We have commented out the old implementation and will replace it with a new
    // one that adheres to the Open/Closed Principle.
    // public double applyDiscount(double price, String discountType) {

    // if (discountType.equals("seasonal")) {
    // return price - (price * 0.2);
    // } else if (discountType.equals("loyalty")) {
    // return price - (price * 0.1);
    // } else if (discountType.equals("clearance")) {
    // return price - (price * 0.5);
    // }
    // return price;
    // }

    // WHY: By using the Strategy pattern, we can add new discount types without
    // modifying
    // the existing code. Each discount type is encapsulated in its own class that
    // implements the DiscountStrategy interface. This way, we can easily extend the
    // functionality by adding new classes without changing the DiscountCalculator
    // class, thus adhering to the Open/Closed Principle.
    public double applyDiscount(double price, DiscountStrategy discountStrategy) {
        return discountStrategy.apply(price);
    }
}
