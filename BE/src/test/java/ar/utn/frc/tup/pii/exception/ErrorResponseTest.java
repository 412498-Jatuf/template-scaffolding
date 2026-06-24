package ar.utn.frc.tup.pii.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorResponseTest {

    @Test
    void shouldBuildErrorResponseWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse error = ErrorResponse.builder()
                .status(404)
                .message("No encontrado")
                .timestamp(now)
                .build();

        assertNotNull(error);
        assertEquals(404, error.getStatus());
        assertEquals("No encontrado", error.getMessage());
        assertEquals(now, error.getTimestamp());
    }

    @Test
    void shouldBuildDefaultErrorResponse() {
        ErrorResponse error = new ErrorResponse();

        assertEquals(0, error.getStatus());
        assertEquals(null, error.getMessage());
        assertEquals(null, error.getTimestamp());
    }
}
