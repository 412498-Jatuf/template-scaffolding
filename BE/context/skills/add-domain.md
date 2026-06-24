# add-domain

Use this skill to add a **new domain** (e.g., `producto`, `cliente`) following the project scaffolding conventions. The domain name is used in **singular PascalCase** for classes and **plural lowercase** for API paths.

## Files to create (14 per domain)

### Main (11 files)
| # | File | Notes |
|---|------|-------|
| 1 | `entity/{Domain}Entity.java` | JPA entity, `@Data @Builder @NoArgsConstructor @AllArgsConstructor` |
| 2 | `enums/{Enum}.java` | Only if domain requires enums |
| 3 | `model/{Domain}.java` | Immutable `record` matching entity fields |
| 4 | `dto/request/{Domain}RequestDTO.java` | `@Valid`, `@NotBlank`/`@NotNull` on required fields |
| 5 | `dto/response/{Domain}ResponseDTO.java` | Same fields as entity + id, no validation |
| 6 | `mapper/{Domain}Mapper.java` | `@Component`, injects `ModelMapper`; methods: `toResponseDTO`, `toEntity`, `updateEntity` |
| 7 | `repository/{Domain}Repository.java` | `extends JpaRepository<{Entity}, Long>` |
| 8 | `service/{Domain}Service.java` | Interface: `findAll`, `findById`, `create`, `update`, `delete` |
| 9 | `service/impl/{Domain}ServiceImpl.java` | `@Service`, `@Transactional`; uses `EntityNotFoundException` for 404 |
| 10 | `controller/{Domain}Controller.java` | `@RestController`, `ResponseEntity<T>` direct, no ApiResponse |
| 11 | Update `DATA_SPEC.md` | Add new domain section |

### Tests (3 files)
| # | File | Approach |
|---|------|----------|
| 12 | `service/{Domain}ServiceTest.java` | `@ExtendWith(MockitoExtension)`, `@Mock` repo+mapper |
| 13 | `integration/{Domain}IntegrationTest.java` | `@SpringBootTest(RANDOM_PORT)`, `RestTemplate` |
| 14 | `controller/{Domain}ControllerTest.java` | `MockMvcBuilders.standaloneSetup` + `@Mock` service |

## CRUD Endpoints
| Method | Path | Success | Error |
|--------|------|---------|-------|
| GET | `/api/{domain}s` | 200 List | — |
| GET | `/api/{domain}s/{id}` | 200 | 404 |
| POST | `/api/{domain}s` | 201 | 400 |
| PUT | `/api/{domain}s/{id}` | 200 | 404 |
| DELETE | `/api/{domain}s/{id}` | 204 | 404 |

## ServiceImpl pattern
```java
@Service @RequiredArgsConstructor
public class {Domain}ServiceImpl implements {Domain}Service {
    private final {Domain}Repository repository;
    private final {Domain}Mapper mapper;

    @Transactional(readOnly = true)
    public List<ResponseDTO> findAll() { ... }

    @Transactional(readOnly = true)
    public ResponseDTO findById(Long id) {
        return repository.findById(id)
            .map(mapper::toResponseDTO)
            .orElseThrow(() -> new EntityNotFoundException("{Domain} not found: " + id));
    }

    @Transactional
    public ResponseDTO create(RequestDTO dto) { ... }

    @Transactional
    public ResponseDTO update(Long id, RequestDTO dto) {
        Entity e = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("{Domain} not found: " + id));
        mapper.updateEntity(dto, e);
        return mapper.toResponseDTO(repository.save(e));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("{Domain} not found: " + id);
        repository.deleteById(id);
    }
}
```
