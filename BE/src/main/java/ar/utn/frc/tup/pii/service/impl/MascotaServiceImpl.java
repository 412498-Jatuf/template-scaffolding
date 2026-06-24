package ar.utn.frc.tup.pii.service.impl;

import ar.utn.frc.tup.pii.dto.request.MascotaRequestDTO;
import ar.utn.frc.tup.pii.dto.response.MascotaResponseDTO;
import ar.utn.frc.tup.pii.entity.MascotaEntity;
import ar.utn.frc.tup.pii.mapper.MascotaMapper;
import ar.utn.frc.tup.pii.repository.MascotaRepository;
import ar.utn.frc.tup.pii.service.MascotaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository repository;
    private final MascotaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MascotaResponseDTO findById(Long id) {
        MascotaEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada con id: " + id));
        return mapper.toResponseDTO(entity);
    }

    @Override
    @Transactional
    public MascotaResponseDTO create(MascotaRequestDTO requestDTO) {
        MascotaEntity entity = mapper.toEntity(requestDTO);
        entity = repository.save(entity);
        return mapper.toResponseDTO(entity);
    }

    @Override
    @Transactional
    public MascotaResponseDTO update(Long id, MascotaRequestDTO requestDTO) {
        MascotaEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada con id: " + id));
        mapper.updateEntity(requestDTO, entity);
        entity = repository.save(entity);
        return mapper.toResponseDTO(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Mascota no encontrada con id: " + id);
        }
        repository.deleteById(id);
    }
}
