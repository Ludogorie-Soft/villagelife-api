package com.example.ludogorieSoft.village.auth;

import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.dtos.request.AuthenticationRequest;
import com.example.ludogorieSoft.village.dtos.request.RegisterRequest;
import com.example.ludogorieSoft.village.dtos.request.VerificationRequest;
import com.example.ludogorieSoft.village.dtos.response.AuthenticationResponce;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setMobile("1234567890");
        request.setUsername("johndoe");
        request.setPassword("password");
        request.setRole(Role.USER);

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn("Registration successful");

        ResponseEntity<String> response = authenticationController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration successful", response.getBody());
        verify(authenticationService, times(1)).register(request);
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("johndoe");
        request.setPassword("password");

        AuthenticationResponce authResponse = new AuthenticationResponce();
        authResponse.setToken("jwt-token");

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponce> response = authenticationController.authenticate(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody().getToken());
        verify(authenticationService, times(1)).authenticate(request);
    }

    @Test
    void testGetAdministratorInfo() {
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setFullName("Admin User");

        when(authService.getAdministratorInfo()).thenReturn(alternativeUserDTO);

        ResponseEntity<AlternativeUserDTO> response = authenticationController.getAdministratorInfo();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(alternativeUserDTO, response.getBody());
        verify(authService, times(1)).getAdministratorInfo();
    }

    @Test
    void testAuthorizeAdminToken() {
        String token = "Bearer some-token";

        ResponseEntity<String> response = authenticationController.authorizeAdminToken(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Authorized", response.getBody());
    }

    @Test
    void testVerifyVerificationToken() {
        VerificationRequest verificationRequest = new VerificationRequest();
        verificationRequest.setToken("verification-token");
        verificationRequest.setEmail("john.doe@example.com");

        when(authenticationService.verifyVerificationToken(any(VerificationRequest.class))).thenReturn("Your account is verified!");

        ResponseEntity<String> response = authenticationController.verifyVerificationToken(verificationRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Your account is verified!", response.getBody());
        verify(authenticationService, times(1)).verifyVerificationToken(verificationRequest);
    }
}