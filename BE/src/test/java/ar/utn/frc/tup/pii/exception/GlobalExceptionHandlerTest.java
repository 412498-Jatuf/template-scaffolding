package ar.utn.frc.tup.pii.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFound_ShouldReturn404() {
        EntityNotFoundException ex = new EntityNotFoundException("Mascota no encontrada");

        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Mascota no encontrada", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleValidation_ShouldReturn400() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(
                new Object(), "mascotaRequestDTO");
        bindingResult.addError(new FieldError("mascotaRequestDTO", "nombre", "es obligatorio"));
        bindingResult.addError(new FieldError("mascotaRequestDTO", "especie", "es obligatoria"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("nombre: es obligatorio"));
        assertTrue(response.getBody().getMessage().contains("especie: es obligatoria"));
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleGeneral_ShouldReturn500() {
        RuntimeException ex = new RuntimeException("Error inesperado");

        ResponseEntity<ErrorResponse> response = handler.handleGeneral(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleBadCredentials_ShouldReturn401() {
        BadCredentialsException ex = new BadCredentialsException("Credenciales invalidas");

        ResponseEntity<ErrorResponse> response = handler.handleBadCredentials(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getStatus());
        assertEquals("Credenciales invalidas", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleDataIntegrity_ShouldReturn409() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("El email ya esta registrado");

        ResponseEntity<ErrorResponse> response = handler.handleDataIntegrity(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("El email ya esta registrado"));
        assertNotNull(response.getBody().getTimestamp());
    }
}
