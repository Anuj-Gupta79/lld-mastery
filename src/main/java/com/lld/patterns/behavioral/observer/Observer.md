# P18 — Observer Pattern

## Intent
Subject broadcasts state changes to all registered Observers without knowing what they do with the update.

## Key Roles
- **Observable (interface)** — contract forcing Subject to support add/remove/notify
- **Observer (interface)** — contract forcing listeners to implement `update()`
- **Subject (Stock)** — maintains observer list, triggers notification on state change
- **ConcreteObservers** — react independently to the same notification

## Core Rules
- `notifyObservers()` is **private** — only the Subject decides when to notify
- Observer list is on the Subject, not shared
- Each Observer acts independently — Subject does not care what they do
- Wrap each `observer.update()` in try-catch — one failure must not break the loop
- `removeObserver()` takes the **Object**, not the index — `List.remove(Object)` not `List.remove(int)`

## Memory Leak Warning
Subject holds a reference to every registered Observer. If an Observer is never removed, the GC cannot collect it — even if nothing else references it. Always deregister when done.

## Observer vs Mediator
- **Observer** — Subject knows Observers, Observers don't know each other. Fire and forget broadcast.
- **Mediator** — Colleagues know only the Mediator. All communication routed through the center.

## Async Consideration
Sequential notification means a slow Observer blocks all subsequent ones. Fix direction: notify each Observer on its own thread.

## Why Not `java.util.Observable`?
Deprecated in Java 9 — it was a **class**, not an interface. Subject couldn't extend anything else. Always define your own interface.