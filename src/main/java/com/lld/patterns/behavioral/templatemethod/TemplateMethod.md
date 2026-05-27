# P21 — Template Method

## Intent
Define the skeleton of an algorithm in a base class. Defer variant steps to subclasses. Subclasses fill in the blanks but cannot change the overall structure.

## Key Roles
- **Abstract Base Class** — owns the template method and invariant steps
- **Template Method** — `public final void` — orchestrates the sequence, cannot be overridden
- **Invariant Steps** — `protected final void` — same for all subclasses, not overridable
- **Variant Steps** — `protected abstract void` — subclasses must implement

## Key Modifiers
| Modifier | Why |
|---|---|
| `public final` on template method | Caller invokes it; subclasses cannot reorder steps |
| `protected final` on invariant steps | Visible to subclasses, not overridable |
| `protected abstract` on variant steps | Subclasses must implement; not exposed to caller |

## Structure
```
Report (abstract)
├── generateReport()        ← public final — template method
├── fetchData()             ← protected final — invariant
├── exportReport()          ← protected final — invariant
├── parseData()             ← protected abstract — variant
└── formatReport()          ← protected abstract — variant

PDFReport extends Report    ← implements parseData, formatReport
ExcelReport extends Report  ← implements parseData, formatReport
```

## Template Method vs Strategy
| | Template Method | Strategy |
|---|---|---|
| Mechanism | Inheritance | Composition |
| Algorithm | Partial — skeleton fixed, steps vary | Complete swap |
| Runtime swap | No | Yes |
| Who switches | Never — locked in hierarchy | Caller switches via setter |

## Weaknesses
- Inheritance coupling — subclasses locked to base class hierarchy
- Subclass proliferation — every new variant needs a new class
- Split readability — algorithm spread across parent and children files

## When to Use
- Multiple classes share the same algorithm structure but differ in specific steps
- You want to enforce sequence and prevent reordering
- Variant steps are well-defined and unlikely to change independently