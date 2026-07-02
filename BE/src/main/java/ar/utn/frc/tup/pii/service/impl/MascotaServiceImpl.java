package ar.utn.frc.tup.pii.service.impl;

import ar.utn.frc.tup.pii.dto.request.MascotaRequestDTO;
import ar.utn.frc.tup.pii.dto.response.MascotaResponseDTO;
import ar.utn.frc.tup.pii.entity.MascotaEntity;
import ar.utn.frc.tup.pii.mapper.GenericMapper;
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
    private final GenericMapper genericMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(e -> genericMapper.toDto(e, MascotaResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MascotaResponseDTO findById(Long id) {
        MascotaEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada con id: " + id));
        return genericMapper.toDto(entity, MascotaResponseDTO.class);
    }

    @Override
    @Transactional
    public MascotaResponseDTO create(MascotaRequestDTO requestDTO) {
        MascotaEntity entity = genericMapper.toEntity(requestDTO, MascotaEntity.class);
        entity = repository.save(entity);
        return genericMapper.toDto(entity, MascotaResponseDTO.class);
    }

    @Override
    @Transactional
    public MascotaResponseDTO update(Long id, MascotaRequestDTO requestDTO) {
        MascotaEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada con id: " + id));
        genericMapper.updateEntity(requestDTO, entity);
        entity = repository.save(entity);
        return genericMapper.toDto(entity, MascotaResponseDTO.class);
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
