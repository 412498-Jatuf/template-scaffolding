package ar.utn.frc.tup.pii.controller;

import ar.utn.frc.tup.pii.dto.request.MascotaRequestDTO;
import ar.utn.frc.tup.pii.dto.response.MascotaResponseDTO;
import ar.utn.frc.tup.pii.enums.Especie;
import ar.utn.frc.tup.pii.exception.GlobalExceptionHandler;
import ar.utn.frc.tup.pii.service.MascotaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MascotaControllerTest {

    @Mock
    private MascotaService service;

    @InjectMocks
    private MascotaController controller;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private MascotaResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        responseDTO = MascotaResponseDTO.builder()
                .id(1L)
                .nombre("Firulais")
                .especie(Especie.PERRO)
                .raza("Labrador")
                .edad(3)
                .peso(25.5)
                .fechaNacimiento(LocalDate.of(2023, 6, 15))
                .descripcion("Perro amigable")
                .build();
    }

    @Test
    void findAll_ShouldReturnList() throws Exception {
        when(service.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/mascotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Firulais")));
    }

    @Test
    void findById_ShouldReturnMascota() throws Exception {
        when(service.findById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/mascotas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Firulais")));
    }

    @Test
    void findById_NotFound_ShouldReturn404() throws Exception {
        when(service.findById(99L)).thenThrow(new EntityNotFoundException("No encontrada"));

        mockMvc.perform(get("/api/mascotas/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("No encontrada")));
    }

    @Test
    void create_ShouldReturnCreated() throws Exception {
        when(service.create(any(MascotaRequestDTO.class))).thenReturn(responseDTO);

        MascotaRequestDTO request = MascotaRequestDTO.builder()
                .nombre("Firulais")
                .especie(Especie.PERRO)
                .build();

        mockMvc.perform(post("/api/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Firulais")));
    }

    @Test
    void create_InvalidData_ShouldReturn400() throws Exception {
        MascotaRequestDTO invalid = MascotaRequestDTO.builder().nombre("").especie(null).build();

        mockMvc.perform(post("/api/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    void update_ShouldReturnUpdated() throws Exception {
        when(service.update(eq(1L), any(MascotaRequestDTO.class))).thenReturn(responseDTO);

        MascotaRequestDTO request = MascotaRequestDTO.builder()
                .nombre("Firulais")
                .especie(Especie.PERRO)
                .build();

        mockMvc.perform(put("/api/mascotas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Firulais")));
    }

    @Test
    void update_NotFound_ShouldReturn404() throws Exception {
        when(service.update(eq(99L), any(MascotaRequestDTO.class)))
                .thenThrow(new EntityNotFoundException("No encontrada"));

        MascotaRequestDTO request = MascotaRequestDTO.builder()
                .nombre("Firulais")
                .especie(Especie.PERRO)
                .build();

        mockMvc.perform(put("/api/mascotas/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/mascotas/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_NotFound_ShouldReturn404() throws Exception {
        doThrow(new EntityNotFoundException("No encontrada")).when(service).delete(99L);

        mockMvc.perform(delete("/api/mascotas/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }
}
