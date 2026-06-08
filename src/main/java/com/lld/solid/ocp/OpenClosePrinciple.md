# Open Closed Principle (OCP)

## What it is

The Open Closed Principle states:

> Software entities should be open for extension but closed for modification.

This means:

- You should be able to add new behaviour.
- You should not need to modify existing, working code.

In this example, new discount types are added by creating new implementations of `DiscountStrategy`.

The existing calculator remains untouched.

---

## Why it exists

Imagine the calculator was written like this:

```java
if(discountType.equals("SEASONAL")) {
    ...
}
else if(discountType.equals("LOYALTY")) {
    ...
}
else if(discountType.equals("CLEARANCE")) {
    ...
}
```

Now a new discount arrives:

```text
Festival Discount
```

You must modify the calculator.

A month later:

```text
Student Discount
```

Modify again.

A year later:

```text
VIP Discount
```

Modify again.

Every modification introduces risk.

A bug can easily break discounts that were already working.

OCP tries to avoid this by allowing new behaviour through extension rather than modification.

---

## How it helps

- Reduces regression bugs.
- Existing code becomes stable.
- New features are easier to add.
- Promotes use of interfaces and polymorphism.
- Makes systems easier to maintain over time.

The more stable a piece of code becomes, the less frequently it should need modification.

---

## The key ideas in practice

### The abstraction

```java
interface DiscountStrategy
```

This defines the contract:

```java
double apply(double price);
```

The calculator only knows this contract.

It does not care about the actual discount type.

---

### Different behaviours

```java
SeasonalDiscount
```

```java
LoyaltyDiscount
```

```java
ClearanceDiscount
```

```java
NoDiscount
```

Each class provides its own implementation.

This is polymorphism in action.

The same method call:

```java
strategy.apply(price);
```

produces different behaviour depending on the object supplied.

---

### Calculator never changes

```java
class DiscountCalculator
```

```java
public double applyDiscount(
    double price,
    DiscountStrategy strategy
)
```

Notice that the calculator does not contain:

```java
if
else
switch
```

for discount types.

It simply delegates the work to the strategy.

That is what keeps it closed for modification.

---

### Adding a new discount

Suppose tomorrow we need:

```java
StudentDiscount
```

We simply create:

```java
class StudentDiscount implements DiscountStrategy
```

and provide the implementation.

Nothing changes inside:

```java
DiscountCalculator
```

This is the core idea of OCP.

---

### The hidden design pattern

This example uses the Strategy Pattern.

Each discount algorithm is packaged inside its own class.

The calculator receives the strategy and executes it.

Many OCP implementations rely on:

- Strategy Pattern
- Factory Pattern
- Dependency Injection
- Polymorphism

---

## Common interview questions

### What does "Open for Extension" mean?

New functionality can be added without touching existing code.

Usually through inheritance, interfaces, or composition.

---

### What does "Closed for Modification" mean?

Existing tested code should remain unchanged when new features are introduced.

---

### Why are interfaces commonly used for OCP?

Interfaces provide stable contracts.

New implementations can be added without changing existing consumers.

---

### Does OCP eliminate all modifications?

No.

Bug fixes and major redesigns may still require modifications.

OCP mainly targets feature growth.

---

### What is the biggest sign of OCP violation?

Large chains of:

```java
if
else if
else
```

or

```java
switch
```

that keep growing whenever a new type is introduced.

---

### Is inheritance the only way to implement OCP?

No.

Modern applications often prefer:

- Interfaces
- Composition
- Strategy Pattern

over deep inheritance hierarchies.

---

## Notes for revision

- OCP = Open for Extension, Closed for Modification.
- New features should be added without changing existing code.
- Interfaces are a common way to achieve OCP.
- DiscountCalculator depends on `DiscountStrategy`, not concrete discount classes.
- New discounts are added by creating new implementations.
- No changes are required inside DiscountCalculator.
- Polymorphism is the foundation behind this example.
- Long if-else chains are often a sign of OCP violation.
- Strategy Pattern is one of the most common OCP implementations.
- Interview memory trick:

  **"Don't edit working code to add new behaviour. Extend it."**