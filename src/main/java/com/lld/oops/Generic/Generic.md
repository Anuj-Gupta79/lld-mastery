# OOP — Generics

## Intent
Write classes and methods that work on any type while preserving compile-time type safety.
Eliminates casts and surfaces type errors at compile time, not runtime.

## Core Concepts

### Generic class
`Pair<T, V>` — T and V are type parameters, resolved at instantiation.
`Pair<String, Integer>` and `Pair<Double, String>` are two distinct usages of the same blueprint.

### Bounded type parameter
`<T extends Comparable<T>>` — T must implement Comparable; unlocks compareTo().
`<T extends Number>` — T must extend Number; unlocks doubleValue(), intValue(), etc.
Bounds express what capabilities you need from T — tighter bound = more methods available.

### Wildcard — `List<?>`
Accepts any List regardless of element type.
Read-only — you cannot add elements (compiler doesn't know the actual type).
Use when you only need to iterate, not insert.

### Why not just use Object?
`Object[]` compiles but requires explicit casts everywhere.
Wrong cast throws ClassCastException at runtime.
Generics make the compiler do that check — zero runtime surprises.

### Single Responsibility on generic classes
`Pair` holds two values — that is its only job.
Utility methods (findMax, calculateAverage) belong in a separate class, not on the data holder.

## Watch-outs
- Don't shadow class type parameter in a method — rename method's param (E, U) to avoid confusion
- `@SuppressWarnings("hiding")` on a generic method = signal to rename, not suppress
- Wildcards (`?`) are for read-only consumption — use bounded params (`<T>`) when you need to produce or insert
- Generics are erased at runtime (type erasure) — `List<String>` and `List<Integer>` are both `List` at bytecode level