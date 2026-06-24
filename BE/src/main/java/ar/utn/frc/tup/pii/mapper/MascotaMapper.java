package ar.utn.frc.tup.pii.mapper;

import ar.utn.frc.tup.pii.dto.request.MascotaRequestDTO;
import ar.utn.frc.tup.pii.dto.response.MascotaResponseDTO;
import ar.utn.frc.tup.pii.entity.MascotaEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MascotaMapper {

    private final ModelMapper modelMapper;

    public MascotaResponseDTO toResponseDTO(MascotaEntity entity) {
        return modelMapper.map(entity, MascotaResponseDTO.class);
    }

    public MascotaEntity toEntity(MascotaRequestDTO requestDTO) {
        return modelMapper.map(requestDTO, MascotaEntity.class);
    }

    public void updateEntity(MascotaRequestDTO requestDTO, MascotaEntity entity) {
        modelMapper.map(requestDTO, entity);
    }
}
