package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.AdministratorRequest;
import com.example.ludogorieSoft.village.services.AdministratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdministratorControllerIntegrationTestWithSQL {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AdministratorService administratorService;


    private String createURL(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    @Sql(statements = "INSERT INTO admins (id, full_name, email, username, password, mobile) VALUES (1, 'John Doe', 'john@example.com', 'johndoe', 'password', '1234567890')", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM admins WHERE id = '1'", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void getAllAdministrators() {
        HttpEntity<String> entity = new HttpEntity<>(null);
        ResponseEntity<List<AdministratorDTO>> response = restTemplate.exchange(
                createURL("/api/v1/admins"), HttpMethod.GET, entity, new ParameterizedTypeReference<List<AdministratorDTO>>() {
                });
        List<AdministratorDTO> administratorList = response.getBody();
        assert administratorList != null;
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(administratorList.size(), administratorService.getAllAdministrators().size());
    }


    @Test
    @Sql(statements = "INSERT INTO admins (id, full_name, email, username, password, mobile) VALUES (1, 'John Doe', 'john@example.com', 'johndoe', 'password','1234567890')", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM admins WHERE id = '1'", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void getAdministratorById() {
        long administratorId = 1L;

        HttpEntity<String> entity = new HttpEntity<>(null);
        ResponseEntity<AdministratorDTO> response = restTemplate.exchange(
                createURL("/api/v1/admins/" + administratorId), HttpMethod.GET, entity, AdministratorDTO.class);
        AdministratorDTO administrator = response.getBody();
        assert administrator != null;
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", administrator.getFullName());
        assertEquals("john@example.com", administrator.getEmail());
        assertEquals("johndoe", administrator.getUsername());
        assertEquals("1234567890", administrator.getMobile());

    }

    @Test
    @Sql(statements = "DELETE FROM admins", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM admins", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void createAdministrator() {
        AdministratorRequest administratorRequest = new AdministratorRequest();
        administratorRequest.setFullName("John Doe");
        administratorRequest.setEmail("john@example.com");
        administratorRequest.setUsername("johndoe");
        administratorRequest.setPassword("password");
        administratorRequest.setMobile("1234567890");

        HttpEntity<AdministratorRequest> requestEntity = new HttpEntity<>(administratorRequest);
        ResponseEntity<Void> response = restTemplate.postForEntity(createURL("/api/v1/admins"), requestEntity, Void.class);

        assertEquals(201, response.getStatusCodeValue());
        URI location = response.getHeaders().getLocation();
        assertNotNull(location);
    }

    @Test
    @Sql(statements = "INSERT INTO admins (id, full_name, email, username, password, mobile) VALUES (1, 'John Doe', 'john@example.com', 'johndoe', 'password', '1234567890')", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM admins WHERE id = '1'", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void updateAdministrator() {
        long administratorId = 1L;

        AdministratorRequest administratorRequest = new AdministratorRequest();
        administratorRequest.setFullName("Updated Name");
        administratorRequest.setEmail("updated@example.com");
        administratorRequest.setUsername("updated");
        administratorRequest.setPassword("newpassword");
        administratorRequest.setMobile("9876543210");

        HttpEntity<AdministratorRequest> requestEntity = new HttpEntity<>(administratorRequest);
        ResponseEntity<AdministratorDTO> response = restTemplate.exchange(
                createURL("/api/v1/admins/" + administratorId), HttpMethod.PUT, requestEntity, AdministratorDTO.class);

        assertEquals(200, response.getStatusCodeValue());
        AdministratorDTO updatedAdministrator = response.getBody();
        assertNotNull(updatedAdministrator);
        assertEquals("Updated Name", updatedAdministrator.getFullName());
        assertEquals("updated@example.com", updatedAdministrator.getEmail());
        assertEquals("updated", updatedAdministrator.getUsername());
        assertEquals("9876543210", updatedAdministrator.getMobile());

    }


    @Test
    @Sql(statements = "INSERT INTO admins (id, full_name, email, username, password, mobile) VALUES (1, 'John Doe', 'john@example.com', 'johndoe', 'password', '1234567890')", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM admins WHERE id = '1'", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void deleteAdministratorById() {
        long administratorId = 1L;

        ResponseEntity<String> response = restTemplate.exchange(
                createURL("/api/v1/admins/" + administratorId), HttpMethod.DELETE, null, String.class);

        assertEquals(200, response.getStatusCodeValue());
        String responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Administrator with id: 1 has been deleted successfully!!", responseBody);
    }



}
