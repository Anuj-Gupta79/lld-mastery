# Polymorphism

## What it is
Polymorphism means one interface, many behaviours.
A variable declared as `LivingBeing` can hold a `Human`, `Bird`, or `Animal` at runtime.
When you call `move()` on it, Java figures out which version to run based on the actual
object — not the declared type. This resolution at runtime is called dynamic dispatch.

## Why it exists
Without polymorphism, you'd need a separate loop or a chain of `if-else` blocks
for every type — and every new type added breaks the calling code.
With polymorphism, new subclasses plug in without the caller ever changing.
The caller only knows `LivingBeing`. What lives inside the array doesn't matter to it.

## How it helps
- Calling code is stable — add a new subclass, the loop still works unchanged.
- Behaviour varies by actual type — same method call, different result.
- Enables open/closed principle in practice: open for extension, closed for modification.

## The key ideas in practice

**Dynamic dispatch**
`LivingBeing[] livingBeings = { new Human(), new Birds(), new Animal() }` — declared type is `LivingBeing`.
At runtime, `being.move()` resolves to `Human.move()`, `Birds.move()`, or `Animal.move()` based on the real object.
The JVM holds a vtable per class — method call looks up the actual type's version, not the declared type's.

**@Override**
Each subclass overrides `move()` with its own implementation.
`@Override` tells the compiler to verify the signature matches — catches silent bugs where a typo creates a new method instead of overriding.

**instanceof + cast**
`makeNoise()` lives only on `Animal`, not on `LivingBeing`.
To reach it, you must check the real type with `instanceof` and then cast.
This is a smell — it means the superclass contract is incomplete.
The clean fix: move `makeNoise()` to an interface or the superclass, and the cast disappears entirely.

**The doubt in the code — answered**
If multiple unrelated classes have `makeNoise()`, you don't want instanceof chains for each.
Solution: define a `Noisy` interface with `makeNoise()`. Any class that makes noise implements it.
Then cast to `Noisy`, not to a specific class — one check covers all of them.
That's polymorphism working through interfaces instead of inheritance.

## Notes for revision
- Declared type controls what methods you can *call*. Actual type controls which *implementation* runs. Keep these two separate in your head.
- Polymorphism only works on overridden methods — if the subclass doesn't override, the superclass version runs (which is still correct behaviour, just inherited).
- `instanceof` + cast in a loop is a signal that the abstraction is incomplete — something belongs on the superclass or an interface that isn't there yet.
- Every time you add an `if (x instanceof SomeType)` block, ask: should this method just be on the superclass/interface?
- Compile-time polymorphism (method overloading) is different — resolved by parameter types at compile time, not at runtime. What's shown here is runtime polymorphism.
- The vtable lookup has negligible cost in practice — never avoid polymorphism for performance reasons at this level.