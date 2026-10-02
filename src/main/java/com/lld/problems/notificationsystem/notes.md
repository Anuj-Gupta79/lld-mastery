# L13 — Notification System / Logger

## Requirements

- Entry point: `sendAlert(String message, Severity severity)` on `NotificationService`. No recipient/userId — not modeling per-user preference routing.
- Severity: open enum (`LOW`, `MEDIUM`, `HIGH`), extensible for future levels (e.g. `CRITICAL`).
- Routing: severity-ladder, canonical CoR — exactly **one** handler fires per alert.
  - `LOW` → Push
  - `MEDIUM` → Email
  - `HIGH` → SMS
- Decoration: applied **after** CoR resolves the channel, by the handler itself. Mandatory, always applied, multiple (stacked) decorators per channel.
  - Push: `ShortDescriptionDecorator` (innermost, truncates to 100 chars) → `HeadingDecorator` (outermost)
  - Email: `BodyFormatDecorator` (innermost) → `SubjectDecorator` (outermost)
  - SMS: `TrimDecorator` (innermost, truncates to 150 chars) → `TagDecorator` (outermost)
- Failure: no fallback, no rollback — fails cleanly.
- Unhandled severity: chain exhausts with no match → throws `UnhandledSeverityException`.

## Deviation Log

**None.** Every design decision this session resolved to the canonical/standard form:

- CoR: canonical single-handler-stop model (explicitly considered and rejected the cumulative multi-channel variant — that's being deferred to a **future, separate logging-system case study**, not treated as equivalent here).
- Decorator: standard GoF shape — common `Notification` interface, abstract `NotificationDecorator` base holding wrapped reference, concrete decorators each adding one piece.
- Decorator stack assembly ("client" role): GoF leaves this unspecified/to the client's discretion. We chose **handler-as-client** (each handler builds its own decorator stack) because the channel is only known at runtime, post-CoR-resolution. This is a documented _choice_, not a deviation — GoF doesn't mandate who assembles the stack.

## Design Decisions Worth Remembering

1. **L12 contrast, correctly reasoned in the opposite direction.** L12: same behavior + different data → one generic parameterized handler class. L13: genuinely different behavior per channel (different delivery mechanism, different formatting, different failure mode) → separate handler classes (`PushHandler`, `EmailHandler`, `SMSHandler`). Confirms the "same behavior vs. different behavior" test is actually understood, not just pattern-matched from L12's answer.

2. **Decorator stacking order matters when one decorator truncates.** Whichever decorator adds fixed-format text that must never be mangled/truncated should be **outermost** (applied last, after any truncation has already happened). Applied consistently across all three channels once explained for `PushHandler`.

3. **Composition vs. aggregation — two new relationship instances, same lifecycle-ownership test as L12:**
   - `NotificationDecorator.wrapped : Notification` → **composition** (constructed specifically to be wrapped, no independent purpose/existence outside the decorator).
   - `NotificationHandler.next : NotificationHandler` → **aggregation** (peer-to-peer link; each handler has independent identity/lifecycle, matches L12's denomination-handler-chain precedent).
   - `NotificationService.firstHandler : NotificationHandler` → **composition** (exclusive ownership; the chain has no purpose or reference point outside this one service — initially mis-called as aggregation, self-corrected once pointed back to the L12 `CashDispenser`↔`DenominationHandler` precedent).

4. **CoR forwarding must be pure recursion, not manual traversal.** A handler should only ever reason about itself and its immediate `next` — never loop ahead or inspect the structure beyond one hop. Implementation went through two bad intermediate states before landing here (see bugs below).

## Bugs Caught This Session (all self-fixed after being flagged)

| #   | Bug                                                                                                           | Where                             | Severity                                                                                                                                                                 |
| --- | ------------------------------------------------------------------------------------------------------------- | --------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 1   | `wrapped` field typed as `NotificationDecorator` instead of `Notification`                                    | `NotificationDecorator`           | Broke substitutability — couldn't wrap `RawNotification` at all                                                                                                          |
| 2   | `RawNotification.format()` returned `""`                                                                      | `RawNotification`                 | Would've silently dropped all message content in every channel                                                                                                           |
| 3   | `NotificationDecorator` missing `implements Notification`                                                     | `NotificationDecorator`           | Broke the whole decorator chain's type compatibility                                                                                                                     |
| 4   | `TrimDecorator` used `.trim()` (whitespace strip) instead of length truncation                                | `TrimDecorator`                   | Didn't meet the 150-char SMS requirement at all                                                                                                                          |
| 5   | Unsafe `substring(0, 150)` with no length check                                                               | `TrimDecorator` (initial version) | `StringIndexOutOfBoundsException` risk on any message under 150 chars                                                                                                    |
| 6   | `handle()` threw on first mismatch instead of forwarding                                                      | `NotificationHandler`             | HIGH severity would've thrown immediately at `PushHandler`, never reaching `SMSHandler`                                                                                  |
| 7   | **Chain-mutating loop** — `this.next = this.next.getNext()` reassigned the chain permanently during traversal | `NotificationHandler`             | Most serious bug this session: would silently corrupt the chain structure for all future requests after the first forwarded one. Caught via code trace, not via running. |
| 8   | Copy-paste label — `EmailHandler.send()` printed "Sending SMS notification"                                   | `EmailHandler`                    | Cosmetic, but would've confused hand-trace verification                                                                                                                  |

Bug #7 is the standout — same category of risk as L12's stale-remainder bug and L11's toggle-logic bug: a structural/state bug that wouldn't show up on a single test call, only on repeated use. Worth keeping an eye out for this class of bug (hidden state mutation during traversal/recursion) in future chain- or tree-based case studies.

## Verification

Hand-traced before running `Main.java` (4 calls: LOW, MEDIUM, HIGH, and a deliberately short chain to force `UnhandledSeverityException`). Prediction matched actual output on first run — confirmed explicitly when asked.

## Class List (final)

- `Severity` (enum)
- `Notification` (interface) → `format(): String`
- `RawNotification implements Notification`
- `NotificationDecorator implements Notification` (abstract) → `HeadingDecorator`, `ShortDescriptionDecorator`, `SubjectDecorator`, `BodyFormatDecorator`, `TagDecorator`, `TrimDecorator`
- `NotificationHandler` (abstract) → `PushHandler`, `EmailHandler`, `SMSHandler`
- `UnhandledSeverityException extends RuntimeException`
- `NotificationService` (coordinator)
- `Main` (assistant-written: chain wiring + test calls)
