package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.services.PopulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PopulationController.class)
@AutoConfigureMockMvc
public class PopulationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PopulationService populationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPopulation() throws Exception {

        PopulationDTO populationDTO1 = new PopulationDTO();
        populationDTO1.setId(1L);
        PopulationDTO populationDTO2 = new PopulationDTO();
        populationDTO2.setId(2L);
        List<PopulationDTO> populationDTOList = Arrays.asList(populationDTO1, populationDTO2);

        when(populationService.getAllPopulation()).thenReturn(populationDTOList);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetPopulationById() throws Exception {

        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setId(1L);

        when(populationService.getPopulationById(anyLong())).thenReturn(populationDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populations/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testCreatePopulation() throws Exception {

        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setId(1L);

        when(populationService.createPopulation(any(PopulationDTO.class))).thenReturn(populationDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/populations")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testUpdatePopulation() throws Exception {

        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setId(1L);

        when(populationService.updatePopulation(anyLong(), any(PopulationDTO.class))).thenReturn(populationDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/populations/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testDeletePopulationById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/populations/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Population with id: 1 has been deleted successfully!!"));
    }
}

