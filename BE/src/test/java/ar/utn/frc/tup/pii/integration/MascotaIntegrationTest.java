package ar.utn.frc.tup.pii.integration;

import ar.utn.frc.tup.pii.dto.request.MascotaRequestDTO;
import ar.utn.frc.tup.pii.enums.Especie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MascotaIntegrationTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/mascotas";
    }

    private Long createMascota(MascotaRequestDTO request) {
        ResponseEntity<String> raw = restTemplate.postForEntity(baseUrl, request, String.class);
        assertEquals(HttpStatus.CREATED, raw.getStatusCode());
        return extractId(raw.getBody());
    }

    private Long extractId(String json) {
        int start = json.indexOf("\"id\":") + 5;
        int end = json.indexOf(",", start);
        if (end < 0) end = json.indexOf("}", start);
        return Long.parseLong(json.substring(start, end).trim());
    }

    @Test
    void createAndFindMascota_ShouldPersistAndRetrieve() {
        Long id = createMascota(MascotaRequestDTO.builder()
                .nombre("Firulais")
                .especie(Especie.PERRO)
                .raza("Labrador")
                .edad(3)
                .peso(25.5)
                .fechaNacimiento(LocalDate.of(2023, 6, 15))
                .descripcion("Perro amigable")
                .build());

        ResponseEntity<String> getResponse = restTemplate.getForEntity(baseUrl + "/" + id, String.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertTrue(getResponse.getBody().contains("Firulais"));
    }

    @Test
    void findAll_ShouldReturnOk() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createWithInvalidData_ShouldReturnBadRequest() {
        try {
            restTemplate.postForEntity(baseUrl,
                    MascotaRequestDTO.builder().nombre("").especie(null).build(),
                    String.class);
            fail("Expected BadRequest");
        } catch (HttpClientErrorException.BadRequest e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    void findById_NotFound_ShouldReturn404() {
        try {
            restTemplate.getForEntity(baseUrl + "/999", String.class);
            fail("Expected NotFound");
        } catch (HttpClientErrorException.NotFound e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("Mascota no encontrada"));
        }
    }

    @Test
    void updateMascota_ShouldUpdateSuccessfully() {
        Long id = createMascota(MascotaRequestDTO.builder()
                .nombre("Mishi")
                .especie(Especie.GATO)
                .raza("Siames")
                .edad(2)
                .peso(4.5)
                .build());

        MascotaRequestDTO updateRequest = MascotaRequestDTO.builder()
                .nombre("Mishi Actualizado")
                .especie(Especie.GATO)
                .raza("Siames")
                .edad(3)
                .peso(5.0)
                .build();

        ResponseEntity<String> updateResponse = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.PUT,
                new HttpEntity<>(updateRequest), String.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertTrue(updateResponse.getBody().contains("Mishi Actualizado"));
    }

    @Test
    void deleteMascota_ShouldDeleteSuccessfully() {
        Long id = createMascota(MascotaRequestDTO.builder()
                .nombre("Loro")
                .especie(Especie.AVE)
                .build());

        restTemplate.delete(baseUrl + "/" + id);

        try {
            restTemplate.getForEntity(baseUrl + "/" + id, String.class);
            fail("Expected NotFound after delete");
        } catch (HttpClientErrorException.NotFound e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }
}
