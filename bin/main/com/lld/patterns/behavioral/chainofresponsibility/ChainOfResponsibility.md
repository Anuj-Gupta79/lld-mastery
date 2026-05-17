# Chain of Responsibility

## What Is It
A behavioral pattern where a request is passed along a chain of handlers. Each handler decides: handle it, or pass it to the next. The sender knows only the first handler.

## The Problem It Solves
Avoids hardcoding "who handles what" in a single place. Without this pattern, you get a fat `if-else` ladder where all handling logic lives in one class — every new case means touching that class (OCP violation). CoR lets you add, remove, or reorder handlers without touching existing ones.

## Structure
| Role | Responsibility |
|---|---|
| `Handler` (abstract) | Holds `nextHandler` reference, defines `handleRequest()` + abstract `canHandle()` / `processRequest()` |
| `ConcreteHandler` | Implements `canHandle()` and `processRequest()` for its specific case |
| `Client` | Builds the chain, sends request to the first handler only |

## How It Works
1. Client sends request to the first handler in the chain.
2. Each handler checks `canHandle()`.
3. If yes → `processRequest()`, chain stops.
4. If no → delegate to `nextHandler`.
5. If no handler matches and chain ends → fallback (log, throw, ignore).

## When To Use
- Multiple objects may handle a request and the handler isn't known upfront.
- You want to decouple sender from receiver.
- Handlers need to be added/removed/reordered at runtime or without touching existing code.
- Examples: support escalation, middleware pipelines, event bubbling, auth/validation filters.

## When NOT To Use
- Every handler must always process the request (use a Pipeline instead).
- Chain is long and performance is critical — every hop adds overhead.
- Handler order matters and must be guaranteed — chain wiring can be fragile.

## vs Similar Patterns
| Pattern | Intent |
|---|---|
| **Chain of Responsibility** | One handler handles, rest pass. Early exit. |
| **Pipeline** | Every stage transforms and passes forward. No early exit. |
| **Decorator** | Every wrapper adds behavior, all execute. Same interface, wraps not chains. |
| **Strategy** | One algorithm selected upfront, no chaining. |

## Key Insight
The handler decides — not the sender. That's the whole pattern. Decoupled dispatch with early exit, wired at the composition root.