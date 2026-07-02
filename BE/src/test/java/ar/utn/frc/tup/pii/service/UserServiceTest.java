package ar.utn.frc.tup.pii.service;

import ar.utn.frc.tup.pii.dto.request.LoginRequestDTO;
import ar.utn.frc.tup.pii.dto.request.RegisterRequestDTO;
import ar.utn.frc.tup.pii.dto.response.UserResponseDTO;
import ar.utn.frc.tup.pii.entity.UserEntity;
import ar.utn.frc.tup.pii.exception.BadCredentialsException;
import ar.utn.frc.tup.pii.mapper.GenericMapper;
import ar.utn.frc.tup.pii.repository.UserRepository;
import ar.utn.frc.tup.pii.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private GenericMapper genericMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    private UserEntity entity;
    private UserResponseDTO responseDTO;
    private RegisterRequestDTO registerDTO;
    private LoginRequestDTO loginDTO;

    @BeforeEach
    void setUp() {
        entity = UserEntity.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .password("$2a$10$hashedPassword")
                .build();

        responseDTO = UserResponseDTO.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .build();

        registerDTO = RegisterRequestDTO.builder()
                .username("testuser")
                .email("test@test.com")
                .password("password123")
                .build();

        loginDTO = LoginRequestDTO.builder()
                .email("test@test.com")
                .password("password123")
                .build();
    }

    @Test
    void register_ShouldCreateAndReturnUser() {
        when(repository.existsByEmail(registerDTO.getEmail())).thenReturn(false);
        when(repository.existsByUsername(registerDTO.getUsername())).thenReturn(false);
        when(genericMapper.toEntity(registerDTO, UserEntity.class)).thenReturn(entity);
        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("$2a$10$hashedPassword");
        when(repository.save(entity)).thenReturn(entity);
        when(genericMapper.toDto(entity, UserResponseDTO.class)).thenReturn(responseDTO);

        UserResponseDTO result = service.register(registerDTO);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void register_DuplicateEmail_ShouldThrowException() {
        when(repository.existsByEmail(registerDTO.getEmail())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> service.register(registerDTO));
    }

    @Test
    void register_DuplicateUsername_ShouldThrowException() {
        when(repository.existsByEmail(registerDTO.getEmail())).thenReturn(false);
        when(repository.existsByUsername(registerDTO.getUsername())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> service.register(registerDTO));
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsValid() {
        when(repository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(loginDTO.getPassword(), entity.getPassword())).thenReturn(true);
        when(genericMapper.toDto(entity, UserResponseDTO.class)).thenReturn(responseDTO);

        UserResponseDTO result = service.login(loginDTO);

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void login_ShouldThrowException_WhenEmailNotFound() {
        when(repository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> service.login(loginDTO));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIncorrect() {
        when(repository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(loginDTO.getPassword(), entity.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> service.login(loginDTO));
    }
}
