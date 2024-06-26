package com.example.ludogorieSoft.village.auth;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.request.AuthenticationRequest;
import com.example.ludogorieSoft.village.dtos.request.RegisterRequest;
import com.example.ludogorieSoft.village.dtos.response.AuthenticationResponce;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.repositories.AdministratorRepository;
import com.example.ludogorieSoft.village.authorization.JWTService;
import com.example.ludogorieSoft.village.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    @Mock
    private AuthService authService;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        administratorRepository = Mockito.mock(AdministratorRepository.class);
        authenticationService = Mockito.mock(AuthenticationService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtService = Mockito.mock(JWTService.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        authService = Mockito.mock(AuthService.class);
        authenticationController = new AuthenticationController(new AuthenticationService(
                administratorRepository, passwordEncoder, jwtService, authenticationManager), authService);
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

        String authenticationResponse ="Administrator registered successfully!!!";

       ResponseEntity<String> message = authenticationController.register(registerRequest);

        when(authenticationService.register(registerRequest)).thenReturn(authenticationResponse);

        assertEquals("Administrator registered successfully!!!", message.getBody());
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
        assert authenticationResponse != null;
        assertEquals("token", authenticationResponse.getToken());

        verify(authenticationManager).authenticate(any());

        verify(jwtService).generateToken(user);
    }

    @Test
    void testGetAdminInfo() {
        AdministratorDTO expectedAdministratorDTO = new AdministratorDTO();
        expectedAdministratorDTO.setId(1L);
        expectedAdministratorDTO.setUsername("admin");

        when(authService.getAdministratorInfo()).thenReturn(expectedAdministratorDTO);

        ResponseEntity<AdministratorDTO> responseEntity = authenticationController.getAdministratorInfo();

        assertEquals(expectedAdministratorDTO, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}

