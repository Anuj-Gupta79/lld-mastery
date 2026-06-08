# Inheritance

## What it is

A mechanism where a subclass acquires the fields and methods of a superclass,
then extends or replaces them as needed.
Models IS-A relationships — `Car` IS-A `Vehicle`, `Bike` IS-A `Vehicle`.

## Why it exists

When multiple types genuinely share the same origin and common behaviour,
repeating that behaviour in each class is waste and a maintenance trap.
Inheritance lets you write it once in the parent and have every subclass get it automatically.
The subclass focuses only on what makes it different.

## How it helps

- Shared logic lives in one place — fix a bug in `Vehicle.move()`, all vehicles benefit.
- Polymorphism: caller code works against `Vehicle`, actual behaviour comes from the real type at runtime.
- Subclass constructor stays lean — delegates common setup to `super()`.

## The key ideas in practice

**`extends`**
Subclass inherits all non-private fields and methods.
Private fields are inherited but not directly accessible — subclass goes through getters.

**`super()`**
Must be the first line in a subclass constructor.
Delegates common field initialisation to the parent — subclass only sets what's unique to it.
`Car` hardcodes `4` wheels by calling `super(brand, 4)` — callers never need to pass wheel count.

**`@Override`**
Subclass provides its own version of a parent method.
`Car.move()` calls `super.move()` first then adds Car-specific output — reuse the base, extend on top.
`Bike.move()` replaces the parent behaviour entirely — no `super` call needed.
Always write `@Override` — the compiler catches signature mismatches that would otherwise silently create a new method.

**Polymorphism**
`Vehicle v = new Car(...)` is valid — Car IS-A Vehicle.
`v.move()` calls Car's version at runtime — method resolved by actual object type, not declared type.
Swap the object, the behaviour changes. The caller's code stays the same.

## Inheritance vs Composition

|              | Inheritance                             | Composition                       |
| ------------ | --------------------------------------- | --------------------------------- |
| Relationship | IS-A                                    | HAS-A                             |
| Coupling     | Tight — subclass knows parent internals | Loose — depends only on interface |
| Flexibility  | Fixed at compile time                   | Swappable at runtime              |

Prefer composition when the relationship is HAS-A or when behaviour needs to vary at runtime.
Use inheritance when IS-A is genuine and the hierarchy is shallow.

## Notes for revision

- The golden rule: if you can't say "X IS-A Y" without it feeling forced, don't inherit — compose.
- Deep inheritance chains are fragile. Two levels is fine. Three is a smell. Beyond that, something is wrong.
- Calling an overridable method from a constructor is a classic trap: the subclass method runs before the subclass constructor has finished, so fields it relies on are still default-initialised (null, 0).
- Java has single inheritance only for classes — you get one superclass. For multiple capability contracts, use interfaces.
- Overriding without `@Override` is silent — if you mistype the method name, Java creates a new method and calls the parent's. The annotation turns this into a compile error.
