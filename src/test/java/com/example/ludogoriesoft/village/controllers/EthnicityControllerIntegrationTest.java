package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.EthnicityDTO;
import com.example.ludogorieSoft.village.services.EthnicityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = EthnicityController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = EthnicityController.class
                )
        }
)
class EthnicityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EthnicityService ethnicityService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEthnicities() throws Exception {
        EthnicityDTO ethnicityDTO1 = new EthnicityDTO(1L, "Ethnicity 1");
        EthnicityDTO ethnicityDTO2 = new EthnicityDTO(2L, "Ethnicity 2");

        List<EthnicityDTO> ethnicityDTOList = Arrays.asList(ethnicityDTO1, ethnicityDTO2);

        when(ethnicityService.getAllEthnicities()).thenReturn(ethnicityDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ethnicities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].ethnicityName").value("Ethnicity 1"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].ethnicityName").value("Ethnicity 2"))
                .andReturn();
    }

    @Test
    void testGetEthnicityById() throws Exception {
        Long ethnicityId = 1L;
        EthnicityDTO ethnicityDTO = new EthnicityDTO(ethnicityId, "Ethnicity 1");

        when(ethnicityService.getEthnicityById(ethnicityId)).thenReturn(ethnicityDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ethnicities/{id}", ethnicityId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ethnicityName").value("Ethnicity 1"))
                .andReturn();
    }

    @Test
    void testCreateEthnicity() throws Exception {
        EthnicityDTO ethnicityDTO = new EthnicityDTO(null, "New Ethnicity");

        EthnicityDTO createdEthnicityDTO = new EthnicityDTO(1L, "New Ethnicity");

        when(ethnicityService.createEthnicity(any(EthnicityDTO.class))).thenReturn(createdEthnicityDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/ethnicities")
                        .content(objectMapper.writeValueAsString(ethnicityDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ethnicityName").value("New Ethnicity"))
                .andReturn();
    }

    @Test
    void testUpdateEthnicity() throws Exception {
        Long ethnicityId = 1L;
        EthnicityDTO ethnicityDTO = new EthnicityDTO(ethnicityId, "Updated Ethnicity");

        EthnicityDTO updatedEthnicityDTO = new EthnicityDTO(ethnicityId, "Updated Ethnicity");

        when(ethnicityService.updateEthnicity(anyLong(), any(EthnicityDTO.class))).thenReturn(updatedEthnicityDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/ethnicities/{id}", ethnicityId)
                        .content(objectMapper.writeValueAsString(ethnicityDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ethnicityName").value("Updated Ethnicity"))
                .andReturn();
    }

    @Test
    void testDeleteEthnicityById() throws Exception {
        Long ethnicityId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/ethnicities/{id}", ethnicityId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Ethnicity with id: " + ethnicityId + " has been deleted successfully!!"))
                .andReturn();
    }
}