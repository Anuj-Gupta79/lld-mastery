# P6 — Adapter Pattern

## Intent
Make an incompatible interface usable without modifying either the client or the adaptee.
The adapter sits between them and translates calls.

## Core Structure
- **Target interface** (`PaymentProcessor`) — what the client expects
- **Adaptee** (`BankApi`) — existing class with incompatible method signature
- **Adapter** (`PaymentAdapter`) — implements Target, holds Adaptee, translates calls
- **Client** (`AdapterDemo`) — only knows about Target interface

## Key Concepts

**What "incompatible" means:**
Client calls `pay()`. BankApi only has `makeTransaction()`.
Neither side changes — Adapter absorbs the translation.

**Composition over inheritance:**
Adapter holds a reference to BankApi (`has-a`), not extends it.
Preferred because it works even when Adaptee is final or comes from a third-party library.

**OCP in action:**
BankApi is untouched. Client is untouched. New adapter = new integration, zero modification.

## Object Adapter vs Class Adapter
| | Object Adapter | Class Adapter |
|---|---|---|
| Mechanism | Holds Adaptee via composition | Extends Adaptee (inheritance) |
| Flexibility | Can wrap any subclass of Adaptee | Locked to one concrete Adaptee |
| Java support | ✅ Preferred | ⚠️ Limited (no multiple inheritance) |

## When to Use
- Integrating a third-party library whose interface doesn't match yours
- Reusing legacy code without modifying it
- Standardizing multiple incompatible implementations behind one interface