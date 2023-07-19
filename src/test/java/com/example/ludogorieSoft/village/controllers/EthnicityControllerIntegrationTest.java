package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.EthnicityDTO;
import com.example.ludogorieSoft.village.services.EthnicityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EthnicityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EthnicityService ethnicityService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEthnicitiesShouldReturnListOfEthnicities() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ethnicities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].ethnicityName").exists())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].ethnicityName").exists())
                .andReturn();
    }

    @Test
    void getEthnicityByIdWithValidIdShouldReturnEthnicity() throws Exception {
        Long validEthnicityId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ethnicities/{id}", validEthnicityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validEthnicityId))
                .andExpect(jsonPath("$.ethnicityName").exists())
                .andReturn();
    }


    @Test
    void updateEthnicityWithValidIdShouldReturnUpdatedEthnicity() throws Exception {
        Long validEthnicityId = 1L;

        EthnicityDTO updatedEthnicity = new EthnicityDTO();
        updatedEthnicity.setEthnicityName("Updated Ethnicity");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/ethnicities/{id}", validEthnicityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEthnicity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validEthnicityId))
                .andExpect(jsonPath("$.ethnicityName").value("Updated Ethnicity"))
                .andReturn();
    }


    @Test
    void testCreateEthnicityWithInvalidInput() throws Exception {
        EthnicityDTO ethnicityDTO = new EthnicityDTO();
        ethnicityDTO.setId(1L);

        ObjectMapper objectMapper = new ObjectMapper();
        String ethnicityJson = objectMapper.writeValueAsString(ethnicityDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/ethnicities")
                        .content(ethnicityJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}
