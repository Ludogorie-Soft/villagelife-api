package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdministratorControllerTest {
    @Mock
    private AdministratorService administratorService;
    AdministratorController administratorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        administratorController = new AdministratorController(administratorService);
    }

    @Test
    void getAllAdministrators_shouldReturnListOfAdministrators() {
        AlternativeUserDTO createdAdministrator1 = new AlternativeUserDTO();
        createdAdministrator1.setId(1L);
        createdAdministrator1.setUsername("username1");
        AlternativeUserDTO createdAdministrator2 = new AlternativeUserDTO();
        createdAdministrator2.setId(1L);
        createdAdministrator2.setUsername("username2");
        List<AlternativeUserDTO> administrators = Arrays.asList(createdAdministrator1, createdAdministrator2);
        when(administratorService.getAllAdministrators()).thenReturn(administrators);

        ResponseEntity<List<AlternativeUserDTO>> response = administratorController.getAllAdministrators();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(administrators, response.getBody());
    }

    @Test
    void getAdministratorById_shouldReturnAdministrator() {
        Long administratorId = 1L;
        AlternativeUserDTO administrator = new AlternativeUserDTO();
        administrator.setId(administratorId);
        administrator.setUsername("username");
        when(administratorService.getAdministratorById(administratorId)).thenReturn(administrator);

        ResponseEntity<AlternativeUserDTO> response = administratorController.getAdministratorById(administratorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(administrator, response.getBody());
    }

    @Test
    void updateAdministrator_shouldReturnUpdatedAdministrator() {
        Long administratorId = 1L;
        AdministratorRequest request = new AdministratorRequest();
        request.setUsername("username");
        AlternativeUserDTO updatedAdministrator = new AlternativeUserDTO();
        updatedAdministrator.setId(administratorId);
        updatedAdministrator.setUsername("username");
        when(administratorService.updateAdministrator(administratorId, request)).thenReturn(updatedAdministrator);

        ResponseEntity<AlternativeUserDTO> response = administratorController.updateAdministrator(administratorId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAdministrator, response.getBody());
    }

    @Test
    void deleteAdministratorById_shouldReturnSuccessMessage() {
        Long administratorId = 1L;

        ResponseEntity<String> response = administratorController.deleteAdministratorById(administratorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Administrator with id: " + administratorId + " has been deleted successfully!!", response.getBody());
        verify(administratorService, times(1)).deleteAdministratorById(administratorId);
    }
}
