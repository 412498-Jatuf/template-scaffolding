# DATA_SPEC — Especificacion de datos (SDD)

Fuente de verdad para ENUMs, entidades JPA, modelos en memoria y DTOs.
Toda discrepancia entre este documento y el codigo es un bug en el codigo.

---

## ENUMs de dominio

### Especie

Package: `ar.utn.frc.tup.pii.enums`

| Valor   | Significado      |
|---------|------------------|
| `PERRO` | Perro domestico  |
| `GATO`  | Gato domestico   |
| `AVE`   | Ave domestica    |
| `PEZ`   | Pez domestico    |

---

## Entidades JPA

Package: `ar.utn.frc.tup.pii.entity`
Anotaciones Lombok: `@Data @Builder @NoArgsConstructor @AllArgsConstructor`

### MascotaEntity

Tabla: `mascotas`

| Campo Java        | Tipo Java    | Columna SQL        | Tipo SQL              | Restricciones                | JPA                          |
|-------------------|-------------|--------------------|-----------------------|-----------------------------|-----------------------------|
| `id`              | `Long`       | `id`               | `BIGINT AUTO_INCREMENT`| PK                           | `@Id @GeneratedValue(IDENTITY)` |
| `nombre`          | `String`     | `nombre`           | `VARCHAR(100)`        | NOT NULL                     | `@Column(nullable=false, length=100)` |
| `especie`         | `Especie`    | `especie`          | `VARCHAR(20)`         | NOT NULL                     | `@Enumerated(STRING)`       |
| `raza`            | `String`     | `raza`             | `VARCHAR(100)`        | nullable                     |                              |
| `edad`            | `Integer`    | `edad`             | `INT`                 | nullable                     |                              |
| `peso`            | `Double`     | `peso`             | `DOUBLE`              | nullable                     |                              |
| `fechaNacimiento` | `LocalDate`  | `fecha_nacimiento`  | `DATE`                | nullable                     |                              |
| `descripcion`     | `String`     | `descripcion`      | `VARCHAR(500)`        | nullable                     |                              |

---

## Modelos en memoria

Package: `ar.utn.frc.tup.pii.model`

### Mascota

Java record inmutable. Representa el modelo de dominio puro sin dependencias JPA.

```
Mascota {
  Long              id
  String            nombre
  Especie           especie
  String            raza
  Integer           edad
  Double            peso
  LocalDate         fechaNacimiento
  String            descripcion
}
```

---

## DTOs

### Request DTO — `dto/request/`

#### MascotaRequestDTO

| Campo            | Tipo       | Validaciones                  |
|-----------------|------------|-------------------------------|
| `nombre`         | `String`   | `@NotBlank`, `@Size(max=100)` |
| `especie`        | `Especie`  | `@NotNull`                    |
| `raza`           | `String`   | `@Size(max=100)`              |
| `edad`           | `Integer`  | `@Min(0)`                     |
| `peso`           | `Double`   | `@DecimalMin("0.0")`          |
| `fechaNacimiento`| `LocalDate`| `@PastOrPresent`              |
| `descripcion`    | `String`   | `@Size(max=500)`              |

### Response DTO — `dto/response/`

#### MascotaResponseDTO

| Campo            | Tipo       |
|-----------------|------------|
| `id`             | `Long`     |
| `nombre`         | `String`   |
| `especie`        | `Especie`  |
| `raza`           | `String`   |
| `edad`           | `Integer`  |
| `peso`           | `Double`   |
| `fechaNacimiento`| `LocalDate`|
| `descripcion`    | `String`   |

---

## Manejo de errores — `exception/`

### ErrorResponse

| Campo      | Tipo            |
|-----------|-----------------|
| `status`   | `int`           |
| `message`  | `String`        |
| `timestamp`| `LocalDateTime` |

### GlobalExceptionHandler

| Excepcion                          | HTTP Status | Response              |
|------------------------------------|-------------|-----------------------|
| `jakarta.persistence.EntityNotFoundException` | 404         | `ErrorResponse` (mensaje de la excepcion) |
| `MethodArgumentNotValidException`  | 400         | `ErrorResponse` (campo: mensaje) |
| `BadCredentialsException`           | 401         | `ErrorResponse` ("Credenciales invalidas") |
| `DataIntegrityViolationException`   | 409         | `ErrorResponse` (mensaje de la excepcion) |
| `Exception` (generica)             | 500         | `ErrorResponse` ("Error interno del servidor") |

---

## Autenticacion — `auth/`

### UserEntity

Tabla: `users`

| Campo     | Tipo     | Restricciones |
|-----------|---------|---------------|
| `id`      | `Long`  | PK, AUTO_INCREMENT |
| `username`| `String`| NOT NULL, UNIQUE, max 50 |
| `email`   | `String`| NOT NULL, UNIQUE, max 100 |
| `password`| `String`| NOT NULL, BCrypt hashed |

### DTOs

#### RegisterRequestDTO

| Campo     | Tipo     | Validaciones |
|-----------|---------|--------------|
| `username`| `String`| `@NotBlank`, `@Size(max=50)` |
| `email`   | `String`| `@NotBlank`, `@Email`, `@Size(max=100)` |
| `password`| `String`| `@NotBlank`, `@Size(min=6)` |

#### LoginRequestDTO

| Campo     | Tipo     | Validaciones |
|-----------|---------|--------------|
| `email`   | `String`| `@NotBlank` |
| `password`| `String`| `@NotBlank` |

#### UserResponseDTO

| Campo     | Tipo     |
|-----------|---------|
| `id`      | `Long`  |
| `username`| `String`|
| `email`   | `String`|

### Endpoints Auth

| Metodo | Endpoint              | Request Body          | Response Body          | Status |
|--------|-----------------------|-----------------------|------------------------|--------|
| `POST` | `/api/auth/register`  | `RegisterRequestDTO`  | `UserResponseDTO`      | 201/400/409 |
| `POST` | `/api/auth/login`     | `LoginRequestDTO`     | `UserResponseDTO`      | 200/401 |
| `GET`  | `/api/auth/me`        | —                     | `UserResponseDTO`      | 200/401 |

### API REST (protegida)

Todos los endpoints bajo `/api/mascotas` requieren autenticacion HTTP Basic.

| Metodo   | Endpoint              | Request Body        | Response Body         | Status         |
|----------|-----------------------|---------------------|-----------------------|----------------|
| `POST`   | `/api/mascotas`       | `MascotaRequestDTO` | `MascotaResponseDTO`  | `201 Created`  |
| `GET`    | `/api/mascotas`       | —                   | `List<MascotaResponseDTO>` | `200 OK`  |
| `GET`    | `/api/mascotas/{id}`  | —                   | `MascotaResponseDTO`  | `200 OK`       |
| `PUT`    | `/api/mascotas/{id}`  | `MascotaRequestDTO` | `MascotaResponseDTO`  | `200 OK`       |
| `DELETE` | `/api/mascotas/{id}`  | —                   | —                     | `204 No Content` |

Errores:
- `400 Bad Request` — `ErrorResponse` con detalle de campos invalidos
- `401 Unauthorized` — credenciales invalidas o no autenticado
- `404 Not Found` — `ErrorResponse` con mensaje de entidad no encontrada
- `409 Conflict` — email o username ya registrado
- `500 Internal Server Error` — `ErrorResponse` generico
