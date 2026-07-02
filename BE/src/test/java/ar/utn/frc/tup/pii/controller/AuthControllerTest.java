package ar.utn.frc.tup.pii.controller;

import ar.utn.frc.tup.pii.dto.request.LoginRequestDTO;
import ar.utn.frc.tup.pii.dto.request.RegisterRequestDTO;
import ar.utn.frc.tup.pii.dto.response.UserResponseDTO;
import ar.utn.frc.tup.pii.exception.BadCredentialsException;
import ar.utn.frc.tup.pii.exception.GlobalExceptionHandler;
import ar.utn.frc.tup.pii.service.UserService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private AuthController controller;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private UserResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        responseDTO = UserResponseDTO.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .build();
    }

    @Test
    void register_ShouldReturnCreated() throws Exception {
        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("testuser")
                .email("test@test.com")
                .password("password123")
                .build();

        when(service.register(any(RegisterRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@test.com")));
    }

    @Test
    void register_InvalidData_ShouldReturn400() throws Exception {
        RegisterRequestDTO invalid = RegisterRequestDTO.builder()
                .username("")
                .email("no-email")
                .password("123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ShouldReturnOk() throws Exception {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .email("test@test.com")
                .password("password123")
                .build();

        when(service.login(any(LoginRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")));
    }

    @Test
    void login_BadCredentials_ShouldReturn401() throws Exception {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .email("wrong@test.com")
                .password("wrongpass")
                .build();

        when(service.login(any(LoginRequestDTO.class)))
                .thenThrow(new BadCredentialsException("Credenciales invalidas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is(401)));
    }
}
