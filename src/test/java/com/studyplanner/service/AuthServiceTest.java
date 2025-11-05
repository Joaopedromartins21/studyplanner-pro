package com.studyplanner.service;

import com.studyplanner.dto.AuthResponse;
import com.studyplanner.dto.LoginRequest;
import com.studyplanner.dto.RegisterRequest;
import com.studyplanner.entity.User;
import com.studyplanner.repository.UserRepository;
import com.studyplanner.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setLevel(1);
        user.setExperience(0);
    }

    @Test
    void testRegister_Success() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("refresh-token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("jwt-token", response.getToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });

        assertEquals("Nome de usuário já existe", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("refresh-token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("jwt-token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
