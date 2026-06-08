# Nested and Inner Classes

## What it is
Java lets you define a class inside another class. Four variants exist:
static nested, inner, local inner, and anonymous. Each has different
scope, access rules, and instantiation syntax.

## Why it exists
Sometimes a class only makes sense in the context of another class.
Nesting keeps that relationship explicit and limits the class's visibility
to where it's actually needed, instead of polluting the package namespace.

## The four variants

**Static nested class**
A class declared `static` inside an outer class. It belongs to the class,
not to any instance. Think of it as a helper class that happens to live
inside the outer class for organisational reasons.
- Can access outer static members. Cannot touch instance fields — there's no outer instance.
- Instantiate without an outer object: `new Computer.Processor(...)`.

**Inner class (non-static nested)**
Tied to a specific instance of the outer class. Holds an implicit back-reference
to the outer object that created it.
- Can access everything: static and instance members of the outer class.
- Must instantiate via an outer instance: `computer.new Battery(...)`.
- ⚠ Memory trap: the back-reference keeps the outer object alive even if you no longer need it.
  Prefer static nested when you don't actually need outer instance access.

**Local inner class**
Defined inside a method body. Completely invisible outside that method.
- Can access outer instance members and effectively-final local variables from the enclosing method.
- Use when helper logic is genuinely method-scoped and won't be reused elsewhere.

**Anonymous inner class**
A class with no name, defined and instantiated in one expression.
Always implements an interface or extends a class.
- Good for one-shot behaviour: event handlers, strategy variations, single-use callbacks.
- ⚠ If you declare a field inside it with the same name as an outer field, it silently shadows it.
- Modern note: for single-abstract-method interfaces (functional interfaces), use a lambda instead.
  Anonymous classes are still needed when you need state (multiple fields) or a non-functional interface.

## Static initialiser block
Runs once when the class is first loaded by the JVM, before any constructor.
Use for expensive one-time static setup. Order: static block → constructor, every time.

## How it helps
- Keeps tightly coupled helper classes close to where they belong.
- Reduces package clutter for classes that have no standalone meaning.
- Anonymous classes let you pass behaviour (implement an interface) without
  creating a file just for one use.

## Access cheat sheet

| Variant       | Outer static | Outer instance | Instantiation              |
|---------------|:---:|:---:|----------------------------|
| Static nested | ✅  | ❌  | `new Outer.Nested()`       |
| Inner         | ✅  | ✅  | `outerRef.new Inner()`     |
| Local inner   | ✅  | ✅  | Inside the method only     |
| Anonymous     | ✅  | ✅  | `new Interface() { ... }`  |

## Revision notes
- If you don't need the outer instance → make it static nested. Avoids the memory leak.
- Inner class holding a reference to a long-lived outer object is a classic Android/Swing memory leak pattern.
- Anonymous class shadowing outer fields is silent — no compiler warning. Watch for same-name fields inside the block.
- Lambda replaces anonymous class when the interface has exactly one abstract method.