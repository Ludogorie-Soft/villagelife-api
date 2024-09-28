package com.example.ludogorieSoft.village.auth;

import com.example.ludogorieSoft.village.authorization.JWTService;
import com.example.ludogorieSoft.village.dtos.request.AuthenticationRequest;
import com.example.ludogorieSoft.village.dtos.request.RegisterRequest;
import com.example.ludogorieSoft.village.dtos.response.AuthenticationResponce;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.exeptions.UsernamePasswordException;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import com.example.ludogorieSoft.village.repositories.AlternativeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    @Mock
    private AlternativeUserRepository alternativeUserRepository;
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

        AlternativeUser savedAlternativeUser = AlternativeUser.builder()
                .fullName(request.getFullName())
                .username("username")
                .password(encodedPassword)
                .build();


        when(alternativeUserRepository.save(any(AlternativeUser.class))).thenReturn(savedAlternativeUser);
        String response = authenticationService.register(request);
        verify(passwordEncoder).encode(request.getPassword());
        verify(alternativeUserRepository).save(any(AlternativeUser.class));
        assertNotNull(response);

    }

    @Test
    void authenticate_ShouldReturnAuthenticationResponse() {
        AuthenticationRequest request = new AuthenticationRequest("johndoe", "password");
        AlternativeUser authenticatedAlternativeUser = mock(AlternativeUser.class);
        String jwtToken = "jwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(alternativeUserRepository.findByUsername(request.getUsername())).thenReturn(authenticatedAlternativeUser);
        when(jwtService.generateToken(authenticatedAlternativeUser)).thenReturn(jwtToken);

        AuthenticationResponce response = authenticationService.authenticate(request);

        assertEquals(jwtToken, response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(alternativeUserRepository).findByUsername(request.getUsername());
        verify(jwtService).generateToken(authenticatedAlternativeUser);
    }
    @Test
    void testAuthenticate_Failure() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(UsernameNotFoundException.class);

        AuthenticationRequest request = new AuthenticationRequest("invalidUsername", "invalidPassword");

        assertThrows(UsernamePasswordException.class, () -> authenticationService.authenticate(request));
    }
}
