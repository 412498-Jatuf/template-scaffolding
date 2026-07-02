package ar.utn.frc.tup.pii.service.impl;

import ar.utn.frc.tup.pii.dto.request.LoginRequestDTO;
import ar.utn.frc.tup.pii.dto.request.RegisterRequestDTO;
import ar.utn.frc.tup.pii.dto.response.UserResponseDTO;
import ar.utn.frc.tup.pii.entity.UserEntity;
import ar.utn.frc.tup.pii.exception.BadCredentialsException;
import ar.utn.frc.tup.pii.mapper.GenericMapper;
import ar.utn.frc.tup.pii.repository.UserRepository;
import ar.utn.frc.tup.pii.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final GenericMapper genericMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO register(RegisterRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new DataIntegrityViolationException("El email ya esta registrado");
        }
        if (repository.existsByUsername(dto.getUsername())) {
            throw new DataIntegrityViolationException("El username ya esta registrado");
        }
        UserEntity entity = genericMapper.toEntity(dto, UserEntity.class);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);
        return genericMapper.toDto(entity, UserResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO login(LoginRequestDTO dto) {
        UserEntity entity = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas"));
        if (!passwordEncoder.matches(dto.getPassword(), entity.getPassword())) {
            throw new BadCredentialsException("Credenciales invalidas");
        }
        return genericMapper.toDto(entity, UserResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findMe(String email) {
        return repository.findByEmail(email)
                .map(e -> genericMapper.toDto(e, UserResponseDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }
}
