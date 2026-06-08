# Single Responsibility Principle (SRP)

## What it is

The Single Responsibility Principle states:

> A class should have only one reason to change.

The keyword here is **reason**.

A class may contain many methods, but all of them should serve one responsibility.

In this example:

- `Employee` manages employee data.
- `SalaryCalculator` calculates salary.
- `EmployeeReport` generates reports.
- `EmployeeRepository` handles database operations.

Each class owns one responsibility.

---

## Why it exists

Imagine the original `Employee` class contained:

```java
calculateSalary()
generateReport()
saveToDatabase()
```

Now different teams make changes:

- Finance changes salary rules.
- Business changes report format.
- Database team changes persistence logic.

All three modifications require touching the same class.

This creates:

- Higher risk of bugs
- Difficult testing
- Frequent merge conflicts
- Poor maintainability

SRP separates these concerns so changes remain isolated.

---

## How it helps

- Smaller and cleaner classes.
- Easier unit testing.
- Lower coupling between unrelated features.
- Reduced risk when requirements change.
- Better code readability.
- Easier collaboration among teams.

When responsibilities are separated, a change in one area is less likely to break another.

---

## The key ideas in practice

### Employee owns data

```java
class Employee
```

The responsibility of this class is simply:

```text
Store employee information
```

It knows:

- id
- name
- salary
- hours worked

It should not know:

- how salary is calculated
- how reports are generated
- how data is saved

---

### Salary calculation moved out

```java
class SalaryCalculator
```

```java
calculateSalary(employee)
```

If tomorrow payroll rules change:

```java
Overtime = 2500
```

Only this class changes.

Nothing else is affected.

---

### Reporting moved out

```java
class EmployeeReport
```

If the report format changes:

```text
PDF
Excel
HTML
```

Only this class changes.

The Employee class remains untouched.

---

### Persistence moved out

```java
class EmployeeRepository
```

Today:

```text
MySQL
```

Tomorrow:

```text
MongoDB
PostgreSQL
Oracle
```

Database-related changes stay inside the repository layer.

---

### Identifying SRP violations

A simple question:

> Can different people request different changes to this class?

If the answer is yes, the class probably has multiple responsibilities.

Example:

```java
Employee
```

Requests may come from:

- Finance Team
- Reporting Team
- Database Team

That is usually a strong sign of SRP violation.

---

## Common interview questions

### Does SRP mean a class should have only one method?

No.

A class can have many methods.

The rule is:

> All methods should contribute to the same responsibility.

---

### What does "one reason to change" actually mean?

A class should change because of one business concern.

Not because of multiple unrelated concerns.

---

### Is SRP only for classes?

No.

The idea can be applied to:

- Classes
- Methods
- Modules
- Services
- Microservices

---

### How do I identify an SRP violation?

Look for classes that:

- Handle business logic
- Generate reports
- Send emails
- Access databases

all at the same time.

These are multiple responsibilities mixed together.

---

### Is creating many small classes a drawback?

Sometimes.

Over-applying SRP can create excessive abstraction.

The goal is not maximum classes.

The goal is clear responsibilities.

---

## Notes for revision

- SRP = Single Responsibility Principle.
- A class should have only one reason to change.
- Focus on "reason to change", not "number of methods".
- Employee should store employee data only.
- Salary calculation belongs in SalaryCalculator.
- Report generation belongs in EmployeeReport.
- Database operations belong in EmployeeRepository.
- Different teams requesting changes to the same class is often a sign of SRP violation.
- SRP reduces coupling and improves maintainability.
- Easier testing is a natural benefit of SRP.
- Interview memory trick:
  
  **"One class, one responsibility, one reason to change."**