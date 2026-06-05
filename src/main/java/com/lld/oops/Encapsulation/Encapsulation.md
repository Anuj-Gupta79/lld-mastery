# OOP — Encapsulation

## Intent
Hide internal state behind private fields. Expose only controlled operations through public methods.
Outside code interacts with behaviour, never with raw data.

## Core Concepts

### Private fields
Fields are private — no direct read or write from outside the class.
All access goes through methods that can enforce rules.

### Validation inside methods
`deposit()` and `withdraw()` guard against invalid input.
Caller says what to do — the class decides whether it's allowed.
If validation were the caller's job, every caller would need to repeat it.

### Computed getters
`getAccountStatus()` derives a value from internal state — no separate field needed.
Getter is not just a field reader; it can contain logic.

### No setters by default
`accountHolder` and `balance` have no setters — only the class mutates them.
Add a setter only when external mutation is genuinely needed.

## Why Encapsulation matters
- State stays consistent — no caller can set `balance = -9999`
- Rules live in one place — change validation once, all callers benefit
- Implementation can change — swap `double` for `BigDecimal` without touching callers

## Watch-outs
- Getter returning a mutable object (List, array) breaks encapsulation — return a copy or unmodifiable view
- Setter on every field = encapsulation in name only — think before adding setters
- Computed state belongs in a method, not a stored field that can drift out of sync