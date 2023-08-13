package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.authorization.JWTService;
import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.repositories.EthnicityVillageRepository;
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
        AdministratorDTO createdAdministrator1 = new AdministratorDTO();
        createdAdministrator1.setId(1L);
        createdAdministrator1.setUsername("username1");
        AdministratorDTO createdAdministrator2 = new AdministratorDTO();
        createdAdministrator2.setId(1L);
        createdAdministrator2.setUsername("username2");
        List<AdministratorDTO> administrators = Arrays.asList(createdAdministrator1, createdAdministrator2);
        when(administratorService.getAllAdministrators()).thenReturn(administrators);

        ResponseEntity<List<AdministratorDTO>> response = administratorController.getAllAdministrators();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(administrators, response.getBody());
    }

    @Test
    void getAdministratorById_shouldReturnAdministrator() {
        Long administratorId = 1L;
        AdministratorDTO administrator = new AdministratorDTO();
        administrator.setId(administratorId);
        administrator.setUsername("username");
        when(administratorService.getAdministratorById(administratorId)).thenReturn(administrator);

        ResponseEntity<AdministratorDTO> response = administratorController.getAdministratorById(administratorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(administrator, response.getBody());
    }

    @Test
    void createAdministrator_shouldReturnCreatedAdministrator() {
        AdministratorRequest request = new AdministratorRequest();
        request.setUsername("username");
        AdministratorDTO createdAdministrator = new AdministratorDTO();
        createdAdministrator.setId(1L);
        createdAdministrator.setUsername("username");
        when(administratorService.createAdministrator(request)).thenReturn(createdAdministrator);

        ResponseEntity<AdministratorDTO> response = administratorController.createAdministrator(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdAdministrator, response.getBody());
    }

    @Test
    void updateAdministrator_shouldReturnUpdatedAdministrator() {
        Long administratorId = 1L;
        AdministratorRequest request = new AdministratorRequest();
        request.setUsername("username");
        AdministratorDTO updatedAdministrator = new AdministratorDTO();
        updatedAdministrator.setId(administratorId);
        updatedAdministrator.setUsername("username");
        when(administratorService.updateAdministrator(administratorId, request)).thenReturn(updatedAdministrator);

        ResponseEntity<AdministratorDTO> response = administratorController.updateAdministrator(administratorId, request);

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


//    @Test
//    void getAllVillages_shouldReturnListOfVillageResponses() {
//        VillageResponse villageResponse1 = new VillageResponse();
//        villageResponse1.setId(1L);
//        VillageResponse villageResponse2 = new VillageResponse();
//        villageResponse2.setId(3L);
//        List<VillageResponse> villages = Arrays.asList(villageResponse1,villageResponse2);
//
//        when(villageService.getAllVillagesWithAdmin()).thenReturn(villages);
//
//        List<VillageResponse> response = administratorController.getAllVillages();
//
//        assertEquals(villages, response);
//    }

}
