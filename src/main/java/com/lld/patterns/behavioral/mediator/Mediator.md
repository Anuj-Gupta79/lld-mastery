# P16 — Mediator Pattern

## Intent
Defines an object that encapsulates how a set of objects interact.
Promotes loose coupling by keeping objects from referring to each other explicitly,
and lets you vary their interaction independently.

---

## The Problem It Solves
Without Mediator: N objects → O(N²) connections. Every object knows every other.
Adding one object means wiring it to all existing ones.

With Mediator: N objects → O(N) connections. Every object knows only the Mediator.
Adding one object means wiring it to the Mediator only.

---

## Structure

```
«interface»              «interface» (optional)
Mediator                 Colleague
+ send(sender, msg)      + receive(sender, msg)
      ▲                        ▲
      |                        |
  ChatRoom                   User
(ConcreteMediator)       (ConcreteColleague)
```

### Key Relationships
- **Colleague holds Mediator interface** — not the concrete class
- **Mediator holds all Colleagues** — via `List<User>`
- Colleague calls `mediator.send(this, message)` — passes itself
- Mediator loops, skips sender, calls `colleague.receive()` on everyone else

---

## Key Concepts

| Concept | Rule |
|---|---|
| Mediator interface | Only exposes coordination contract — `send`. Receive belongs to Colleague. |
| Colleague field type | Always `Mediator` (interface), never `ChatRoom` (concrete) |
| Sender exclusion | `Objects.equals(user, sender)` — safe when passing `this` (reference equality) |
| Add/Remove | Managed by Mediator — colleagues never hold refs to each other |
| Coordination logic | Lives entirely in Mediator — not in Colleague |

---

## Why Colleague Holds Mediator Interface
Colleague field must be `Mediator` (interface), never `ChatRoom` (concrete).
Tomorrow `SlackRoom` or `DiscordRoom` can implement `Mediator` — User doesn't change at all.

---

## Mediator vs Direct Reference

| | Direct Reference | Mediator |
|---|---|---|
| Coupling | Each object knows others | Each object knows only Mediator |
| Scalability | O(N²) connections | O(N) connections |
| Add/Remove | Update all objects | Update Mediator only |
| Coordination logic | Scattered across objects | Centralized in Mediator |

---

## Real World Analogies

| Analogy | Mediator | Colleagues |
|---|---|---|
| Airport | Control tower | Planes |
| Real estate | Broker | Owners + Renters |
| Group chat | ChatRoom | Users |
| Traffic | Traffic light controller | Cars |

---

## Common Mistakes

- Putting `receive()` on the Mediator interface — it belongs on Colleague
- Colleague holding concrete class reference instead of interface
- Mediator holding logic that belongs in Colleague (over-centralization)
- Using `users.remove(users.indexOf(user))` — redundant; use `users.remove(user)` directly

---

## Mediator vs Observer (Preview)
Both involve one object notifying multiple others.
- **Observer** — subject doesn't know who's listening; subscribers self-register
- **Mediator** — colleagues know the mediator; mediator actively decides routing

Full comparison after P18 — Observer.

---