package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.AdministratorRequest;
import com.example.ludogorieSoft.village.services.AdministratorService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AllArgsConstructor
class AdministratorControllerIntegrationWithSQLTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AdministratorService administratorService;

    @LocalServerPort
    private int port;

    AdministratorControllerIntegrationWithSQLTest() {
    }

    private String getRootUrl() {
        return "http://localhost:" + port + "/api/v1/admins";
    }

    @Test
    public void testGetAllAdministrators() {

        ResponseEntity<List<AdministratorDTO>> response = restTemplate.exchange(
                getRootUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AdministratorDTO>>() {
                });

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Add more assertions as needed
    }

    @Test
    public void testUpdateAdministrator() {
        // Arrange
        Long id = 1L;
        AdministratorRequest administratorRequest = new AdministratorRequest();
        // Set properties of the administratorRequest object

        // Act
        restTemplate.put(
                getRootUrl() + "/" + id,
                administratorRequest);

        // Assert
        // Verify that the administrator was updated successfully using administratorService or other means
        // Add assertions as needed
    }

    @Test
    public void testDeleteAdministratorById() {
        // Arrange
        Long id = 1L;

        // Act
        restTemplate.delete(
                getRootUrl() + "/" + id);

        // Assert
        // Verify that the administrator was deleted successfully using administratorService or other means
        // Add assertions as needed
    }
}

