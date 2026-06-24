# test-strategy

Use this skill when **writing tests** for any layer. Follow the exact approach per layer.

## Test Matrix

| Layer | Approach | Framework | Context |
|-------|----------|-----------|---------|
| Controller | `MockMvcBuilders.standaloneSetup(controller)` | `@ExtendWith(MockitoExtension)` | No Spring |
| Service | `@ExtendWith(MockitoExtension)` + `@Mock` + `@InjectMocks` | Mockito only | No Spring |
| Integration | `@SpringBootTest(webEnvironment = RANDOM_PORT)` + `RestTemplate` | Spring Boot Test | Full context |
| Model | JUnit pure: instantiate and assert | None | No Spring |
| Exception | JUnit pure: call handler methods, assert `ResponseEntity` | None | No Spring |

## Controller Tests
- Build: `MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build()`
- Mock: `@Mock MascotaService`, `@InjectMocks MascotaController`
- Test both success AND error paths (200, 201, 204, 400, 404)
- Use `org.hamcrest.Matchers` for JSON path assertions

## Service Tests
- Mock: `@Mock Repository`, `@Mock Mapper`, `@InjectMocks ServiceImpl`
- Verify repository interactions: `verify(repository).findAll()`
- Test `EntityNotFoundException` throwing: `assertThrows(EntityNotFoundException.class, ...)`

## Integration Tests
- Use `RestTemplate` directly (no `TestRestTemplate`)
- DO NOT use a custom error handler on RestTemplate
- For 4xx/5xx: catch `HttpClientErrorException` (e.g., `.NotFound`, `.BadRequest`)
- Extract IDs from JSON body, not Location header

## Model / Exception Tests
- No mocks, no Spring, no annotations needed
- Model: test record creation + field access via accessor methods
- Exception: instantiate handler with `new`, call method, check response body fields

## Key Rules
- NEVER use `@WebMvcTest`, `@DataJpaTest`, or `TestRestTemplate`
- NEVER create a custom error handler on RestTemplate in integration tests
- Each test method tests ONE scenario (success path OR error, not both)
- Use `jakarta.persistence.EntityNotFoundException`, not custom exceptions
