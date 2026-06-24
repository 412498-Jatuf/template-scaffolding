package ar.utn.frc.tup.pii.service;

import ar.utn.frc.tup.pii.dto.request.MascotaRequestDTO;
import ar.utn.frc.tup.pii.dto.response.MascotaResponseDTO;

import java.util.List;

public interface MascotaService {

    List<MascotaResponseDTO> findAll();

    MascotaResponseDTO findById(Long id);

    MascotaResponseDTO create(MascotaRequestDTO requestDTO);

    MascotaResponseDTO update(Long id, MascotaRequestDTO requestDTO);

    void delete(Long id);
}
