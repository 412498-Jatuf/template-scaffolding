# test-strategy

Use this skill when **writing tests** for any layer. No Spring context except integration.

## Per layer

- **Controller**: `MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build()`, mock service, test success AND error paths (200, 201, 204, 400, 404), JSON assertions with `org.hamcrest.Matchers`
- **Service**: `@ExtendWith(MockitoExtension)`, `@Mock Repository` + `@InjectMocks ServiceImpl`. Default mapper: `@Mock GenericMapper`. Custom mapper: `@Mock {Domain}Mapper` instead. Mock: `when(genericMapper.toDto(entity, Class)).thenReturn(dto)`. Verify: `verify(repository).findAll()`. Error: `assertThrows(EntityNotFoundException, ...)`
- **Integration**: `@SpringBootTest(RANDOM_PORT)` + `RestTemplate` (no `TestRestTemplate`). Catch `HttpClientErrorException` for 4xx/5xx. Extract IDs from JSON body
- **Model / Exception**: pure JUnit, no mocks, no Spring. Model: instantiate record + assert fields. Exception: `new` handler + assert `ResponseEntity` body

## Key Rules
- NEVER `@WebMvcTest`, `@DataJpaTest`, `TestRestTemplate`
- NEVER custom error handler on RestTemplate (integration tests)
- One scenario per test method (success OR error, not both)
- `jakarta.persistence.EntityNotFoundException` only, no custom exceptions
