package ar.utn.frc.tup.pii.service;

import ar.utn.frc.tup.pii.dto.request.MascotaRequestDTO;
import ar.utn.frc.tup.pii.dto.response.MascotaResponseDTO;
import ar.utn.frc.tup.pii.entity.MascotaEntity;
import ar.utn.frc.tup.pii.enums.Especie;
import ar.utn.frc.tup.pii.mapper.MascotaMapper;
import ar.utn.frc.tup.pii.repository.MascotaRepository;
import ar.utn.frc.tup.pii.service.impl.MascotaServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository repository;

    @Mock
    private MascotaMapper mapper;

    @InjectMocks
    private MascotaServiceImpl service;

    private MascotaEntity entity;
    private MascotaResponseDTO responseDTO;
    private MascotaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        entity = MascotaEntity.builder()
                .id(1L)
                .nombre("Firulais")
                .especie(Especie.PERRO)
                .raza("Labrador")
                .edad(3)
                .peso(25.5)
                .fechaNacimiento(LocalDate.of(2023, 6, 15))
                .descripcion("Perro amigable")
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

        requestDTO = MascotaRequestDTO.builder()
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
    void findAll_ShouldReturnListOfMascotas() {
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponseDTO(entity)).thenReturn(responseDTO);

        List<MascotaResponseDTO> result = service.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Firulais", result.get(0).getNombre());
        verify(repository).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList() {
        when(repository.findAll()).thenReturn(List.of());

        List<MascotaResponseDTO> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_ShouldReturnMascota() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponseDTO(entity)).thenReturn(responseDTO);

        MascotaResponseDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals("Firulais", result.getNombre());
        assertEquals(Especie.PERRO, result.getEspecie());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void create_ShouldSaveAndReturnMascota() {
        when(mapper.toEntity(requestDTO)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponseDTO(entity)).thenReturn(responseDTO);

        MascotaResponseDTO result = service.create(requestDTO);

        assertNotNull(result);
        assertEquals("Firulais", result.getNombre());
        verify(repository).save(entity);
    }

    @Test
    void update_ShouldUpdateAndReturnMascota() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        doNothing().when(mapper).updateEntity(requestDTO, entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponseDTO(entity)).thenReturn(responseDTO);

        MascotaResponseDTO result = service.update(1L, requestDTO);

        assertNotNull(result);
        assertEquals("Firulais", result.getNombre());
        verify(mapper).updateEntity(requestDTO, entity);
    }

    @Test
    void update_ShouldThrowException_WhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(99L, requestDTO));
    }

    @Test
    void delete_ShouldDeleteMascota() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.delete(99L));
    }
}
