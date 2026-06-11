# L1 — Parking Lot System

## Problem Scope

A parking lot system that handles vehicle entry and exit, slot allocation by vehicle type,
fee calculation on exit, and slot release after payment.

**In scope:** entry/exit flow, slot allocation, fee calculation, payment, receipt  
**Out of scope:** payment gateways, discounts, adding/removing slots at runtime

---

## Core Use Cases

1. Vehicle entry — allocate slot, generate ticket
2. Slot allocation — match vehicle type to slot type
3. Fee calculation — based on duration and slot type
4. Vehicle exit — calculate fee, take payment, release slot
5. Availability tracking — always know which slots are free by type

---

## Entities

| Entity                 | Role                                                |
| ---------------------- | --------------------------------------------------- |
| ParkingLot             | Orchestrator — owns all slots, handles entry/exit   |
| ParkingSlot            | Individual slot — has type and occupied state       |
| Vehicle                | Data carrier — plate number, vehicle type           |
| Ticket                 | Entry record — holds slot, vehicle, entry time      |
| Payment                | Calculates fee from ticket, returns Receipt         |
| Receipt                | Exit record — fee, ticket reference, payment status |
| SlotAssignmentStrategy | Algorithm for finding best available slot           |
| ParkingSlotFactory     | Creates correct slot subclass from type string      |
| VehicleFactory         | Creates correct vehicle subclass from type string   |

---

## Patterns Used

### Strategy — Slot Assignment

**Why:** The algorithm for finding the best slot is swappable at runtime.
Tomorrow the business may want nearest-to-elevator instead of nearest-to-entrance.
Each algorithm gets its own class — no touching existing code when adding new ones. OCP preserved.

**Where:** `SlotAssignment` interface, `NearestToEntranceStrategy`, `NearestToExitStrategy`

**Context:** `ParkingLot` holds the strategy via aggregation and calls `assign(vehicle)` at entry.

### State — Slot Occupancy

**Why:** A ParkingSlot behaves differently based on whether it is free or occupied.
Same object, different behavior based on state.

**Where:** `SlotState` interface with `isOccupied(): boolean`, implemented by `ParkingSlot`

### Factory — Vehicle and Slot Creation

**Why:** Caller should not need to know which concrete class to instantiate.
VehicleFactory takes a type string and returns the right Vehicle subclass.
ParkingSlotFactory does the same for slots. Creation logic is centralized.

**Where:** `VehicleFactory.createVehicle(type)`, `ParkingSlotFactory.createSlot(type)`

---

## Class Diagram

```mermaid
classDiagram
    class ParkingLot {
        -slots: Map~SlotType, List~ParkingSlot~~
        -parkingSlotFactory: ParkingSlotFactory
        -vehicleFactory: VehicleFactory
        +entry(vehicleType: String, platNumber: int): Ticket
        +exit(ticket: Ticket): Receipt
    }

    class ParkingSlot {
        <<abstract>>
        -slotNumber: int
        +isOccupied(): boolean
    }

    class Vehicle {
        <<abstract>>
        -platNumber: int
        -ownerName: String
    }

    class Ticket {
        -slot: ParkingSlot
        -vehicle: Vehicle
        -entryTime: DateTime
    }

    class Payment {
        +calculateFee(ticket: Ticket): Receipt
    }

    class Receipt {
        -fee: double
        -ticket: Ticket
        -paymentStatus: boolean
    }

    class SlotAssignment {
        <<interface>>
        +assign(vehicle: Vehicle): ParkingSlot
    }

    class SlotState {
        <<interface>>
        +isOccupied(): boolean
    }

    class ParkingSlotFactory {
        +createSlot(type: String): ParkingSlot
    }

    class VehicleFactory {
        +createVehicle(type: String): Vehicle
    }

    class NearestToEntranceStrategy
    class NearestToExitStrategy

    class SmallParkingSlot
    class MediumParkingSlot
    class LargeParkingSlot

    class Bike
    class Car
    class Truck

    class SlotType {
        <<enum>>
        SMALL
        MEDIUM
        LARGE
    }

    class VehicleType {
        <<enum>>
        BIKE
        CAR
        TRUCK
    }

    ParkingLot *-- ParkingSlot
    ParkingLot o-- SlotAssignment
    ParkingLot --> ParkingSlotFactory
    ParkingLot --> VehicleFactory
    ParkingLot --> Payment

    ParkingSlot ..|> SlotState
    ParkingSlot --> SlotType

    SmallParkingSlot --|> ParkingSlot
    MediumParkingSlot --|> ParkingSlot
    LargeParkingSlot --|> ParkingSlot

    Vehicle --> VehicleType
    Bike --|> Vehicle
    Car --|> Vehicle
    Truck --|> Vehicle

    NearestToEntranceStrategy ..|> SlotAssignment
    NearestToExitStrategy ..|> SlotAssignment

    Ticket --> ParkingSlot
    Ticket --> Vehicle
    Payment --> Ticket
    Payment --> Receipt
```

---

## Sequence Diagram — Entry Flow

```mermaid
sequenceDiagram
    participant Main
    participant ParkingLot
    participant VehicleFactory
    participant SlotAssignment

    Main->>ParkingLot: entry(vehicleType, plateNumber)
    ParkingLot->>VehicleFactory: createVehicle(vehicleType)
    VehicleFactory-->>ParkingLot: Vehicle
    ParkingLot->>SlotAssignment: assign(vehicle)
    SlotAssignment-->>ParkingLot: ParkingSlot
    ParkingLot->>ParkingLot: createTicket(parkingSlot, vehicle)
    ParkingLot-->>Main: Ticket
```

---

## Sequence Diagram — Exit Flow

```mermaid
sequenceDiagram
    participant Main
    participant ParkingLot
    participant Payment

    Main->>ParkingLot: exit(ticket)
    ParkingLot->>Payment: calculateFee(ticket)
    Payment-->>ParkingLot: Receipt
    ParkingLot->>ParkingLot: releaseSlot(ticket)
    ParkingLot-->>Main: Receipt
```

---

## Key Design Decisions

- `ParkingLot` uses `Map<SlotType, List<ParkingSlot>>` — initialized at startup for O(1) slot lookup by type
- `Ticket` holds object references to `ParkingSlot` and `Vehicle` — not just IDs
- `SlotAssignment` is aggregated by `ParkingLot` — strategy is independent, reusable, swappable
- `Payment` returns `Receipt` — not just a double — receipt carries fee + ticket + status
- `releaseSlot()` called after payment — never free a slot before payment succeeds
- Factories called by `ParkingLot`, not `Main` — creation is an implementation detail
- Vehicle and ParkingSlot are abstract classes, not interfaces — they carry shared fields
