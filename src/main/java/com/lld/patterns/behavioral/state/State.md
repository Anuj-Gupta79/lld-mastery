# P19 — State Pattern

## Intent
Allow an object to alter its behavior when its internal state changes. The object will appear to change its class.

## Players
- **State interface** — declares all state-dependent methods; sets the contract every concrete state must fulfill
- **Concrete State** — implements behavior for its state; handles valid actions, rejects invalid ones; owns the transition decision
- **Context** — holds reference to current state; forwards every method call to it; exposes `setState()` as package-private

## Key Concepts

### Who owns the transition?
Concrete State — not the Context. The current state knows what it just did and what comes next.

### Why package-private `setState()`?
Only Concrete States should drive transitions. Outside code must not force a state change. Package-private restricts access to same-package classes only.

### Chicken-and-egg on construction
Context must be created first (with `this`), then initial state is set inside the constructor. State objects receive the context reference via constructor.

### State vs giant if/else
Adding a new state = one new class + touch only the state(s) that transition into it. Open/Closed principle — existing states don't change.

### itemCount lives on Context, decision lives on State
Context holds data. State reads it via getter and decides the transition. Context never decides.

## Pitfalls
- New state object created on every transition → GC pressure at scale. Fix: pre-create all state instances in Context constructor, reuse via Singleton-style fields.
- `setState()` made public → outside code can corrupt state machine. Keep package-private.
- Context initializes with `itemCount = 0` → starts in wrong state. Always initialize with real data.

## Transition Map (Vending Machine)
```
Idle ──insertMoney()──► HasMoney
HasMoney ──selectItem()──► Dispensing
HasMoney ──refund()──► HasMoney (stays, money returned)
Dispensing ──dispense()[items>0]──► Idle
Dispensing ──dispense()[items==0]──► OutOfStocks
OutOfStocks ──refund()──► HasMoney
```