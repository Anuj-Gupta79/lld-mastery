# Command Pattern

## What Is It
A behavioral pattern where an action is wrapped as an object. The object knows how to execute the action — and optionally, how to reverse it. The caller never invokes the action directly; it hands a command to an invoker.

## The Problem It Solves
Direct method calls are fire-and-forget. Once `fan.turnOn()` returns, the action is gone — nothing to undo, queue, retry, or log. Command preserves the action as a first-class object so it can be stored, scheduled, and reversed.

## Structure
| Role | Responsibility |
|---|---|
| `Command` | Interface — defines `execute()` and `undo()` contract |
| `ConcreteCommand` | Holds a reference to the Receiver + any state needed to reverse the action |
| `Receiver` | The object that actually does the work — pure logic, no command knowledge |
| `Invoker` | Holds a history of executed commands. Calls `execute()`, pushes to stack. Calls `undo()` by popping and reversing. |
| `Client` | Wires Receiver into Commands, hands Commands to Invoker |

## How It Works
1. Client creates a `ConcreteCommand`, injecting the Receiver.
2. Client passes the command to `Invoker.executeCommand()`.
3. Invoker calls `command.execute()` → command delegates to Receiver.
4. Invoker pushes command onto history stack.
5. On `undo()` → Invoker pops last command, calls `command.undo()` → command reverses the action on the Receiver.

## When To Use
- You need undo/redo functionality.
- You need to queue or schedule actions for later execution.
- You need to log or audit every action taken.
- You need to support transactional behavior — execute, and roll back on failure.
- Examples: text editors, transaction systems, job queues, macro recording, UI button actions.

## When NOT To Use
- Actions are simple and one-directional — wrapping adds overhead with no benefit.
- No need for history, undo, or scheduling — a direct call is cleaner.
- Too many command classes can bloat the codebase — evaluate if lambdas/functional interfaces suffice for simple cases.

## vs Similar Patterns
| Pattern | Intent |
|---|---|
| **Command** | Wraps action as object for undo, queue, log |
| **Strategy** | Wraps algorithm for swappable behavior — no history, no undo |
| **Chain of Responsibility** | Request passed along handlers until one handles it — no undo |
| **Memento** | Also enables undo — but saves full object state, not the action itself |

## Key Concepts From Implementation
- `deletedText` must be captured inside `execute()`, not the constructor — the state to save isn't known until execution time.
- Invoker holds no reference to the Receiver — it talks only to the `Command` interface. The command carries the receiver inside it.
- Stack gives LIFO — last executed is first undone. That's the correct undo order.
- Push to history only after `execute()` succeeds — never push a failed command.

## Key Insight
The action outlives the call. Wrapping it as an object gives it a lifetime — it can be stored, reversed, replayed, or queued. The Invoker stays decoupled from what the action does; it only knows when to fire it.