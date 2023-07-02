package com.example.ludogorieSoft.village.auth;

import com.example.ludogorieSoft.village.authorization.JWTService;
import com.example.ludogorieSoft.village.dtos.request.AuthenticationRequest;
import com.example.ludogorieSoft.village.dtos.request.RegisterRequest;
import com.example.ludogorieSoft.village.dtos.response.AuthenticationResponce;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.repositories.AdministratorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

//    @Test
//    void register_ShouldReturnAuthenticationResponse() {
//        // Arrange
//        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "johndoe", "password", "1234567890", Role.ADMIN);
//        Administrator savedAdministrator = mock(Administrator.class);
//        String encodedPassword = "encodedPassword";
//        String jwtToken = "jwtToken";
//
//        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
//        when(administratorRepository.save(any(Administrator.class))).thenReturn(savedAdministrator);
//        when(jwtService.generateToken(savedAdministrator)).thenReturn(jwtToken);
//
//        // Act
//        AuthenticationResponce response = authenticationService.register(request);
//
//        // Assert
//        assertEquals(jwtToken, response.getToken());
//        verify(passwordEncoder).encode(request.getPassword());
//        verify(administratorRepository).save(any(Administrator.class));
//        verify(jwtService).generateToken(savedAdministrator);
//    }


    @Test
    void authenticate_ShouldReturnAuthenticationResponse() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("johndoe", "password");
        Administrator authenticatedAdministrator = mock(Administrator.class);
        String jwtToken = "jwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(administratorRepository.findByUsername(request.getUsername())).thenReturn(authenticatedAdministrator);
        when(jwtService.generateToken(authenticatedAdministrator)).thenReturn(jwtToken);

        // Act
        AuthenticationResponce response = authenticationService.authenticate(request);

        // Assert
        assertEquals(jwtToken, response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(administratorRepository).findByUsername(request.getUsername());
        verify(jwtService).generateToken(authenticatedAdministrator);
    }
}
