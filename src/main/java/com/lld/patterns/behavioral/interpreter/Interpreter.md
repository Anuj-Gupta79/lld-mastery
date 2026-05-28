# P23 — Interpreter Pattern

## Intent
Map each rule in a grammar to a class. Evaluate sentences by calling `interpret()` on a tree of those objects.

## Core Structure
- **Expression** — interface with `boolean interpret(Context ctx)`
- **Terminal Expression** — leaf node, directly evaluates a value (e.g. `GreaterThan`, `Equals`)
- **Non-Terminal Expression** — composite node, holds other expressions and delegates (e.g. `AndExpression`, `OrExpression`)
- **Context** — carries runtime data (e.g. `Map<String, Object>`) that terminal nodes look up

## Key Concepts

**Tree building:**
Operator precedence determines tree structure. The operation that acts last is the root. Parentheses control grouping, not node creation.

**Why Non-Terminals hold `Expression` interface, not concrete types:**
So any expression — including another `And`/`Or` — can sit on either side. Makes the tree infinitely composable.

**Context role:**
Passed down the tree unchanged. Terminal nodes pull their specific key from it. Non-terminals never touch it directly — just pass it along.

**Cast in GreaterThan:**
`getValue()` returns `Object`. Cast to `(Integer)` before comparison — Java auto-unboxes for `>`.

## Interpreter vs Composite
| | Composite | Interpreter |
|---|---|---|
| Structure | Identical tree | Identical tree |
| Intent | Structural traversal | Behavioral evaluation |
| Example | Dir/file tree | Rule engine |

## Interpreter vs Visitor
- Visitor: operates across a type hierarchy, one operation per concrete type
- Interpreter: evaluates a grammar, one class per grammar rule

## Weaknesses at Scale
1. **Class explosion** — every new grammar rule = new class. 50 rules = 50 classes to write, test, maintain.
2. **No built-in caching** — repeated sub-expressions re-evaluated on every `interpret()` call. Fix: memoize results against Context.
3. Real systems use a proper **Parser** to build the tree from raw strings + an AST for evaluation.

## Who Builds the Tree
In production: a **Parser**. It tokenizes the raw string (`"age > 18 AND country == IN"`), applies grammar rules, and constructs the expression tree. Interpreter only handles evaluation — parsing is a separate concern.