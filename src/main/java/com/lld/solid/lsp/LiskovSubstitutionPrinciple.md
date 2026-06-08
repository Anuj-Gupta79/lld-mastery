# Liskov Substitution Principle (LSP)

## What it is

The Liskov Substitution Principle states:

> Objects of a subclass should be replaceable with objects of the parent class without breaking program correctness.

In simpler words:

> If `B` is a subtype of `A`, then anywhere `A` is used, `B` should work without surprises.

The caller should not need to know whether it received the parent object or one of its subclasses.

If behaviour changes unexpectedly after substitution, the inheritance hierarchy is wrong.

---

## Why it exists

Inheritance represents an **"is-a" relationship**.

When a class extends another class, it is making a promise:

> "I can behave like my parent."

If that promise is broken, client code starts failing in subtle ways.

The famous example is:

```text
Square extends Rectangle
```

At first glance this looks correct because mathematically a square is a rectangle.

But in code:

```java
rectangle.setWidth(10);
rectangle.setHeight(5);
```

a rectangle expects width and height to be independent.

A square cannot satisfy that expectation.

As a result, the subclass breaks the contract of its parent.

---

## How it helps

- Makes inheritance safe.
- Prevents unexpected runtime behaviour.
- Improves maintainability.
- Reduces defensive coding.
- Makes polymorphism reliable.
- Leads to better object modeling.

Without LSP, inheritance becomes a source of bugs instead of reuse.

---

## The key ideas in practice

### The violation

```java
class SquareViolation extends RectangleViolation
```

The parent exposes:

```java
setWidth()
setHeight()
```

The caller assumes:

```java
width and height can change independently
```

For example:

```java
rectangle.setWidth(10);
rectangle.setHeight(5);
```

Expected area:

```text
10 * 5 = 50
```

---

### What actually happens

Inside `SquareViolation`:

```java
setWidth(10)
```

sets:

```java
side = 10
```

Then:

```java
setHeight(5)
```

sets:

```java
side = 5
```

Final area:

```text
5 * 5 = 25
```

The caller expected:

```text
50
```

but received:

```text
25
```

The subclass changed the behaviour expected by the parent.

This is an LSP violation.

---

### Why the inheritance is wrong

The problem is not in the implementation.

The problem is the model.

The code says:

```java
Square is a Rectangle
```

but the behaviour says otherwise.

A rectangle allows:

```text
width != height
```

A square requires:

```text
width == height
```

These constraints are different.

The subclass cannot honour the parent's contract.

---

### The fix

Instead of:

```java
Square extends Rectangle
```

use:

```java
Shape
├── Rectangle
└── Square
```

Both shapes share:

```java
area()
```

but neither pretends to be the other.

This is a much more accurate abstraction.

---

### The hidden lesson

LSP violations are often a sign that:

```text
Inheritance was chosen where a common abstraction was needed.
```

Whenever a subclass must:

- Ignore parent behaviour
- Override methods drastically
- Throw UnsupportedOperationException
- Change method expectations

you should question the inheritance hierarchy.

---

## Common interview questions

### What is LSP in one sentence?

A subclass should be usable anywhere its parent is expected without changing program correctness.

---

### How is LSP related to inheritance?

Inheritance is only valid when the child preserves the behaviour promised by the parent.

If the behaviour changes, inheritance becomes incorrect.

---

### Why does Square-Rectangle violate LSP?

Because Rectangle allows width and height to vary independently.

Square cannot honour that contract.

Replacing Rectangle with Square changes program behaviour.

---

### Is every inheritance hierarchy automatically LSP compliant?

No.

The compiler only checks syntax.

LSP is a behavioural rule, not a language rule.

Code can compile perfectly and still violate LSP.

---

### What are common signs of LSP violation?

- Overridden methods changing expected behaviour.
- UnsupportedOperationException in subclasses.
- Empty method implementations.
- Excessive type checking using instanceof.
- Subclasses requiring special handling.

---

### Does LSP apply only to classes?

No.

It applies to:

- Interfaces
- Abstract classes
- Class hierarchies
- API contracts

Any abstraction that promises behaviour.

---

## Notes for revision

- LSP = A child should be usable anywhere its parent is expected.
- Think behaviour, not inheritance syntax.
- If replacing parent with child breaks code, LSP is violated.
- Square-Rectangle is the classic LSP interview example.
- A subclass must preserve the parent's contract.
- LSP violations usually indicate poor object modelling.
- Watch for overridden methods that drastically change behaviour.
- Watch for UnsupportedOperationException in subclasses.
- Prefer common abstractions over forced inheritance.
- Interview memory trick:

  **"If the caller can tell it's a subclass, LSP is probably broken."**