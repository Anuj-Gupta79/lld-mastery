# P7 — Bridge Pattern

## Intent
Decouple an abstraction from its implementation so the two can vary independently.
Compose them at runtime instead of locking them together via inheritance.

## Core Structure
- **Implementor interface** (`NotificationChannel`) — defines how the operation is executed
- **Concrete Implementors** (`EmailChannel`, `SMSChannel`, etc.) — actual delivery mechanisms
- **Abstraction** (`Notification`) — defines what the operation is; holds reference to Implementor
- **Refined Abstractions** (`AlertNotification`, `ReminderNotification`) — add domain behaviour on top

## Key Concepts

**The bridge:**
`Notification` holds a `NotificationChannel` field — that reference is the bridge.
Abstraction delegates the actual work to the implementor via `channel.send()`.

**Why not just subclass:**
Without Bridge, every combination needs its own class:
`AlertEmail`, `AlertSMS`, `AlertPush`, `ReminderEmail`, `ReminderSMS`...
With 2 notification types and 4 channels = 8 classes.
With Bridge = 2 + 4 = 6 classes. Gap widens as dimensions grow.

**Two independent dimensions:**
- Dimension 1 (what): notification type — Alert, Reminder, Promotional...
- Dimension 2 (how): channel — Email, SMS, Push, WhatsApp...
Adding a new channel touches zero abstraction code. Adding a new type touches zero channel code.

**Runtime flexibility:**
Channel is injected via constructor. Can be swapped at runtime if a setter is added.

## Bridge vs Adapter
| | Bridge | Adapter |
|---|---|---|
| Purpose | Designed upfront to separate dimensions | Retrofit incompatible interfaces |
| Timing | Design-time decision | Usually applied to existing code |
| Goal | Prevent class explosion | Fix interface mismatch |

## When to Use
- Two or more orthogonal dimensions of variation in a class hierarchy
- Want to avoid combinatorial subclass explosion
- Implementation should be swappable at runtime