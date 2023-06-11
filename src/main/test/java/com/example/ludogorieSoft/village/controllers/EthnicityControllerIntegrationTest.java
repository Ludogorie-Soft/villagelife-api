package com.example.ludogoriesoft.village.controllers;

import com.example.ludogoriesoft.village.dtos.EthnicityDTO;
import com.example.ludogoriesoft.village.services.EthnicityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EthnicityController.class)
class EthnicityControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EthnicityService ethnicityService;

    private List<EthnicityDTO> ethnicityList;

    @BeforeEach
    void setUp() {
        EthnicityDTO ethnicity1 = new EthnicityDTO();
        ethnicity1.setId(1L);
        ethnicity1.setEthnicityName("Ethnicity 1");

        EthnicityDTO ethnicity2 = new EthnicityDTO();
        ethnicity2.setId(2L);
        ethnicity2.setEthnicityName("Ethnicity 2");

        ethnicityList = Arrays.asList(ethnicity1, ethnicity2);
    }

    @Test
    void testGetAllEthnicities() throws Exception {
        when(ethnicityService.getAllEthnicities()).thenReturn(ethnicityList);

        mockMvc.perform(get("/api/v1/ethnicities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ethnicityName").value("Ethnicity 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].ethnicityName").value("Ethnicity 2"));
    }

    @Test
    void testGetEthnicityById() throws Exception {
        long ethnicityId = 1L;
        EthnicityDTO ethnicity = new EthnicityDTO();
        ethnicity.setId(ethnicityId);
        ethnicity.setEthnicityName("Ethnicity 1");

        when(ethnicityService.getEthnicityById(ethnicityId)).thenReturn(ethnicity);

        mockMvc.perform(get("/api/v1/ethnicities/{id}", ethnicityId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ethnicityName").value("Ethnicity 1"));
    }

    @Test
    void testCreateEthnicity() throws Exception {
        long ethnicityId = 1L;
        EthnicityDTO ethnicityDTO = new EthnicityDTO();
        ethnicityDTO.setId(ethnicityId);
        ethnicityDTO.setEthnicityName("New Ethnicity");

        when(ethnicityService.createEthnicity(any(EthnicityDTO.class))).thenReturn(ethnicityDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/ethnicities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"ethnicityName\": \"New Ethnicity\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ethnicityName").value("New Ethnicity"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateEthnicity() throws Exception {
        long ethnicityId = 1L;
        EthnicityDTO ethnicityDTO = new EthnicityDTO();
        ethnicityDTO.setId(ethnicityId);
        ethnicityDTO.setEthnicityName("Updated Ethnicity");

        when(ethnicityService.updateEthnicity(anyLong(), any(EthnicityDTO.class))).thenReturn(ethnicityDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/ethnicities/{id}", ethnicityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"ethnicityName\": \"Updated Ethnicity\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ethnicityName").value("Updated Ethnicity"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }


    @Test
    void testDeleteEthnicityById() throws Exception {
        long ethnicityId = 1L;

        mockMvc.perform(delete("/api/v1/ethnicities/{id}", ethnicityId))
                .andExpect(status().isOk())
                .andExpect(content().string("Ethnicity with id: " + ethnicityId + " has been deleted successfully!!"));
    }


}