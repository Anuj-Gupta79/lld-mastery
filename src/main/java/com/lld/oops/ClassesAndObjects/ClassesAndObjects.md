# Classes and Objects

## What it is
A class is a blueprint — it defines what state an object holds and what behaviour it can perform.
An object is a live instance of that blueprint, with its own copy of the state.
Every `Car` object created from the same class has its own `brand`, `speed`, `fuel` — independent of every other `Car`.

## Why it exists
Without a class, you'd repeat the same fields and logic everywhere you needed a Car.
The class captures the concept once — the object is that concept brought to life with specific values.
This is the foundation everything else in OOP builds on.

## How it helps
- One blueprint, unlimited instances — each with independent state.
- Behaviour and state travel together — you don't pass data around separately, you ask the object to act.
- Rules stay inside the class — callers interact with behaviour, not raw data.

## The key ideas in practice

**Fields**
Represent what the object *knows* — `brand`, `speed`, `fuel`.
Private by default — outside code has no direct access.

**Constructor**
Runs at creation time, sets the object's initial state.
A half-built object (fields left at defaults unintentionally) is a bug waiting to happen.
The constructor's job is to make the object fully valid from the first line after `new`.

**Methods**
Represent what the object *can do* — `accelerate()`, `brake()`, `refuel()`.
Behaviour owns the rules: `brake()` ensures speed never goes below zero.
The caller says *what* to do, not *how* — the method decides the details.

**`this` keyword**
When a constructor parameter has the same name as a field, `this` disambiguates.
`this.brand = brand` — left side is the field on the object, right side is the parameter passed in.
Without `this`, both sides refer to the parameter and the field never gets set.

**Getters**
Controlled read access — outside code can see the value but cannot write it directly.
Getter can also contain logic (format, compute, derive) rather than just returning the field.

## Notes for revision
- Class = blueprint, object = instance. Simple but worth saying out loud until it's automatic.
- `new Car(...)` allocates memory, calls the constructor, returns a reference. The reference is not the object — it's a pointer to it.
- Two references can point to the same object. Mutating through one is visible through the other.
- Private fields with no setters = the class is the only thing that can change its own state. That's the goal.
- The test for a good class: can a caller ever put the object into a state the class wouldn't allow? If yes, the class is leaking control.
- `this` confusion is one of the most common beginner bugs — when a field silently never gets assigned because the parameter shadowed it. The compiler won't warn you.