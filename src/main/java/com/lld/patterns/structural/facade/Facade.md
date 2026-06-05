# P10 — Facade Pattern

## Intent
Provide a simplified interface to a complex subsystem.
Client talks to one object; facade handles the orchestration internally.

## Core Structure
- **Subsystems** (`Lights`, `SoundSystem`, `Projector`, `StreamingPlayer`) — each does one thing
- **Facade** (`HomeTheater`) — knows the right sequence; exposes coarse-grained methods
- **Client** (`FacadeDemo`) — only calls `watchMovie()` / `endMovie()`; never touches subsystems directly

## Key Concepts

**What facade hides:**
Correct ordering of calls, which subsystems are involved, and their configuration details.
Client doesn't need to know that "watch movie" requires 7 steps across 4 objects.

**Facade doesn't prevent direct access:**
Subsystems are still public. Advanced clients can bypass the facade.
Facade is a convenience layer, not a hard barrier.

**DIP in the facade:**
Subsystems are injected via constructor, not created inside.
Facade stays testable — mock any subsystem without changing the facade.
A factory or DI container can handle wiring in production.

**Facade vs God Class:**
Facade delegates everything — it contains no business logic itself.
A God Class absorbs logic and grows. Facade stays thin.

## Facade vs Other Patterns
| | Facade | Adapter | Mediator |
|---|---|---|---|
| Purpose | Simplify a subsystem | Fix interface mismatch | Decouple peer objects |
| Knows about | Multiple subsystems | One adaptee | Multiple colleagues |
| Client impact | Fewer calls | Same calls, new interface | Indirect communication |

## When to Use
- Subsystem has many moving parts the client shouldn't care about
- Want a single entry point for a common workflow
- Reducing coupling between client and subsystem internals