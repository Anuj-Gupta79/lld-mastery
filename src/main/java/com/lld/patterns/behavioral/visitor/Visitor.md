# P22 — Visitor Pattern

## Intent
Add new operations to a stable object hierarchy without modifying those classes.
Separates *what is done* from *who it is done to*.

## The Core Problem It Solves
Given siblings `Heading`, `Paragraph`, `Image` — adding operations (export, word count)
by modifying each class violates OCP. Every new operation touches every class.
Visitor moves operations out into separate classes.

## Double Dispatch
Java resolves method overloads at **compile time** based on declared type.
Calling `visitor.visit(element)` where `element` is `IncomeSource` → compiler can't pick the right overload.

Solution — each element implements `accept()`:
```
void accept(Visitor v) { v.visit(this); }
```
`this` is statically typed as the concrete class inside `accept()`.
Two dispatches:
1. `element.accept(visitor)` — resolves which element (SalaryIncome? RentalIncome?)
2. `v.visit(this)` inside accept — resolves which overload (visit(SalaryIncome))

## Structure
- `IncomeSource` — interface with `accept(Visitor v)`
- Concrete elements (`SalaryIncome`, `RentalIncome`, `FreelanceIncome`) — each implements `accept()`
- `Visitor` interface — one `visit()` overload per concrete element type
- Concrete visitors (`TaxCalculatorVisitor`, `IncomeSummaryVisitor`) — implement all overloads

## Key Rules
- `Visitor` interface must have one overload per concrete type — not one generic method
- No `instanceof` in visitors — if you have it, double dispatch is broken
- `accept()` exists purely for compiler type resolution, not encapsulation

## Known Weaknesses
- **Adding a new type** (e.g. `CryptoIncome`) — must touch `Visitor` interface + every concrete visitor
- Open for new **operations**, closed for new **types** — inverse of typical OCP
- **Encapsulation violation** — elements must expose internal state via public getters so visitors can operate on them

## Visitor vs Strategy
| | Visitor | Strategy |
|---|---|---|
| Scope | Behavior across a type hierarchy | Behavior for one context |
| Who switches | Element dispatches via accept | Caller swaps via setter |
| Intent | New operations without modifying types | Swappable algorithms |

## Visitor vs Iterator
- Iterator — traverses a collection, one type
- Visitor — operates on each element, many types, type-specific behavior per element

## Stateful Visitors
Accumulating state (e.g. `totalTax`) works but ties the visitor to one use.
Re-using the same instance across multiple lists accumulates incorrectly.
Options: new instance per use, or reset after `getTotalTax()`.