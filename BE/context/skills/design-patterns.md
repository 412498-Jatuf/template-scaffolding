# design-patterns

Apply patterns **only when they add architectural value**. No overengineering. Classes live where logic needs them, no fixed package. Spring `List<Interface>` injection over static `Map<>`.

## Triggers

| Pattern | Use when | Don't when |
|---|---|---|
| **OCP** | Business rules could vary or grow (new algorithms, rates, discounts) | Simple CRUD, no variable logic |
| Strategy | Multiple algorithms for 1 op | One algorithm, trivial if/else |
| Factory | Instantiation from dynamic input | `new` or `@Component` works |
| State | Behavior varies by status (PENDING→PAID) | Single flag, no behavioral change |
| Registry | Central lookup of strategies | Spring DI already covers it |
| Chain of Resp. | Sequential filters/handlers | One handler, order irrelevant |

## Strategy + Registry + Factory

```java
public interface Strategy { void execute(); boolean supports(String key); }

@Component public class ConcreteA implements Strategy {
    public void execute() { /* logic */ }
    public boolean supports(String key) { return "A".equalsIgnoreCase(key); }
}

@Component @RequiredArgsConstructor
public class Factory {
    private final List<Strategy> strategies;
    public Strategy get(String key) { return strategies.stream().filter(s -> s.supports(key)).findFirst().orElseThrow(); }
}
```

## State

```java
public interface State { void next(Context ctx); String status(); }

public class PendingState implements State {
    public void next(Context ctx) { ctx.setState(new PaidState()); }
    public String status() { return "PENDING"; }
}

public class Context {
    private State state;
    public Context(State s) { this.state = s; }
    public void setState(State s) { this.state = s; }
    public void next() { state.next(this); }
}
```

## Chain of Responsibility

```java
public abstract class Handler { protected Handler next;
    public Handler setNext(Handler h) { this.next = h; return h; }
    public abstract void process(Request req); }

public class AuthHandler extends Handler {
    public void process(Request req) { if (!req.isAuthenticated()) throw new SecurityException(); if (next != null) next.process(req); }
}

public class ValidationHandler extends Handler {
    public void process(Request req) { if (req.getData() == null) throw new IllegalArgumentException(); if (next != null) next.process(req); }
}

// Chain manually where used:
// Handler chain = new AuthHandler();
// chain.setNext(new ValidationHandler());
// chain.process(request);
```

## Rules
- Strategy + Factory + `List<>` is the most common combo in Spring Boot exams
- One status variable → no pattern. Behavior varies → State
- **SOLID**: interface Service → ServiceImpl (DIP), `List<Interface>` over if/else (OCP), one responsibility per class (SRP)
