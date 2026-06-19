
# Spring Boot Best Practices Skill

## Purpose

This skill helps the agent generate, review, and improve Spring Boot applications following established best practices.

The agent must prioritize:

- Clean architecture
- Maintainable code
- Testability
- Security
- Separation of concerns
- Production-ready solutions

---

# Project Setup & Structure

## Build Tool

Use Maven or Gradle for dependency management.

Supported:

- Maven (`pom.xml`)
- Gradle (`build.gradle`)

---

## Spring Boot Starters

Prefer Spring Boot starters to simplify dependency management.

Examples:

```xml
spring-boot-starter-web
spring-boot-starter-data-jpa
spring-boot-starter-validation
spring-boot-starter-security
```

Avoid manually adding individual dependencies when a starter exists.

---

# Package Structure

Organize the project by feature/domain instead of technical layers.

Preferred structure:

```
com.example.app

├── user
│   ├── UserController
│   ├── UserService
│   ├── UserRepository
│   ├── UserDTO
│   └── UserEntity
│
├── order
│   ├── OrderController
│   ├── OrderService
│   ├── OrderRepository
│   ├── OrderDTO
│   └── OrderEntity
```

Avoid:

```
com.example.app

├── controller
├── service
├── repository
└── entity
```

The agent should group code by business domain.

---

# Dependency Injection & Components

## Constructor Injection

Always use constructor-based dependency injection.

Avoid:

```java
@Autowired
private UserService userService;
```

Prefer:

```java
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

Benefits:

- Dependencies are explicit
- Easier unit testing
- Supports immutability

---

## Immutable Dependencies

Dependency fields must be declared as:

```java
private final Dependency dependency;
```

Avoid mutable injected fields.

---

## Component Stereotypes

Use Spring annotations according to responsibility.

| Annotation | Responsibility |
|---|---|
| `@Component` | Generic Spring component |
| `@Service` | Business logic |
| `@Repository` | Data access |
| `@RestController` | REST API layer |

---

# Configuration

## Externalized Configuration

Store configuration outside the source code.

Preferred:

```
application.yml
```

Example:

```yaml
server:
  port: 8080

database:
  url: localhost
```

---

## Type-Safe Configuration

Use:

```java
@ConfigurationProperties
```

instead of manually reading configuration values.

Example:

```java
@ConfigurationProperties(prefix = "database")
public class DatabaseProperties {

    private String url;

}
```

---

## Spring Profiles

Use profiles for environment-specific configuration.

Examples:

```
application-dev.yml

application-test.yml

application-prod.yml
```

---

## Secrets Management

Never hardcode secrets.

Do not store:

- Passwords
- Tokens
- API keys
- Credentials

Use:

- Environment variables
- HashiCorp Vault
- AWS Secrets Manager

---

# Web Layer

## REST API Design

Create clear RESTful endpoints.

Example:

```
GET    /users
GET    /users/{id}
POST   /users
PUT    /users/{id}
DELETE /users/{id}
```

Follow consistent naming conventions.

---

# DTO Usage

Never expose JPA entities directly through APIs.

Avoid:

```java
@GetMapping
public User getUser(){
    return user;
}
```

Prefer:

```java
@GetMapping
public UserDTO getUser(){
    return userMapper.toDTO(user);
}
```

DTOs define the API contract.

---

# Validation

Use Java Bean Validation.

Example:

```java
public class UserRequest {

    @NotNull
    private String name;

    @Size(min = 8)
    private String password;

}
```

Controller:

```java
@PostMapping
public ResponseEntity<?> create(
    @Valid @RequestBody UserRequest request
){

}
```

---

# Error Handling

Implement global exception handling.

Use:

```java
@ControllerAdvice
```

and:

```java
@ExceptionHandler
```

Example:

```java
@ControllerAdvice
public class GlobalExceptionHandler {

}
```

All API errors should return consistent responses.

---

# Service Layer

## Business Logic

Business rules belong inside:

```java
@Service
```

classes.

Controllers should only:

- Receive requests
- Validate input
- Call services
- Return responses

---

## Stateless Services

Services must not maintain mutable state.

Avoid:

```java
private List<User> users;
```

inside services.

---

## Transaction Management

Use:

```java
@Transactional
```

for database operations.

Example:

```java
@Transactional
public void createOrder(Order order){

}
```

Apply transactions only where required.

---

# Data Layer

## Spring Data JPA

Repositories should extend:

```java
JpaRepository
```

or:

```java
CrudRepository
```

Example:

```java
@Repository
public interface UserRepository 
extends JpaRepository<User, Long>{

}
```

---

## Custom Queries

For complex database operations use:

- `@Query`
- JPA Criteria API
- Specifications

Example:

```java
@Query("SELECT u FROM User u WHERE u.email = :email")
User findByEmail(String email);
```

---

## Projections

Fetch only required data.

Prefer DTO projections instead of loading complete entities when possible.

Benefits:

- Better performance
- Less memory usage

---

# Logging

## SLF4J

Use SLF4J for logging.

Example:

```java
private static final Logger logger =
LoggerFactory.getLogger(MyClass.class);
```

---

## Parameterized Logging

Preferred:

```java
logger.info(
    "Processing user {}",
    userId
);
```

Avoid:

```java
logger.info(
    "Processing user " + userId
);
```

Parameterized logging improves performance.

---

# Testing

## Unit Tests

Use:

- JUnit 5
- Mockito

Example:

```java
@Test
void shouldCreateUser(){

}
```

Test:

- Services
- Components
- Business rules

---

## Integration Tests

Use:

```java
@SpringBootTest
```

when loading the complete Spring context.

---

## Test Slices

Use focused tests.

Controllers:

```java
@WebMvcTest
```

Repositories:

```java
@DataJpaTest
```

---

## Testcontainers

Use Testcontainers for realistic integration tests.

Examples:

- PostgreSQL
- MySQL
- Redis
- Kafka

---

# Security

## Spring Security

Use Spring Security for:

- Authentication
- Authorization
- Endpoint protection

---

## Password Encoding

Never store plain passwords.

Always hash passwords.

Preferred:

```java
BCryptPasswordEncoder
```

Example:

```java
passwordEncoder.encode(password);
```

---

# Input Security

## SQL Injection Prevention

Prevent SQL injection by using:

- Spring Data JPA
- Parameterized queries

Avoid manually building SQL strings.

---

## XSS Prevention

Protect against Cross-Site Scripting.

Rules:

- Validate user input
- Encode output
- Never trust external data

---

# Agent Behavior Rules

When generating Spring Boot code:

1. Always use clean architecture principles.
2. Prefer constructor injection.
3. Keep injected dependencies immutable.
4. Organize code by feature/domain.
5. Use DTOs between API and database entities.
6. Keep controllers thin.
7. Put business rules inside services.
8. Use repositories only for persistence.
9. Write tests for important behavior.
10. Use secure coding practices.
11. Never expose secrets.
12. Generate maintainable production-ready code.
````
