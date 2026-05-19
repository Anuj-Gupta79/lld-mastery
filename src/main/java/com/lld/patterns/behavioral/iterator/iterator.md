# P15 — Iterator Pattern

## Intent
Provide a way to sequentially access elements of a collection without exposing its internal structure.

---

## The Problem
If callers iterate collections directly, they know the internal structure. Change `List` → `Map`, the caller breaks. Iterator hides structure behind a stable cursor interface.

**Real-world example:** You start with `List<Integer>` for number frequencies. New requirement — strings come in, so you switch to `Map<String, Integer>`. Callers who iterated the list directly all break. With Iterator, they feel nothing.

---

## Structure

```
«interface» Iterable<T>          «interface» Iterator<T>
  + iterator() → Iterator<T>       + hasNext() → boolean
                                    + next() → T
        ↓                                  ↓
  ConcreteCollection   ——creates——   ConcreteIterator
  (Playlist)                         (PlaylistIterator)
```

---

## Key Concepts

**Iterable vs Iterator**
- `Iterable` owns the data. `Iterator` owns the cursor.
- `Iterable` has one method: `iterator()`. `Iterator` has `hasNext()` and `next()`.
- One collection, many iterators — each with its own independent cursor.

**New instance per call**
`iterator()` must return a new iterator every time. Two callers on the same collection get two independent cursors — they don't interfere.

**Cursor lives on the iterator, not the collection**
If the cursor is a field on the collection, two simultaneous callers corrupt each other's traversal.

**hasNext() guards the boundary**
Prevents going out of bounds. `next()` should also throw `NoSuchElementException` internally when `!hasNext()` — don't rely on the caller to always check.

**Collection owns its data**
Caller should never pass a list in from outside — that leaks the internal structure. Collection initializes its own list; caller uses `addSong()`.

**for-each works free**
Any class implementing `Iterable<T>` qualifies for Java's for-each loop. Java internally calls `iterator()` and drives `hasNext()` / `next()`.

---

## Mistakes to Avoid

| Mistake | Why it breaks |
|---|---|
| Cursor stored on the collection | Two simultaneous iterators corrupt each other |
| `while (!hasNext())` | Inverted condition — loop never runs |
| Passing list in via constructor | Caller owns the list, can mutate it bypassing the collection |
| No guard in `next()` | Silent `IndexOutOfBoundsException` instead of clear contract violation |
| Single shared iterator instance | Defeats the whole point — no independent traversal |

---

## When to Use
- Collection internals must stay hidden from callers
- Multiple simultaneous traversals needed on the same collection
- Collection type may change in future (array → list → map)
- Want Java for-each support

---