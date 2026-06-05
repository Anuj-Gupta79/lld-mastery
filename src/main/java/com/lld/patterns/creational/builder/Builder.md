# P3 — Builder Pattern

## Intent
Construct a complex object step-by-step. Separate what gets built from how it's built.

## Core Structure
- **Product** (`Student`) — immutable, private constructor, all fields final
- **Builder** — static nested class, holds same fields, exposes fluent setters
- **build()** — terminal call that passes Builder into Product constructor

## Key Concepts

**Why private constructor on Product:**
Forces all creation through Builder. No partial or inconsistent Student can exist.

**Why Builder is static nested:**
Must be usable before any Student instance exists. Static = no outer instance required.

**Required vs optional split:**
Required fields go in Builder constructor — compiler enforces them.
Optional fields get setters — caller includes only what they need.

**Fluent API:**
Each setter returns `this` (the Builder), enabling method chaining:
`new Builder("1", "John").setEmail("...").setMobileNumber1("...").build()`

**Immutability guarantee:**
Student fields are `final`. No setters on Student. Once built, state is frozen.

## Why Not Just Use Constructors?
- 3 required + 3 optional = 8 possible constructors (telescoping problem)
- Caller can't tell which `null` maps to which field
- Builder makes the intent explicit at the call site

## Tradeoff
More boilerplate (duplicate fields in Builder). Worth it when object has 4+ fields or optional combinations.