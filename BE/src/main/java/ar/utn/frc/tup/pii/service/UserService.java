package ar.utn.frc.tup.pii.service;

import ar.utn.frc.tup.pii.dto.request.LoginRequestDTO;
import ar.utn.frc.tup.pii.dto.request.RegisterRequestDTO;
import ar.utn.frc.tup.pii.dto.response.UserResponseDTO;

public interface UserService {

    UserResponseDTO register(RegisterRequestDTO dto);

    UserResponseDTO login(LoginRequestDTO dto);

    UserResponseDTO findMe(String email);
}
