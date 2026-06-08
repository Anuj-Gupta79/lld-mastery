# Object Methods — toString, equals, hashCode

## What it is
Three methods inherited from `java.lang.Object` that every Java class gets by default.
The defaults are almost always wrong for real objects — so you override them.
`toString` controls how an object prints. `equals` controls what "same" means.
`hashCode` controls where the object lands in hash-based collections.

## Why it exists
Java needs a universal contract for object identity and representation.
Every class in Java descends from `Object`, so these three methods are always present.
The defaults work on memory addresses — two `Employee` objects with identical data
are "different" by default because they live at different memory locations.
That's correct for some types and completely wrong for others.

## How it helps
- `toString` makes debugging readable — you see actual data, not `Employee@6d06d69c`.
- `equals` lets you define what "same" means for your domain — same `id` + same `name` = same employee.
- `hashCode` makes your objects work correctly inside `HashSet`, `HashMap`, and anything hash-based.
- All three together give your class first-class citizenship in the Java ecosystem.

## The key ideas in practice

**`toString()`**
Called automatically whenever you print an object or concatenate it into a String.
Without override: `Employee@6d06d69c` — class name plus memory address hash, useless.
With override: `Employee Name: Alice, ID: 101, Salary: 50000` — readable, debuggable.

**`equals()`**
Default behaviour: reference equality — `emp1 == emp2`, same memory address only.
Override to define logical equality: same `name` + same `id` = same employee, regardless of salary.
`emp1` and `emp2` have different salaries but are logically the same person — override handles this.
Standard pattern: check same reference → check null and class → cast → compare fields.

**`hashCode()`**
Hash-based collections (`HashSet`, `HashMap`) use hash first to find the bucket, then `equals` to confirm.
If two objects are `equals`, they *must* return the same `hashCode` — this is the contract.
Break it and a `HashSet` stores duplicates. The collection finds the wrong bucket and never even calls `equals`.
The `31 * result` multiplier is a standard prime-based hash mix — spreads values across buckets evenly.

**The equals-hashCode contract**
`equals` true → `hashCode` must be the same. Always. No exceptions.
`hashCode` same → `equals` can still be false (hash collision is normal).
Only override one and you silently corrupt every hash-based collection that holds your objects.

**`Objects.equals(a, b)` — null safety**
`a.equals(b)` throws `NullPointerException` if `a` is null.
`Objects.equals(a, b)` handles null on either side — returns false, never throws.
Fields coming from a database or external input can legitimately be null — always use the null-safe version inside your own `equals` override.

## Notes for revision
- The rule: if you override `equals`, you must override `hashCode`. Always. The two are a pair.
- Only include fields in `hashCode` that you also use in `equals` — nothing else.
- Salary is intentionally excluded from both `equals` and `hashCode` here — same employee can have a different salary after a raise. Think about what "same" means for your domain before choosing fields.
- `HashSet` behaviour: hash locates the bucket, `equals` confirms the match. If `hashCode` is broken (two equal objects return different hashes), they land in different buckets and `equals` is never called — the set silently holds duplicates.
- `toString` is called implicitly in string concatenation and `System.out.println` — you never need to call it manually.
- `Objects.hashCode(name)` handles a null `name` safely — returns 0 instead of throwing. Use it over `name.hashCode()` when the field can be null.
- If your class is used as a `HashMap` key, a mutable field inside `hashCode` is dangerous — mutating the object after insertion moves it to a different bucket, making it permanently unretrievable.