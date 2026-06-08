# Abstract Class vs Interface

## What it is

Two mechanisms for defining shared contracts in Java.
Abstract class — a partial blueprint that related classes build on.
Interface — a capability contract that any class can choose to honour, regardless of where it sits in the hierarchy.

## Why it exists

Not everything that shares behaviour shares an origin.
A `Duck` and a `Person` both swim — but they aren't the same kind of thing.
Forcing them under a common superclass just to share `swim()` is wrong.
Interfaces solve this: define the capability separately, let anyone adopt it.
Abstract classes solve the other problem: when a family of types genuinely share state and base logic, don't repeat it across every subclass.

## How it helps

- Abstract class gives you a home for shared code — subclasses inherit it for free.
- Interface gives you a contract — caller code works against the interface, not the concrete type.
- Both together: extend an abstract class for shared origin, implement interfaces for extra capabilities.

## The four variants in practice

**Abstract class (`LivingBeing`)**
Defines `breathe()` once — all living things breathe the same way.
Declares `move()` and `eat()` abstract — every subclass must provide its own version.
You cannot instantiate `LivingBeing` directly — it's incomplete by design.

**Interface (`Flyable`, `Swimmable`)**
Declares what a type _can do_, nothing about what it _is_.
`Duck` implements both — it can fly AND swim.
`Eagle` implements only `Flyable` — no forced inheritance chain needed.
Java allows a class to implement multiple interfaces — the only way to get multiple-capability contracts.

**Polymorphism via interface array**
`Swimmable[] swimmers = { new Person(), new Duck() }` — loop calls `swim()` on each.
Neither `Person` nor `Duck` know about each other. New swimmers added without touching the loop.

## When to pick which

- Shared state or protected helpers among related types → abstract class
- Capability that unrelated types need → interface
- Need both → extend the abstract class AND implement the interfaces (Java allows this)
- Abstract class with zero concrete methods → should probably just be an interface

## Notes for revision

- IS-A = abstract class. CAN-DO = interface. Tattoo this.
- `default` methods in interfaces (Java 8+) let you add shared logic without breaking existing implementors — useful but don't abuse it, interfaces have no state.
- Interface with too many methods is an ISP violation — split it.
- If you're ever unsure, start with an interface. You can always introduce an abstract class later. Going the other way is painful.
- Multiple inheritance of implementation is not allowed in Java (two superclasses) — but multiple interface implementation is. That's the escape hatch.
