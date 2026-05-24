# P17 — Memento Pattern

## Intent
Capture and restore an object's internal state without exposing its internals to outside classes.

## Roles
| Role | Class | Responsibility |
|---|---|---|
| Originator | `TextEditor` | Creates and restores from Mementos |
| Memento | `EditorMemento` | Immutable snapshot of state |
| Caretaker | `EditorHistory` | Stores and returns Mementos — never reads them |

## Key Decisions

**Immutable Memento**
No setters. Once created, state cannot be changed. Mutable Mementos corrupt history — restore gives you a tampered state, not the real one.

**Package-private getter on Memento**
`String getContent()` — no access modifier = package-private. Only classes in the same package (i.e. `TextEditor`) can call it. Caretaker cannot read contents even if it holds the object.

**Package-private class visibility matters**
`EditorMemento` has no `public` on the class declaration. If `EditorHistory` moves to a different package, the code fails to compile — `EditorMemento` becomes invisible outside its package.

**Caretaker is blind**
`EditorHistory` stores `EditorMemento` objects and pops them on demand. It never calls any method on the Memento. It is a vault, not a reader.

**Caller controls checkpoints**
`createMemento()` is separate from `type()` and `delete()`. The editor does not auto-save. The Demo decides when to snapshot — this is intentional. Save before the action you want to be able to undo from, not after.

**Stack = LIFO = undo order**
`Stack<EditorMemento>` gives correct undo ordering. Most recent save is restored first.

## Save vs Undo Mental Model
```
type → SAVE → type more → SAVE → [delete] → undo → lands on last SAVE
```
You save checkpoints you want to return to. You don't checkpoint the action you want to escape.

## Memento vs Command for Undo
| | Command | Memento |
|---|---|---|
| Stores | Actions (what was done) | State (what it looked like) |
| Undo mechanism | Execute reverse operation | Overwrite with saved snapshot |
| Reverse logic | Required | Not needed |

## Visibility Trap
`protected` = same package + subclasses. Wrong choice here.
Package-private (no modifier) = same package only. Correct choice.

## Caretaker vs Invoker
Both hold objects and support undo. But:
- Invoker knows the type — calls `.execute()` / `.undo()` on Command
- Caretaker is opaque — calls nothing on Memento, just stores and returns it

## Cleaner `restore()` Design
Current: returns `String` (mixes mutation + return)
Better: return `void`, expose `getContent()` separately on `TextEditor`