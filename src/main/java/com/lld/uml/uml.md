# UML for LLD — Notes

## What is UML?

- Unified Modeling Language — a universal, language-agnostic notation for software design
- Blueprint for software — draw the design before writing code
- Two diagrams matter for LLD: **Class Diagram** and **Sequence Diagram**

---

## Class Diagram

### What it shows

- Fields and methods inside each class
- Relationships between classes

### Visibility Modifiers

| Symbol | Meaning   |
| ------ | --------- |
| `+`    | public    |
| `-`    | private   |
| `#`    | protected |

### Class vs Interface syntax

```plantuml
interface MyInterface {
    +method(): void
}

class MyClass {
    -field: Type
    +method(): void
}
```

### 4 Relationship Types

| Relationship                | Meaning                      | Arrow | Memory tip     |                               |
| --------------------------- | ---------------------------- | ----- | -------------- | ----------------------------- |
| Inheritance (extends)       | is-a                         | `--   | >`             | solid line + hollow triangle  |
| Implementation (implements) | is-a                         | `..   | >`             | dotted line + hollow triangle |
| Composition                 | owns-a, lifecycle tied       | `*--` | filled diamond |                               |
| Aggregation                 | has-a, lifecycle independent | `o--` | hollow diamond |                               |
| Association                 | uses-a, temporary            | `-->` | plain arrow    |                               |

### Key Rules

- Triangle always points to parent
- Dotted line = interface, Solid line = abstract/class
- Diamond on the owner side
- No labels needed on standard relationships — arrow type is self-explanatory
- Field = at least Aggregation | Method parameter = Association

---

## Sequence Diagram

### What it shows

- Which object calls which, in what order, at runtime
- Behavior over time — not structure

### Core Syntax

```plantuml
participant Client
participant Service

Client -> Service : methodCall()
Service -> Service : internalMethod()   ' self-call
Service --> Client : return value       ' dotted = return
```

### Key Constructs

| Syntax   | What it shows               |
| -------- | --------------------------- |
| `->`     | method call                 |
| `-->`    | return value                |
| `A -> A` | self-call (internal method) |
| `loop`   | repeating calls             |
| `alt`    | conditional / if-else flow  |

### loop and alt (for future use)

```plantuml
loop for each observer
    Subject -> Observer : update()
end

alt payment success
    Service -> Client : confirm()
else payment failure
    Service -> Client : decline()
end
```

---

## Workflow for LLD Case Studies

1. Read problem statement
2. Identify classes and relationships → draw Class Diagram
3. Identify key flows → draw Sequence Diagram
4. Then write code
