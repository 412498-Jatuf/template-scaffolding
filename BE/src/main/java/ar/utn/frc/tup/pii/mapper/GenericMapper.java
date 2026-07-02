package ar.utn.frc.tup.pii.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenericMapper {

    private final ModelMapper modelMapper;

    public <E, R> R toDto(E entity, Class<R> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public <E, R> E toEntity(R dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public <R, E> void updateEntity(R requestDTO, E entity) {
        modelMapper.map(requestDTO, entity);
    }
}
