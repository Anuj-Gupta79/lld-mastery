# P5 — Singleton Pattern

## Intent
Ensure a class has exactly one instance. Provide a global access point to it.

## Core Structure
- **Private constructor** — prevents `new ClassName()` from outside
- **Static instance field** — holds the single instance
- **Static `getInstance()`** — only entry point; creates or returns the instance

## Three Variants

### Variant 1 — Eager Initialization
Instance created at class load time.
- ✅ Thread-safe (JVM class loading is synchronized)
- ✅ Simplest implementation
- ❌ Instance created even if never used — wastes memory for heavy objects

### Variant 2 — Lazy Initialization
Instance created only on first `getInstance()` call.
- ✅ Memory efficient
- ❌ Not thread-safe — two threads can both see `null` and each create an instance

### Variant 3 — Double-Checked Locking
Lazy + thread-safe. Checks null twice: once without lock, once inside `synchronized`.
- ✅ Lazy + thread-safe
- ✅ Lock acquired only once (first creation); subsequent calls skip `synchronized`
- ⚠️ `volatile` is mandatory — without it, CPU cache may return a partially constructed instance

## Why volatile Matters in Variant 3
Object creation is not atomic. JVM can: allocate memory → assign reference → then initialize fields.
Without `volatile`, another thread may see a non-null reference but an uninitialized object.
`volatile` forces write to main memory immediately, preventing this reordering.

## When to Use Singleton
- Shared resource with exactly one logical instance: DB connection pool, config manager, logger
- Global state that must be consistent across the app

## Tradeoffs
- Hard to unit test — global state bleeds across tests; mocking requires extra effort
- Hidden coupling — any class can call `getInstance()`, making dependencies invisible
- Prefer dependency injection over Singleton where testability matters