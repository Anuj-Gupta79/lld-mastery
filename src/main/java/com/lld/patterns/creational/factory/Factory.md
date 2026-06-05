# P1 — Factory Pattern

## Intent

Centralize object creation. Caller asks for a product by type; Factory decides which concrete class to instantiate.

## Core Structure

- **Product interface** (`Report`) — what the caller works with
- **Concrete Products** (`PDFReport`, `ExcelReport`, `CSVReport`) — actual implementations
- **Factory** (`ReportFactory`) — static method takes a type string, returns the right Product

## Key Concepts

**What the caller sees:**
Only the `Report` interface. No `new PDFReport()` anywhere in client code.

**Why static method:**
No state needed on the factory itself. Static keeps the call site clean:
`ReportFactory.createReport("pdf")` reads like a request, not a construction.

**OCP tradeoff:**
Adding a new type means touching the switch — violates OCP strictly.
Fix: use a registry (`Map<String, Supplier<Report>>`). Acceptable for small, stable type sets.

**Default throws, not returns null:**
`IllegalArgumentException` on unknown type forces caller to handle bad input explicitly.
Returning null would push the failure downstream and make bugs harder to find.

## Factory vs Abstract Factory

|            | Factory          | Abstract Factory                              |
| ---------- | ---------------- | --------------------------------------------- |
| Creates    | One product type | A family of related products                  |
| Switch/map | On one dimension | On product family                             |
| Example    | Any Report type  | PDF family: PDFReport + PDFHeader + PDFFooter |

## When to Use

- Object creation logic is non-trivial or needs to be centralized
- Caller should not depend on concrete classes
- Type is determined at runtime from config, input, or context
