package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.EthnicityDTO;
import com.example.ludogorieSoft.village.services.EthnicityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EthnicityControllerIntegrationWithSQLTest {
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private EthnicityService ethnicityService;
//
//    private String createURL(String uri) {
//        return "http://localhost:" + port + uri;
//    }
//
//    @Test
//    @Sql(statements = {
//            "INSERT INTO ethnicities (id, ethnicity_name) VALUES (1, 'Ethnicity 1')",
//            "INSERT INTO ethnicities (id, ethnicity_name) VALUES (2, 'Ethnicity 2')"
//    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(statements = "DELETE FROM ethnicities", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    void getAllEthnicities() {
//        ResponseEntity<List<EthnicityDTO>> response = restTemplate.exchange(
//                createURL("/api/v1/ethnicities"), HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<EthnicityDTO>>() {});
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        List<EthnicityDTO> ethnicities = response.getBody();
//        assertNotNull(ethnicities);
//        assertEquals(2, ethnicities.size());
//    }
//
//
//    @Test
//    @Sql(statements = {
//            "INSERT INTO ethnicities (id, ethnicity_name) VALUES (1, 'Ethnicity 1')",
//            "INSERT INTO ethnicities (id, ethnicity_name) VALUES (2, 'Ethnicity 2')"
//    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(statements = "DELETE FROM ethnicities", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    void getEthnicityById() {
//        long ethnicityId = 1L;
//
//        ResponseEntity<EthnicityDTO> response = restTemplate.getForEntity(
//                createURL("/api/v1/ethnicities/" + ethnicityId), EthnicityDTO.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        EthnicityDTO ethnicity = response.getBody();
//        assertNotNull(ethnicity);
//        assertEquals("Ethnicity 1", ethnicity.getEthnicityName());
//    }
//
//    @Test
//    @Sql(statements = "DELETE FROM ethnicities", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(statements = "DELETE FROM ethnicities", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    void createEthnicity() {
//        EthnicityDTO ethnicityDTO = new EthnicityDTO();
//        ethnicityDTO.setEthnicityName("New Ethnicity");
//
//        ResponseEntity<EthnicityDTO> response = restTemplate.postForEntity(
//                createURL("/api/v1/ethnicities"), ethnicityDTO, EthnicityDTO.class);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        EthnicityDTO createdEthnicity = response.getBody();
//        assertNotNull(createdEthnicity);
//        assertEquals("New Ethnicity", createdEthnicity.getEthnicityName());
//    }
//
//
//    @Test
//    @Sql(statements = {
//            "INSERT INTO ethnicities (id, ethnicity_name) VALUES (1, 'Ethnicity 1')"
//    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(statements = "DELETE FROM ethnicities", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    void updateEthnicity() {
//        long ethnicityId = 1L;
//        EthnicityDTO updatedEthnicityDTO = new EthnicityDTO();
//        updatedEthnicityDTO.setEthnicityName("Updated Ethnicity");
//
//        HttpEntity<EthnicityDTO> entity = new HttpEntity<>(updatedEthnicityDTO);
//        ResponseEntity<EthnicityDTO> response = restTemplate.exchange(
//                createURL("/api/v1/ethnicities/" + ethnicityId), HttpMethod.PUT, entity, EthnicityDTO.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        EthnicityDTO updatedEthnicity = response.getBody();
//        assertNotNull(updatedEthnicity);
//        assertEquals("Updated Ethnicity", updatedEthnicity.getEthnicityName());
//    }
//
//    @Test
//    @Sql(statements = {
//            "INSERT INTO ethnicities (id, ethnicity_name) VALUES (1, 'Ethnicity 1')"
//    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(statements = "DELETE FROM ethnicities", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//     void deleteEthnicityById() {
//        long ethnicityId = 1L;
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                createURL("/api/v1/ethnicities/" + ethnicityId), HttpMethod.DELETE, null, String.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        String message = response.getBody();
//        assertNotNull(message);
//        assertEquals("Ethnicity with id: 1 has been deleted successfully!!", message);
//    }
}
