# P11 — Flyweight Pattern

## Intent
Share common intrinsic state across many objects to reduce memory when large numbers of similar objects are needed.

## Core Structure
- **Flyweight interface** — declares operations that accept extrinsic state as parameters
- **Concrete Flyweights** — hold intrinsic state; must be immutable
- **Flyweight Factory** — caches and returns shared instances; creates only on cache miss
- **Context object** — holds extrinsic state; references the shared flyweight

## Key Concepts

### Intrinsic vs Extrinsic state
Intrinsic = shared, immutable, lives inside the flyweight (name, color, texture).
Extrinsic = unique per instance, passed in at call time (x, y, size).
The split is the entire design decision — get it wrong and sharing breaks.

### Factory as cache
Factory holds a `Map<key, Flyweight>`. On hit — return cached. On miss — create, cache, return.
Throw on unknown key — never return null, never cache null.

### Immutability is mandatory
Shared objects must not be mutated. If intrinsic state changes, all contexts sharing that object break silently.

### Context is not the Flyweight
`Tree` (context) is not the flyweight — `TreeType` is.
Thousands of `Tree` objects share two `TreeType` instances.

## Flyweight vs Singleton
| | Flyweight | Singleton |
|---|---|---|
| Instances | One per distinct intrinsic state | Exactly one total |
| Purpose | Memory sharing | Global access control |

## Watch-outs
- Mutable flyweight = shared corruption — always make fields `final`
- Passing extrinsic state correctly is the caller's responsibility
- Adds factory indirection — not worth it for small object counts
- New types require touching the factory switch — same OCP tradeoff as Factory pattern