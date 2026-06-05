# P2 — Abstract Factory Pattern

## Intent
Create families of related objects together, without specifying their concrete classes.
One factory = one consistent product family.

## Core Structure
- **Abstract Products** (`Button`, `Checkbox`) — what the client uses
- **Abstract Factory** (`UIComponentFactory`) — declares creation methods for each product in the family
- **Concrete Factories** (`WindowsUIFactory`, `MacOSUIFactory`) — each produces one consistent family
- **Concrete Products** (`WindowsButton`, `MacOSButton`, etc.) — actual implementations

## Key Concepts

**What "family" means:**
A factory doesn't create just one product — it creates a set of products guaranteed
to work together. `WindowsUIFactory` always returns Windows-flavored components.
Mixing factories would produce inconsistent UI.

**Client isolation:**
`renderUI(UIComponentFactory factory)` never calls `new WindowsButton()`.
It only calls `factory.createButton()` — concrete classes are invisible to the client.

**Switching families:**
Pass a different factory. Zero changes to `renderUI`. This is LSP in action —
any `UIComponentFactory` implementation is substitutable.

**OCP tradeoff:**
Adding a new OS family = new factory + new product classes. No existing code changes. ✅
Adding a new product type (e.g. `TextField`) = change the factory interface + all concrete factories. ❌

## Abstract Factory vs Factory
| | Factory | Abstract Factory |
|---|---|---|
| Creates | One product type | A family of related products |
| # of creation methods | One | One per product in the family |
| Example | Any Report type | Windows: Button + Checkbox + TextField |

## When to Use
- System must be independent of how its products are created
- Products come in families that must be used together
- You want to enforce consistency across a product family