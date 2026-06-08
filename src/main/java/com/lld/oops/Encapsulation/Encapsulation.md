# Encapsulation

## What it is

The practice of hiding a class's internal state behind private fields and exposing
only controlled operations through public methods.
Outside code never touches raw data — it calls behaviour and the class decides what happens.

## Why it exists

Without encapsulation, any caller can put the object into an illegal state.
`account.balance = -9999` compiles and runs — nothing stops it.
Encapsulation moves the guard inside the class, where it runs every single time,
for every caller, forever — instead of hoping every caller remembers to validate.

## How it helps

- State stays consistent — the class is the only thing that writes to its own fields.
- Rules live in one place — change the validation logic once, all callers get it automatically.
- Implementation can change freely — swap `double` for `BigDecimal` internally, callers see nothing different.
- Callers think in terms of what they want to do, not how the data is stored.

## The key ideas in practice

**Private fields**
`balance` and `accountHolder` are private — no direct read or write from outside.
Every access goes through a method that can enforce rules or compute a result.

**Validation inside methods**
`deposit()` rejects negative amounts. `withdraw()` rejects overdrafts.
The caller says _what_ to do — the class decides _whether_ it's allowed.
If validation were the caller's job, every caller would need to repeat it, and one will forget.

**Computed getters**
`getAccountStatus()` derives a label from `balance` — no separate field stored.
A getter doesn't have to just return a field; it can contain logic.
Computed state in a method never drifts out of sync with the real data.

**No setters by default**
Neither `balance` nor `accountHolder` has a setter — only the class mutates them.
Add a setter only when external mutation is genuinely required. Default is: don't.

## Notes for revision

- Encapsulation is not about getters and setters. It's about keeping the rules where the data lives.
- A class with a getter and setter on every field is encapsulation in name only — the field might as well be public.
- Getter returning a mutable object (`List`, array) breaks encapsulation silently — the caller can modify the internal collection. Return a copy or `Collections.unmodifiableList(...)`.
- If you find yourself putting validation in the caller, stop — move it into the class.
- The test: can a caller ever put the object into a state the class wouldn't allow? If yes, encapsulation is incomplete.
