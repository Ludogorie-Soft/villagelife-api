package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.authorization.JWTService;
import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest {
    @Mock
    private AdministratorService administratorService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private JWTService jwtService;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAdministratorInfo() {
        String authHeader = "Bearer your-jwt-token";
        when(request.getHeader("Authorization")).thenReturn(authHeader);

        String jwtToken = authHeader.substring(7);
        String username = "testuser";
        when(jwtService.extractUsername(jwtToken)).thenReturn(username);

        AlternativeUserDTO expectedAdminDTO = new AlternativeUserDTO();
        expectedAdminDTO.setUsername(username);
        when(administratorService.findAdminByUsername(username)).thenReturn(expectedAdminDTO);

        AlternativeUserDTO result = authService.getAdministratorInfo();

        verify(request).getHeader("Authorization");
        verify(jwtService).extractUsername(jwtToken);
        verify(administratorService).findAdminByUsername(username);

        assertEquals(expectedAdminDTO, result);
    }

}