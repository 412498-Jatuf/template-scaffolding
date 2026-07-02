# add-domain

Add a **new domain** (e.g., `producto`). Singular PascalCase for classes, plural lowercase for API paths.

## Files (13 per domain)

| # | File | Notes |
|---|------|-------|
| 1 | `entity/{Domain}Entity.java` | `@Data @Builder @NoArgsConstructor @AllArgsConstructor` |
| 2 | `enums/{Enum}.java` | Only if domain requires enums |
| 3 | `model/{Domain}.java` | Immutable `record` matching entity fields |
| 4 | `dto/request/{Domain}RequestDTO.java` | `@Valid`, `@NotBlank`/`@NotNull` on required fields |
| 5 | `dto/response/{Domain}ResponseDTO.java` | Same fields as entity + id, no validation |
| 6 | `repository/{Domain}Repository.java` | `extends JpaRepository<{Entity}, Long>` |
| 7 | `service/{Domain}Service.java` | Interface: `findAll`, `findById`, `create`, `update`, `delete` |
| 8 | `service/impl/{Domain}ServiceImpl.java` | `@Service @Transactional`. Injects `GenericMapper` (`@Component` with `ModelMapper`, methods: `toDto`, `toEntity`, `updateEntity`). Uses `EntityNotFoundException` for 404 |
| 9 | `controller/{Domain}Controller.java` | `@RestController`, `ResponseEntity<T>` direct |
| 10 | `DATA_SPEC.md` | Add new domain section |
| — | `mapper/{Domain}Mapper.java` | Only if domain needs custom logic (calc fields, encryption, etc.). Replaces `GenericMapper` in ServiceImpl; methods: `toResponseDTO`, `toEntity`, `updateEntity` (no class params) |
| 11 | `service/{Domain}ServiceTest.java` | `@ExtendWith(MockitoExtension)`, mock repo + `GenericMapper` |
| 12 | `integration/{Domain}IntegrationTest.java` | `@SpringBootTest(RANDOM_PORT)`, `RestTemplate` |
| 13 | `controller/{Domain}ControllerTest.java` | `MockMvcBuilders.standaloneSetup` + `@Mock` service |

## CRUD: GET(all)200 | GET(id)200/404 | POST 201/400 | PUT 200/404 | DELETE 204/404 | paths: `/api/{domain}s[/{id}]`

## SOLID / OCP (mandatory)
- **Before coding `create()`/`update()`**: ask if business rules could vary or grow later. If yes → inject strategies via `List<Interface>` + Factory (see `design-patterns` skill). Never hardcode varying logic inside ServiceImpl.
- Controller → interface Service → ServiceImpl → Repository. Always depend on abstractions, never on concrete classes (DIP).
- `record` models are the domain truth (immutable); entities are persistence-only. Keep them separate (SRP).

## ServiceImpl template

```java
@Service @RequiredArgsConstructor
public class {Domain}ServiceImpl implements {Domain}Service {
    private final {Domain}Repository repository;
    private final GenericMapper genericMapper;

    @Transactional(readOnly = true)
    public List<ResponseDTO> findAll() { return repository.findAll().stream().map(e -> genericMapper.toDto(e, ResponseDTO.class)).toList(); }

    @Transactional(readOnly = true)
    public ResponseDTO findById(Long id) {
        return repository.findById(id).map(e -> genericMapper.toDto(e, ResponseDTO.class))
            .orElseThrow(() -> new EntityNotFoundException("{Domain} not found: " + id));
    }

    @Transactional
    public ResponseDTO create(RequestDTO dto) { Entity e = repository.save(genericMapper.toEntity(dto, Entity.class)); return genericMapper.toDto(e, ResponseDTO.class); }

    @Transactional
    public ResponseDTO update(Long id, RequestDTO dto) {
        Entity e = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("{Domain} not found: " + id));
        genericMapper.updateEntity(dto, e); return genericMapper.toDto(repository.save(e), ResponseDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new EntityNotFoundException("{Domain} not found: " + id);
        repository.deleteById(id);
    }
}
```

## Custom mapper (rare)

When a `{Domain}Mapper` is created, inject it instead of `GenericMapper`. Method signatures change:

```java
private final {Domain}Mapper mapper;   // replaces GenericMapper

mapper.toResponseDTO(entity)           // replaces genericMapper.toDto(e, ResponseDTO.class)
mapper.toEntity(requestDTO)            // replaces genericMapper.toEntity(dto, Entity.class)
mapper.updateEntity(requestDTO, entity) // replaces genericMapper.updateEntity(dto, e)
```
