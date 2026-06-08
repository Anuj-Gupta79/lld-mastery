# Dependency Inversion Principle (DIP)

## What it is

The Dependency Inversion Principle states:

> High-level modules should not depend on low-level modules. Both should depend on abstractions.

and

> Abstractions should not depend on details. Details should depend on abstractions.

In simple words:

> Depend on interfaces, not implementations.

In this example:

```java
NotificationService
```

should not care whether notifications are sent using:

- Email
- SMS
- WhatsApp
- Push Notifications

Its responsibility is simply:

```text
Send a notification
```

The delivery mechanism is a detail.

---

## Why it exists

Imagine we start with:

```java
NotificationService
    -> EmailSender
```

Everything works fine.

A few months later, the business asks for:

```text
SMS notifications
```

Now we modify:

```java
NotificationService
```

Again later:

```text
WhatsApp notifications
```

Modify again.

Later:

```text
Push notifications
```

Modify again.

The high-level business logic becomes tightly coupled to implementation details.

DIP solves this by introducing an abstraction between them.

---

## How it helps

- Reduces coupling.
- Makes systems easier to extend.
- Improves testability.
- Encourages dependency injection.
- Makes code easier to maintain.
- Supports OCP naturally.

A high-level module becomes stable because implementation details can change independently.

---

## The key ideas in practice

### The violation

```java
class NotificationServiceViolation
```

contains:

```java
private EmailSenderViolation emailSender;
```

and

```java
new EmailSenderViolation();
```

The service is tightly coupled to email.

If tomorrow we switch to SMS:

```java
NotificationServiceViolation
```

must change.

This violates DIP.

---

### The abstraction

```java
interface MessageSender
```

This interface represents:

```text
Anything capable of sending a message
```

The service only understands this contract.

It does not care about implementation details.

---

### Different implementations

```java
EmailSenderFix
```

```java
SMSSenderFix
```

```java
WhatsappSenderFix
```

All implement:

```java
MessageSender
```

Each class provides a different delivery mechanism.

---

### Dependency Injection

Instead of creating dependencies internally:

```java
new EmailSender()
```

we inject them:

```java
new NotificationServiceFix(emailSender)
```

This is called:

```text
Constructor Injection
```

which is the most common form of Dependency Injection.

---

### Why this is powerful

Today:

```java
new EmailSenderFix()
```

Tomorrow:

```java
new SMSSenderFix()
```

Next year:

```java
new PushNotificationSender()
```

The service remains unchanged.

Only new implementations are added.

This is DIP working together with OCP.

---

### Real-world Spring Boot connection

When you write:

```java
@Service
class NotificationService {

    private final MessageSender sender;

    public NotificationService(MessageSender sender) {
        this.sender = sender;
    }
}
```

Spring injects the implementation automatically.

This is one of the most common real-world uses of DIP.

---

## Common interview questions

### What is DIP in one sentence?

Depend on abstractions, not concrete implementations.

---

### What is a high-level module?

Business logic.

Example:

```java
NotificationService
```

It decides *what* should happen.

---

### What is a low-level module?

Implementation details.

Examples:

```java
EmailSender
SMSSender
WhatsappSender
```

They decide *how* something happens.

---

### How is DIP different from Dependency Injection?

Many people confuse them.

DIP is:

```text
A design principle
```

Dependency Injection is:

```text
A technique used to implement DIP
```

---

### Why are interfaces commonly used in DIP?

Interfaces provide stable contracts.

Implementations can change without affecting consumers.

---

### Does DIP improve testing?

Yes.

For example:

```java
MockMessageSender
```

can be injected during testing instead of sending real messages.

This makes unit testing much easier.

---

## Notes for revision

- DIP = Dependency Inversion Principle.
- High-level modules should not depend on low-level modules.
- Both should depend on abstractions.
- Depend on interfaces, not implementations.
- NotificationService is the high-level module.
- EmailSender, SMSSender, WhatsAppSender are low-level modules.
- MessageSender is the abstraction.
- Dependency Injection is a common way to implement DIP.
- Constructor Injection is the most common injection style.
- DIP and OCP often work together.
- DIP greatly improves testability.
- Interview memory trick:

  **"Business logic should know WHAT to do, not HOW it is done."**