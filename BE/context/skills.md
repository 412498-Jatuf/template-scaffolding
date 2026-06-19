# Spring Boot Best Practices Skill
## Purpose
Enforce clean architecture, maintainability, high testability, and separation of concerns in Spring Boot applications.

---

# Project Setup & Package Structure

## Build Tool

- Use Maven (`pom.xml`) for dependency management.

## Starters

- Always use official Spring Boot starters.
- Avoid manual dependency management when possible.
- Prevent dependency version conflicts.

## Package Structure

- Organize code strictly by feature/domain.
- Example domains:
  - `com.example.app.user`
  - `com.example.app.order`

- Never organize by technical layers:
  - controller
  - service
  - repository

---

# Dependency Injection & Components

## Constructor Injection

- Always use constructor-based injection.
- Never use `@Autowired` field injection.

## Immutability

- All injected dependencies must be declared as:
  - `private final`

## Spring Stereotypes

Use annotations according to responsibility:

- `@RestController` → REST API layer
- `@Service` → Business logic
- `@Repository` → Data access

---

# Configuration & Secrets

## Externalized Configuration

- Use `application.yml` for application configuration.
- Prefer hierarchical and readable configuration.

## Type-Safe Properties

- Use `@ConfigurationProperties`.
- Avoid manually reading environment variables.

## Profiles

Separate configurations by environment:

- `application-dev.yml`
- `application-test.yml`
- `application-prod.yml`

## Secrets Management

- Never hardcode credentials.
- Use environment variables or secret management tools.

---

# Web & Service Layers

## REST API Design

- Follow consistent RESTful naming conventions.
- Use correct HTTP methods.

## DTO Usage

- Never expose JPA entities directly through APIs.
- Always use DTOs as API contracts.
- Map entities to DTOs.

## Validation

- Validate incoming data using:
  - Java Bean Validation annotations
  - `@Valid`

## Global Error Handling

- Implement centralized exception handling using:
  - `@ControllerAdvice`
  - `@ExceptionHandler`

- Ensure consistent API error responses.

## Business Logic

- Keep business rules inside stateless `@Service` classes.
- Controllers must remain thin.
- Controllers should only handle:
  - Requests
  - Validation
  - Service calls
  - Responses

## Transaction Management

- Use `@Transactional` only in service methods that require transaction boundaries.

---

# Data Layer & Testing

## Spring Data JPA

- Use:
  - `JpaRepository`
  - `CrudRepository`

for database access.

## Queries & Projections

- Use `@Query` for custom database operations.
- Prefer DTO projections.
- Fetch only required data.

---

# Testing

## Unit Testing

- Use:
  - JUnit 5
  - Mockito

- Focus tests on:
  - Services
  - Business logic
  - Components

- Maintain high test coverage.

## Slice Testing

Use:

- `@WebMvcTest` for controllers.
- `@DataJpaTest` for repositories.

## Integration Testing

- Use `@SpringBootTest` only for full application context tests.

---

# Agent Behavior Rules

The agent must:

1. Organize code by business domain/feature.
2. Enforce constructor injection.
3. Keep dependencies immutable using `private final`.
4. Keep controllers thin.
5. Isolate business logic inside stateless services.
6. Always use DTOs between API and domain entities.
7. Never return JPA entities directly.
8. Implement global exception handling.
9. Validate request payloads.
10. Write focused tests using JUnit 5 and Mockito.
11. Generate clean, maintainable, production-ready Spring Boot code.
