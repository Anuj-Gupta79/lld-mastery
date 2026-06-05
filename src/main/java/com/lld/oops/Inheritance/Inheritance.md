# OOP — Inheritance

## Intent
Allow a subclass to acquire fields and methods of a superclass, then extend or override them.
Models IS-A relationships — Car IS-A Vehicle, Bike IS-A Vehicle.

## Core Concepts

### extends
Subclass inherits all non-private fields and methods of the superclass.
Private fields are inherited but not directly accessible — go through getters.

### super()
Must be first line in subclass constructor.
Delegates common field initialization to the parent — subclass only sets what's unique to it.
`Car` hardcodes `4` wheels via `super()` — caller never needs to pass it.

### @Override
Subclass provides its own implementation of a parent method.
`Car.move()` calls `super.move()` first, then adds Car-specific output — reuse + extend.
`Bike.move()` replaces parent behaviour entirely — no super call needed.

### Polymorphism via inheritance
`Vehicle v = new Car(...)` is valid — Car IS-A Vehicle.
`v.move()` calls Car's version at runtime — method resolved by actual type, not declared type.

## Inheritance vs Composition
| | Inheritance | Composition |
|---|---|---|
| Relationship | IS-A | HAS-A |
| Coupling | Tight — subclass depends on parent internals | Loose — depends on interface |
| Flexibility | Fixed at compile time | Swappable at runtime |

Prefer composition when the relationship is HAS-A or behaviour needs runtime swap.
Use inheritance when IS-A is genuine and the hierarchy is shallow.

## Watch-outs
- Deep inheritance chains — fragile, hard to reason about, avoid beyond 2 levels
- Overriding without `@Override` annotation — compiler won't catch signature mismatches
- Calling overridable methods from constructor — subclass method runs before subclass is initialized
- Java supports single inheritance only — use interfaces for multiple capability contracts