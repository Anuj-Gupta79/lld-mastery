# P8 — Composite Pattern

## Intent
Compose objects into tree structures to represent part-whole hierarchies.
Client treats a single object and a composition of objects identically.

## Core Structure
- **Component interface** (`FileSystemComponent`) — common contract for all nodes
- **Leaf** (`File`) — no children; implements operations directly
- **Composite** (`Folder`) — holds children, implements operations by delegating to them
- **Client** — works only with `FileSystemComponent`; doesn't care if it's a File or Folder

## Key Concepts

**Uniform treatment:**
`rootFolder.getSize()` and `resume.getSize()` are the same call to the client.
The tree handles its own recursion — client never writes a loop or instanceof check.

**Recursion falls out naturally:**
`Folder.getSize()` calls `component.getSize()` on each child.
If the child is a Folder, it recurses. If it's a File, it returns directly.
Depth is irrelevant — the interface makes it work at every level.

**Why declare as Folder during construction:**
`addComponent()` is only on `Folder`, not on `FileSystemComponent`.
During setup you need the concrete type. After construction, pass it as
`FileSystemComponent` so the rest of the code stays decoupled.

**Leaf vs Composite responsibility:**
Leaf implements operations directly (just return its own value).
Composite delegates to children and aggregates results.
Neither knows what the other is.

## Composite vs Interpreter
Both use identical tree structures — the difference is intent:
| | Composite | Interpreter |
|---|---|---|
| Goal | Structural traversal | Behavioral evaluation |
| Example | File/folder size, display | Rule engine, expression eval |

## When to Use
- Data is naturally hierarchical (file system, org chart, UI component tree, DOM)
- Client should treat leaf nodes and container nodes the same way
- Operations need to propagate recursively through the hierarchy