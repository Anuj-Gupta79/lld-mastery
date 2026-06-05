# P12 — Proxy Pattern

## Intent
Provide a surrogate or placeholder for another object to control access to it.

## Core Structure
- **Subject interface** — shared contract between Proxy and RealSubject
- **RealSubject** — the actual object that does the real work
- **Proxy** — holds a reference to RealSubject, controls when and how it is accessed
- **Client** — talks only to the Subject interface, unaware of proxy vs real

## Key Concepts

### Same interface as RealSubject
Proxy implements the same interface — client code never changes.
Substitution is transparent.

### Virtual Proxy (lazy initialization)
RealSubject created only on first use, not at construction time.
Null check inside the method guards single creation.
Use when construction is expensive (disk load, network call, heavy compute).

### Proxy types
| Type | Purpose |
|---|---|
| Virtual | Delay expensive creation until needed |
| Protection | Gate access based on permissions |
| Remote | Represent object in another JVM/network |
| Logging/Caching | Add cross-cutting behaviour without touching real object |

### Proxy vs Decorator
| | Proxy | Decorator |
|---|---|---|
| Purpose | Control access | Add behaviour |
| RealSubject creation | Proxy manages it internally | Decorator receives it externally |
| Client awareness | Transparent substitute | Explicit enhancement |

## Watch-outs
- Proxy manages RealSubject lifecycle — client should not create RealSubject directly
- Remove dead comments from Java — benefits list belongs in .md not in code
- Proxy adds indirection — only worth it when access control or lazy load is genuinely needed