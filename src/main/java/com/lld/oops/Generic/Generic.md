# Generics

## What it is

A way to write classes and methods that work on _any_ type while letting the
compiler verify type correctness at compile time.
You write the logic once — the type is filled in when the code is actually used.

## Why it exists

Before generics, the only way to write reusable container code was to use `Object`.
That works, but every retrieval required an explicit cast — and a wrong cast blows up at runtime
with a `ClassCastException` that the compiler never warned you about.
Generics move that check to compile time: wrong type = compile error, not a production crash.

## How it helps

- One implementation, many types — write `Pair<T, V>` once, use it for any combination.
- Compiler catches type mismatches before the code runs.
- No casts in calling code — cleaner, safer, self-documenting.
- Tighter bounds mean more methods available inside the generic code itself.

## The key ideas in practice

**Generic class (`Pair<T, V>`)**
`T` and `V` are placeholders resolved at instantiation.
`Pair<String, Integer>` and `Pair<Double, String>` are distinct uses of the same blueprint.
The class doesn't care what types you use — the compiler enforces consistency.

**Bounded type parameter**
`<T extends Comparable<T>>` — T must implement `Comparable`, which unlocks `compareTo()`.
`<T extends Number>` — T must extend `Number`, which unlocks `doubleValue()`, `intValue()`, etc.
Bounds express what capabilities you _need_ from T — tighter bound means more methods you can call.

**Wildcard (`List<?>`)**
Accepts any `List` regardless of element type — useful when you only need to iterate.
Read-only: you cannot add elements because the compiler doesn't know the actual type at that point.
Use `<?>` when consuming. Use `<T>` when producing or inserting.

**Single responsibility on generic classes**
`Pair` holds two values — that is its entire job.
Utility methods like `findMax` or `calculateAverage` belong in a separate class, not bolted onto the data holder.

## Notes for revision

- Generics are erased at runtime (type erasure) — `List<String>` and `List<Integer>` are both just `List` at the bytecode level. Generics are a compile-time safety net, not a runtime feature.
- Don't shadow the class type parameter inside a method — if the class uses `T`, name the method's parameter `E` or `U` to avoid silent confusion.
- `@SuppressWarnings("hiding")` on a generic method is a signal to rename, not to suppress.
- Wildcard `?` = read only. Bounded param `<T extends X>` = read and write. Keep this straight.
- If you ever need to know the actual type at runtime (e.g. `instanceof T`), generics can't help you — that information is gone. You'd need to pass a `Class<T>` explicitly.
