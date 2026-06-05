# P9 — Decorator Pattern

## Intent
Add behaviour to an object dynamically by wrapping it in decorator objects.
Each decorator adds one responsibility and delegates the rest down the chain.

## Core Structure
- **Component interface** (`TextFormatter`) — common contract for base and decorators
- **Concrete Component** (`PlainText`) — base implementation, no added behaviour
- **Abstract Decorator** (`TextDecorator`) — implements interface, holds wrapped reference, delegates by default
- **Concrete Decorators** (`UpperCaseDecorator`, `TrimDecorator`, `ExclaimDecorator`) — each adds one behaviour

## Key Concepts

**How the chain works:**
Each decorator wraps another `TextFormatter`.
`format()` calls `super.format(text)` which delegates to the wrapped object,
then applies its own transformation to the result.
Outermost decorator runs first; innermost (PlainText) runs last on the way down,
result bubbles back up through each layer.

**Why abstract decorator holds the reference:**
Without it, every concrete decorator would repeat the delegation boilerplate.
Abstract decorator centralises it — concrete decorators only add their specific behaviour.

**Order matters:**
`Trim(Upper(Plain))` → trims first, then uppercases.
`Upper(Trim(Plain))` → uppercases first, then trims.
Same decorators, different order, different result.

**Runtime composition:**
Wrapping happens at construction time with plain `new` calls.
No subclass needed for every combination — 3 decorators give you 6 orderings without 6 classes.

## Decorator vs Inheritance
| | Decorator | Inheritance |
|---|---|---|
| Composition | Runtime, combinable | Compile-time, fixed |
| New behaviour | Wrap with new decorator | New subclass |
| Combinations | N decorators = N! orderings | Each combo = one subclass |

## When to Use
- Need to add responsibilities to objects without modifying their class
- Subclassing would cause combinatorial class explosion
- Behaviours should be stackable and reorderable at runtime