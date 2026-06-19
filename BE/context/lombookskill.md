# Lombok Usage Skill

## Purpose

Instruct the agent to use Project Lombok to reduce boilerplate code in Spring Boot applications while maintaining safe practices, especially with JPA entities and dependency injection.

---

# Core Guidelines

## Dependency Injection

- Always use `@RequiredArgsConstructor` for Spring bean dependency injection.
- Never use field injection with `@Autowired`.
- All injected dependencies must be declared as `private final`.
- Prefer immutable dependencies.

---

## JPA Entities

- Never use `@Data` on JPA `@Entity` classes.
- Avoid automatic generation of `equals()`, `hashCode()` and `toString()` on entities.
- Always include `@NoArgsConstructor` in entities.
- Use explicit Lombok annotations such as:
  - `@Getter`
  - `@Setter`
  - `@Builder`
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`

---

## Entity Relationships

For entity relationships:

- Always exclude relationship fields from `toString()`.
- Always exclude relationship fields from `equals()` and `hashCode()`.
- Prevent recursive calls and lazy loading issues.

Use:

- `@ToString.Exclude`
- `@EqualsAndHashCode.Exclude`

on relational fields.

---

## DTOs

- Use Lombok to simplify DTO classes.
- Prefer:
  - `@Getter`
  - `@Setter`
  - `@Builder`
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`

- Use `@Value` when immutable DTOs are required.

---

# Agent Behavior Rules

The agent must:

1. Use `@RequiredArgsConstructor` for all Spring components.
2. Use `private final` for injected dependencies.
3. Never use `@Autowired` field injection.
4. Never use `@Data` on JPA entities.
5. Always add `@NoArgsConstructor` to JPA entities.
6. Avoid generated `equals()`, `hashCode()` and `toString()` methods on entities.
7. Add `@ToString.Exclude` and `@EqualsAndHashCode.Exclude` to relationship fields.
8. Use Lombok to reduce unnecessary boilerplate.
9. Keep generated code clean, safe and maintainable.
