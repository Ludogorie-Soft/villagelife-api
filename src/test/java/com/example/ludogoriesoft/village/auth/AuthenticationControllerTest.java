package com.example.ludogorieSoft.village.auth;

import com.example.ludogorieSoft.village.dtos.request.AuthenticationRequest;
import com.example.ludogorieSoft.village.dtos.request.RegisterRequest;
import com.example.ludogorieSoft.village.dtos.response.AuthenticationResponce;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.repositories.AdministratorRepository;
import com.example.ludogorieSoft.village.authorization.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 class AuthenticationControllerTest {
    @Mock
    private AdministratorRepository administratorRepository;
    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationController = new AuthenticationController(new AuthenticationService(
                administratorRepository, passwordEncoder, jwtService, authenticationManager));
    }

    @Test
    void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName("name");
        registerRequest.setEmail("email@");
        registerRequest.setMobile("mobile");
        registerRequest.setRole(Role.ADMIN);
        registerRequest.setUsername("username");
        registerRequest.setPassword("pass");

        AuthenticationResponce authenticationResponse = new AuthenticationResponce();
        authenticationResponse.setToken("token");

        ArgumentCaptor<Administrator> administratorCaptor = ArgumentCaptor.forClass(Administrator.class);
        when(jwtService.generateToken(administratorCaptor.capture())).thenReturn("token");

        ResponseEntity<AuthenticationResponce> responseEntity = authenticationController.register(registerRequest);
        when(authenticationService.register(registerRequest)).thenReturn(authenticationResponse);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(authenticationResponse, responseEntity.getBody());

        Administrator capturedAdministrator = administratorCaptor.getValue();
        assertEquals(registerRequest.getFullName(), capturedAdministrator.getFullName());
    }

    @Test
     void testAuthenticate() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("username");
        authenticationRequest.setPassword("pass");

        Administrator user = new Administrator();
        user.setUsername("username");
        user.setPassword("pass");
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(administratorRepository.findByUsername(anyString())).thenReturn(user);
        when(jwtService.generateToken(any(Administrator.class))).thenReturn("token");

        ResponseEntity<AuthenticationResponce> responseEntity = authenticationController.authenticate(authenticationRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AuthenticationResponce authenticationResponse = responseEntity.getBody();
        assertEquals("token", authenticationResponse.getToken());

        verify(authenticationManager).authenticate(any());

        verify(jwtService).generateToken(user);
    }
}

