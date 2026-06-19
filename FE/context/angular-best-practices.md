# Angular Frontend Best Practices Skill

## Purpose

Enforce modern Angular (16+) architecture using:

- Standalone Components
- Signals for state management
- Strict RxJS patterns
- Tailwind CSS
- Clean Architecture principles

---

# Project Setup & Architecture

## Component Architecture

- Always generate Standalone Components.
- Always use:

  - `standalone: true`

- Never generate or use:

  - NgModules

---

## Folder Structure

Organize code strictly by feature/domain.

Example domains:

- `features/user`
- `features/product`

Rules:

- Keep components, services, models, and interfaces of the same domain together.
- Avoid organizing by technical layers.

---

## Routing

- Use SPA routing with `RouterOutlet`.
- Always lazy-load routes.
- Use `loadComponent` with standalone components.

---

# State Management & Reactivity

## Signals for State

Use Angular Signals for:

- Local component state.
- Synchronous UI data binding.

Preferred:

- `signal`
- `computed`
- `effect`

---

## RxJS for Async Operations

Use RxJS for asynchronous streams:

- HTTP requests
- Router events
- External data streams

---

## Observable Naming Convention

Always append `$` suffix to Observable variables.

Examples:

- `userData$`
- `searchTerms$`

Do not use `$` suffix for Signals.

---

## Memory Leak Prevention

Always use:

- `takeUntilDestroyed()`

for Observable subscriptions.

Rules:

- Never use `ngOnDestroy` only for unsubscribing.
- Use Angular lifecycle-safe subscription handling.

---

# Data Fetching & Strict Typing

## DTO Interfaces

Always define TypeScript interfaces for:

- HTTP requests
- HTTP responses
- API contracts

Never use:

- `any`

---

## Service Encapsulation

Rules:

- Inject `HttpClient` only inside services.
- Services must use:

  - `@Injectable({ providedIn: 'root' })`

Never inject `HttpClient` directly into components.

---

## RxJS Data Processing

Use RxJS operators inside services.

Preferred operators:

- `map`
- `catchError`
- `switchMap`

Services should expose processed data to components.

---

# Component Communication

## Inputs and Outputs

Use:

- `@Input()`
- `@Output()`
- `EventEmitter`

Rules:

- Follow standard Angular communication patterns.
- Avoid custom alias names.

---

## Shared Global State

For shared state between independent components:

- Use injectable services.
- Store shared state using Signals.

---

# Forms & Validation

## Reactive Forms

Always use Reactive Forms.

Use:

- `FormBuilder`
- `FormGroup`
- `FormControl`

Never use:

- Template-driven forms
- `ngModel`

---

## Validators

Implement:

- Synchronous validators.
- Asynchronous validators.

Rules:

- Define validation logic inside TypeScript classes.
- Keep templates clean.

---

# Styling & HTML

## Tailwind CSS

Use Tailwind CSS utility classes directly in templates.

Rules:

- Avoid custom CSS/SCSS files.
- Only write custom styles for:
  - Complex animations
  - Required overrides

---

## Directives and Pipes

Use Angular template features.

For Angular 17+:

- `@if`
- `@for`
- `@switch`

For older Angular versions:

- `*ngIf`
- `*ngFor`

Use pipes for template data transformation.

---

# Agent Behavior Rules

The agent must:

1. Generate only standalone components.
2. Never use NgModules.
3. Organize files by feature/domain.
4. Use Signals for UI state.
5. Use RxJS for asynchronous operations.
6. Always append `$` to Observable names.
7. Never append `$` to Signal variables.
8. Always use `takeUntilDestroyed()` for subscriptions.
9. Never use `any`.
10. Define TypeScript interfaces for all API contracts.
11. Inject HttpClient only inside services.
12. Use Reactive Forms for user input.
13. Use Tailwind CSS for styling.
14. Generate clean, maintainable, optimized Angular code.
