# P4 — Prototype Pattern

## Intent
Create new objects by cloning an existing instance. The original acts as a template;
clones start with its state and diverge only where needed.

## Core Structure
- **Prototype** (`Cloneable`) — marks the class as cloneable
- **Concrete Prototype** (`StudentTemplate`) — implements `clone()`, handles deep copy
- **Client** — calls `clone()` instead of `new`, then tweaks only what differs

## Key Concepts

**Shallow copy vs deep copy:**
`super.clone()` does a shallow copy — primitive fields are duplicated, object references are shared.
For mutable fields (like `List<String> subjects`), shallow copy means both original and clone
point to the same list. A mutation in one affects the other.
Fix: manually copy mutable fields — `clone.subjects = new ArrayList<>(this.subjects)`.

**Rule of thumb:**
Every mutable field in a cloned object needs its own copy.
Immutable fields (String, int, etc.) are safe to share.

**When Prototype beats new:**
- Object construction is expensive (DB call, config load, heavy computation)
- Many objects share most state; only a few fields differ per instance
- Exact class of object isn't known at compile time

## Tradeoffs
- Deep copy logic must be maintained manually — easy to forget a new mutable field
- Circular references make deep copy complex
- `Cloneable` in Java is a marker interface with no method — `clone()` is on `Object`,
  which is a known design flaw. Alternative: copy constructor or dedicated `copy()` method.

## Prototype vs Builder
| | Prototype | Builder |
|---|---|---|
| Starting point | Existing instance | Fresh construction |
| Use case | Many similar objects | Complex object with optional fields |
| Mutation after creation | Yes (setters to tweak) | No (immutable after build) |