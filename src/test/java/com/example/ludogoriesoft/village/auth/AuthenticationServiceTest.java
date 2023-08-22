package com.example.ludogorieSoft.village.auth;

import com.example.ludogorieSoft.village.authorization.JWTService;
import com.example.ludogorieSoft.village.dtos.request.AuthenticationRequest;
import com.example.ludogorieSoft.village.dtos.request.RegisterRequest;
import com.example.ludogorieSoft.village.dtos.response.AuthenticationResponce;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.repositories.AdministratorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    @Mock
    private AdministratorRepository administratorRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerShouldReturnAuthenticationResponse() {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "username", "password", "1234567890", Role.ADMIN);

        String encodedPassword = "password";
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);

        Administrator savedAdministrator = Administrator.builder()
                .fullName(request.getFullName())
                .username("username")
                .password(encodedPassword)
                .build();


        when(administratorRepository.save(any(Administrator.class))).thenReturn(savedAdministrator);
        String response = authenticationService.register(request);
        verify(passwordEncoder).encode(request.getPassword());
        verify(administratorRepository).save(any(Administrator.class));
        assertNotNull(response);

    }

    @Test
    void authenticate_ShouldReturnAuthenticationResponse() {
        AuthenticationRequest request = new AuthenticationRequest("johndoe", "password");
        Administrator authenticatedAdministrator = mock(Administrator.class);
        String jwtToken = "jwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(administratorRepository.findByUsername(request.getUsername())).thenReturn(authenticatedAdministrator);
        when(jwtService.generateToken(authenticatedAdministrator)).thenReturn(jwtToken);

        AuthenticationResponce response = authenticationService.authenticate(request);

        assertEquals(jwtToken, response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(administratorRepository).findByUsername(request.getUsername());
        verify(jwtService).generateToken(authenticatedAdministrator);
    }
}
