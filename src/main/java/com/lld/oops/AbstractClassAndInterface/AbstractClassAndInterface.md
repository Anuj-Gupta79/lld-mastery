# OOP — Abstract Class vs Interface

## Intent
Abstract class — share a common base with partial implementation for related types.
Interface — define a capability contract that unrelated types can adopt independently.

## Core Distinction

| | Abstract Class | Interface |
|---|---|---|
| Relationship | IS-A | CAN-DO |
| Use when | Classes share state or base behaviour | Classes share a capability, not an origin |
| Inheritance | Single only | Multiple allowed |
| Can have implementation | Yes (concrete methods) | Yes (default methods, Java 8+) |
| Constructor | Yes | No |

## Key Concepts

### Abstract class
Forces subclasses to implement abstract methods while providing inherited concrete methods.
Models a family — `LivingBeing` guarantees `breathe()` is shared; `move()` and `eat()` vary per type.

### Interface
Models a role — `Flyable` and `Swimmable` are capabilities.
`Duck` can fly AND swim; `Eagle` can only fly — no shared superclass needed.
Multiple interface implementation avoids the rigidity of single inheritance.

### Polymorphism via array
`Swimmable[] swimmables = { new Person(), new Duck() }` — uniform treatment of different types.
New swimmers added without touching the loop — open/closed in practice.

### When to pick which
- Shared state or protected helpers → abstract class
- Capability across unrelated types → interface
- Both needed → extend abstract class + implement interfaces (Java allows this)

## Watch-outs
- Abstract class with no concrete methods = should probably be an interface
- Interface with too many methods = ISP violation — split it
- `default` methods in interfaces can approximate shared logic but lack state — don't overuse