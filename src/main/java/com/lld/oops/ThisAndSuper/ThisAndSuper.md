# this and super

## What it is

`this` and `super` are special references provided by Java to navigate between the current class and its parent class.

- `this` refers to the current object.
- `super` refers to the parent-class part of the current object.

In this example:

- `this()` is used for constructor chaining within the same class.
- `this.field` is used to access instance variables when parameter names clash.
- `super()` is used to initialize the parent class.
- `super.method()` is used to reuse parent behaviour inside an overridden method.

Think of them as navigation keywords:

- `this` → stay in the current class.
- `super` → move one level up the inheritance hierarchy.

---

## Why it exists

Imagine `Animal` has logic to initialize `name` and `type`.

Without `super()`, every subclass would have to duplicate that logic.

Similarly, if a class has multiple constructors, repeating initialization code in every constructor becomes messy and error-prone.

Java introduced:

- `this()` to reuse constructors in the same class.
- `super()` to reuse constructors from the parent class.

The result is cleaner code, less duplication, and easier maintenance.

---

## How it helps

- Eliminates duplicate constructor logic.
- Makes object creation easier to maintain.
- Encourages code reuse through inheritance.
- Allows subclasses to extend behaviour instead of rewriting it.
- Makes the relationship between parent and child classes explicit.

---

## The key ideas in practice

### `this.field` vs local variable

```java
Animal(String name, String type) {
    this.name = name;
    this.type = type;
}
```

Both constructor parameters and instance variables have the same names.

Without `this`, Java would not know which `name` or `type` you mean.

`this.name` means:

> Use the `name` field belonging to the current object.

---

### `this()` constructor chaining

```java
Animal(String name) {
    this(name, "Unknown");
}
```

Instead of writing initialization logic again, one constructor delegates to another.

This pattern is called **constructor chaining**.

A common design rule is:

> Let simpler constructors delegate to the most complete constructor.

That way all initialization logic exists in one place.

---

### `super()` constructor call

```java
Dog(String name, String type, String breed) {
    super(name, type);
    this.breed = breed;
}
```

Before Java can create the `Dog` part of the object, it must create the `Animal` part.

That's why parent constructors always execute first.

Object creation order:

```text
Animal constructor
        ↓
Dog constructor
```

---

### `super.method()`

```java
@Override
public void displayInfo() {
    super.displayInfo();
    System.out.println("Breed: " + breed);
}
```

The parent class already knows how to display `name` and `type`.

Instead of rewriting that code, the child reuses it and adds its own behaviour.

Output flow:

```text
Animal.displayInfo()
        ↓
Dog.displayInfo()
```

This is one of the cleanest ways to extend behaviour in inheritance.

---

### Constructor execution flow in this example

Creating:

```java
new Dog("Max");
```

Execution chain:

```text
Dog(String)
    ↓
Dog(String, String)
    ↓
Dog(String, String, String)
    ↓
Animal(String, String)
```

Understanding this flow helps answer many inheritance interview questions.

---

## Common interview questions

### Why must `this()` be the first statement in a constructor?

Because Java wants constructor chaining to happen before any initialization logic.

The object must follow a predictable initialization sequence.

---

### Why must `super()` be the first statement in a constructor?

Because the parent part of the object must be initialized before the child part.

Java enforces this rule at compile time.

---

### Can a constructor use both `this()` and `super()`?

No.

Both must be the first statement.

Since only one statement can be first, a constructor can directly call only one of them.

---

### What happens if I don't write `super()`?

Java automatically inserts:

```java
super();
```

But only if the parent has a no-argument constructor.

If it doesn't exist, compilation fails.

---

### Can `this` access static members?

Yes.

```java
this.someStaticField
```

compiles successfully.

However, it is discouraged because static members belong to the class, not the object.

Prefer:

```java
ClassName.someStaticField
```

for better readability.

---

### Can `super` access private members of the parent class?

No.

Private members belong only to the declaring class and are not directly accessible from subclasses.

---

## Notes for revision

- `this` = current object.
- `super` = parent-class part of the current object.
- Use `this.field` when parameter names and field names are the same.
- `this()` calls another constructor in the same class.
- `super()` calls a constructor from the parent class.
- Both `this()` and `super()` must be the first statement in a constructor.
- A constructor cannot directly call both `this()` and `super()`.
- Parent constructor always runs before child constructor.
- `super.method()` is commonly used to extend behaviour rather than replace it.
- If no constructor call is written, Java inserts `super()` automatically.
- Constructor chaining is mainly about avoiding duplicated initialization code.
- In interviews, remember the creation order: **Parent first → Child second**.
- If you ever forget:
  - `this` → same class
  - `super` → parent class