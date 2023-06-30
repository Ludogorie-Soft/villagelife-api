package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.AdministratorRequest;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.services.AdministratorService;
import lombok.AllArgsConstructor;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class AdministratorControllerIntegrationWithSQLTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AdministratorService administratorService;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port + "/api/v1/admins";
    }

    @Before
    public void setup() {
        AdministratorRequest administrator = new AdministratorRequest();
        administrator.setFullName("John Doe");
        administrator.setEmail("john.doe@example.com");
        administrator.setUsername("johndoe");
        administrator.setPassword("password");
        administrator.setMobile("123456789");

        AdministratorDTO administratorDTO = administratorService.createAdministrator(administrator);
        Long ADid = administratorDTO.getId();
    }

    @After
    public void cleanup() {
        administratorService.deleteAdministratorById(1L);
    }

    @Test
    void testGetAllAdministrators() {

        ResponseEntity<List<AdministratorDTO>> response = restTemplate.exchange(
                getRootUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AdministratorDTO>>() {
                });

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


}

