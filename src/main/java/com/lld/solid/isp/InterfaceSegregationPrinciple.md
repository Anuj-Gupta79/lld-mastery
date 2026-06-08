# Interface Segregation Principle (ISP)

## What it is

The Interface Segregation Principle states:

> Clients should not be forced to depend upon methods they do not use.

In simple words:

> Prefer many small, focused interfaces over one large interface.

A class should implement only the behaviours it actually supports.

In this example:

- An Intern can work, eat, sleep, and raise PRs.
- A Manager can work, eat, sleep, manage teams, and sign contracts.

They should not be forced to implement responsibilities that don't belong to them.

---

## Why it exists

Imagine a giant interface:

```java
interface WorkInterface
```

containing:

```java
work()
eat()
sleep()
manageTeam()
signContract()
raisePR()
```

Now every implementation must provide all methods.

Problem:

```java
Intern
```

doesn't manage teams.

```java
Manager
```

doesn't raise PRs.

Yet both are forced to implement these methods.

This usually results in:

```java
throw new UnsupportedOperationException(...)
```

which is a strong signal that the interface is too large.

---

## How it helps

- Reduces unnecessary dependencies.
- Makes interfaces easier to understand.
- Prevents meaningless implementations.
- Improves maintainability.
- Improves flexibility.
- Reduces ripple effects when interfaces change.

Small interfaces are easier to evolve than giant ones.

---

## The key ideas in practice

### The violation

```java
interface WorkInterface
```

contains responsibilities for:

- Workers
- Managers
- Developers
- Leadership

all mixed together.

As a result:

```java
InternViolation
```

must implement:

```java
manageTeam()
signContract()
```

even though those actions don't make sense.

---

### The biggest red flag

```java
throw new UnsupportedOperationException(...)
```

Example:

```java
@Override
public void manageTeam() {
    throw new UnsupportedOperationException();
}
```

The class is basically saying:

> "I was forced to implement this method, but I cannot actually support it."

That is often an ISP violation.

---

### Breaking the interface apart

Instead of:

```java
WorkInterface
```

we create focused interfaces:

```java
Workable
```

```java
Eatable
```

```java
Sleepable
```

```java
Manageable
```

```java
ContractSignable
```

```java
PRRaisable
```

Each interface represents one capability.

---

### Intern implementation

```java
class InternFix
    implements Workable,
               Eatable,
               Sleepable,
               PRRaisable
```

The intern implements only the behaviours it supports.

No fake methods.

No exceptions.

No confusion.

---

### Manager implementation

```java
class ManagerFix
    implements Workable,
               Eatable,
               Sleepable,
               Manageable,
               ContractSignable
```

Again, only relevant capabilities are implemented.

This is exactly what ISP encourages.

---

### Real-world example

Bad design:

```java
interface Worker {
    code();
    test();
    deploy();
    hire();
    fire();
}
```

Every employee must implement everything.

Good design:

```java
Coder
Tester
Deployer
Interviewer
Manager
```

Each role implements only the capabilities it needs.

---

## Common interview questions

### What is ISP in one sentence?

Clients should not be forced to depend on methods they don't use.

---

### What is the main goal of ISP?

To create small, focused interfaces instead of large, general-purpose ones.

---

### What is the most common sign of ISP violation?

Methods that contain:

```java
throw new UnsupportedOperationException(...)
```

because the implementation doesn't actually support the behaviour.

---

### Is having many small interfaces bad?

No.

Small interfaces are usually easier to understand and maintain.

However, don't create tiny interfaces without a meaningful responsibility.

---

### How is ISP related to SRP?

SRP focuses on responsibilities of classes.

ISP focuses on responsibilities of interfaces.

Both aim to reduce unnecessary coupling.

---

### Does ISP encourage composition?

Yes.

Small interfaces work very well with composition and flexible designs.

---

## Notes for revision

- ISP = Interface Segregation Principle.
- Clients should not depend on methods they don't use.
- Prefer many small interfaces over one giant interface.
- Large interfaces often force unnecessary implementations.
- `UnsupportedOperationException` is a common ISP violation signal.
- Interfaces should represent capabilities.
- Intern should not implement manager responsibilities.
- Manager should not implement developer-only responsibilities.
- ISP reduces coupling and improves flexibility.
- Small interfaces are easier to evolve and maintain.
- Interview memory trick:

  **"Don't force a class to implement what it cannot do."**