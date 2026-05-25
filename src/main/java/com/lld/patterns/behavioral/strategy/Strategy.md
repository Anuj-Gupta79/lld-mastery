# P20 — Strategy Pattern

## Intent
Define a family of algorithms, encapsulate each one behind an interface, and make them interchangeable at runtime without changing the context.

## Core Structure
- **Strategy interface** — declares the algorithm contract
- **Concrete Strategies** — each implements one variation of the algorithm
- **Context** — holds a reference to the interface, delegates to it, exposes a setter for runtime switching

## Key Concepts

### Interface as contract
Context holds `StrategyInterface`, never a concrete type.
New strategies plug in without touching the context — open/closed.

### Runtime switching
Setter on context allows swapping strategy after construction.
The context doesn't know which strategy it holds — only that it has one.

### Default strategy via constructor
Provide an initial strategy to avoid null state.
Prefer injection over `new ConcreteStrategy()` inside constructor — avoids DIP violation.

### Algorithms are independent
Strategies don't know about each other.
This distinguishes Strategy from State, where states trigger transitions to each other.

## Strategy vs State
| | Strategy | State |
|---|---|---|
| Who switches | Caller/Context (consciously) | State itself (drives transition) |
| Awareness | Strategies are independent | States know each other |
| Purpose | Swap algorithm | Model lifecycle/behavior |

## Strategy vs Template Method
| | Strategy | Template Method |
|---|---|---|
| Mechanism | Composition — swap whole algorithm | Inheritance — override specific steps |
| Flexibility | Full algorithm replaced | Skeleton fixed, steps customized |

## Watch-outs
- Context instantiating `new ConcreteStrategy()` directly → mild DIP violation → inject instead
- Method names on context should be verbs (`process`, not `processor`)
- All strategies must be demonstrated in `main()` — no dead code