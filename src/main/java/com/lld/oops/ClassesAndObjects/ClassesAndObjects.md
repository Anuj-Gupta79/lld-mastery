# OOP — Classes and Objects

## Intent
Class = blueprint that defines state and behaviour.
Object = instance created from the blueprint, holding its own copy of state.

## Core Concepts

### Class structure
- **Fields** — state (what the object knows)
- **Constructor** — initializes state at creation time
- **Methods** — behaviour (what the object can do)

### Encapsulation
Fields are private — outside code cannot read or modify them directly.
Controlled access via getters; mutation only through methods that enforce rules.

### Behaviour owns the rules
`brake()` ensures speed never goes negative — the class enforces this, not the caller.
Caller just says `brake()`, not `speed -= 10`. Logic stays in one place.

### `this` keyword
Disambiguates when constructor parameter name matches field name.
`this.brand = brand` — left side is the field, right side is the parameter.

## Watch-outs
- Never expose mutable fields directly — caller could corrupt state
- Constructor should fully initialize the object — no half-built state
- Getters for read access only — avoid setters unless mutation is genuinely needed