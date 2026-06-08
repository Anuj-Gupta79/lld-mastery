# Static and Final

## What it is

`static` and `final` are two fundamental Java keywords that solve different problems.

`static` makes a member belong to the class rather than an object. All instances share the same static field, and static methods can be called without creating an object.

`final` restricts modification. Depending on where it is used, it can prevent reassignment of variables, prevent method overriding, or prevent class inheritance.

Together they are commonly used to create constants, utility classes, shared counters, immutable state, and fixed behaviour.

## Why it exists

Without `static`, every object would need its own copy of data even when that data should be shared across all instances.

Without `final`, important values and behaviour could be modified accidentally, making programs harder to reason about and maintain.

Java provides these keywords to clearly express:

- Shared state (`static`)
- Fixed values (`final`)
- Non-overridable behaviour (`final method`)
- Constants (`static final`)

## How it helps

- Saves memory when data should be shared.
- Provides a global access point for utility methods.
- Makes code safer by preventing accidental modification.
- Helps enforce design decisions through inheritance restrictions.
- Makes intent clear to other developers reading the code.

## The key ideas in practice

**Static fields**

`count` belongs to the `Counter` class itself.

```java
Counter counter1 = new Counter();
Counter counter2 = new Counter();
```

Both objects update the same `count` variable.

There is only one copy in memory regardless of how many objects are created.

---

**Static methods**

```java
Counter.getCount();
```

No object is required.

Static methods can directly access only static members because they execute without any instance context.

There is no `this` reference inside a static method.

---

**Static block**

```java
static {
    ...
}
```

Runs exactly once when the JVM loads the class.

It executes before any object creation and before static methods are used.

Typical uses:

- Loading configuration
- Registering drivers
- Initializing static resources

---

**Final variables**

```java
final int id;
```

A final variable can be assigned only once.

For instance variables, initialization usually happens:

- At declaration
- In an initializer block
- Inside the constructor

After assignment, reassignment is not allowed.

---

**Final reference**

```java
final Counter ref = new Counter();
```

The reference cannot point to another object.

```java
ref = new Counter(); // Compile-time error
```

However, the object itself may still change if it exposes mutable state.

`final` does not automatically make an object immutable.

---

**Final methods**

```java
public final void display() {
    ...
}
```

Subclasses inherit the method but cannot override it.

This guarantees that the behaviour remains unchanged throughout the inheritance hierarchy.

---

**Static final constants**

```java
public static final int MAX_USERS = 100;
```

This is the standard way to create constants in Java.

- `static` → one shared copy
- `final` → value cannot change

Constants are typically written using uppercase names with underscores.

## Notes for revision

- `static` belongs to the class, not an object.
- A static field has only one copy regardless of object count.
- Static methods can be called without creating an object.
- Static methods cannot directly access instance variables because no object exists.
- Static blocks execute once when the class is loaded.
- A final variable can be assigned only once.
- A final reference cannot be reassigned.
- `final` does not mean immutable.
- A final method cannot be overridden.
- A final class cannot be extended.
- `static final` is the standard way to define constants.
- Static members are loaded during class loading, while instance members belong to individual objects.
- Remember: `static` controls ownership, `final` controls modification.