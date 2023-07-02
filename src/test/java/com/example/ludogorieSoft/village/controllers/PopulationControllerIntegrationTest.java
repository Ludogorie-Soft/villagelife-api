package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PopulationDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.enums.Foreigners;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.enums.Residents;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.services.PopulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PopulationController.class)
@AutoConfigureMockMvc
class PopulationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PopulationService populationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPopulation() throws Exception {
        PopulationDTO populationDTO1 = new PopulationDTO();
        populationDTO1.setId(1L);
        populationDTO1.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);
        populationDTO1.setResidents(Residents.FROM_21_TO_30_PERCENT);
        populationDTO1.setChildren(Children.FROM_21_TO_50);
        populationDTO1.setForeigners(Foreigners.I_DONT_KNOW);
        PopulationDTO populationDTO2 = new PopulationDTO();
        populationDTO2.setId(2L);
        populationDTO2.setNumberOfPopulation(NumberOfPopulation.FROM_2000_PEOPLE);
        populationDTO2.setResidents(Residents.UP_TO_2_PERCENT);
        populationDTO2.setChildren(Children.OVER_50);
        populationDTO2.setForeigners(Foreigners.NO);
        List<PopulationDTO> populationDTOList = Arrays.asList(populationDTO1, populationDTO2);

        when(populationService.getAllPopulation()).thenReturn(populationDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].numberOfPopulation").value("UP_TO_10_PEOPLE"))
                .andExpect(jsonPath("$.[0].residents").value("FROM_21_TO_30_PERCENT"))
                .andExpect(jsonPath("$.[0].children").value("FROM_21_TO_50"))
                .andExpect(jsonPath("$.[0].foreigners").value("I_DONT_KNOW"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].numberOfPopulation").value("FROM_2000_PEOPLE"))
                .andExpect(jsonPath("$.[1].residents").value("UP_TO_2_PERCENT"))
                .andExpect(jsonPath("$.[1].children").value("OVER_50"))
                .andExpect(jsonPath("$.[1].foreigners").value("NO"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetPopulationById() throws Exception {
        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setId(1L);
        populationDTO.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);
        populationDTO.setResidents(Residents.FROM_21_TO_30_PERCENT);
        populationDTO.setChildren(Children.FROM_21_TO_50);
        populationDTO.setForeigners(Foreigners.I_DONT_KNOW);

        when(populationService.getPopulationById(anyLong())).thenReturn(populationDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populations/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numberOfPopulation").value("UP_TO_10_PEOPLE"))
                .andExpect(jsonPath("$.residents").value("FROM_21_TO_30_PERCENT"))
                .andExpect(jsonPath("$.children").value("FROM_21_TO_50"))
                .andExpect(jsonPath("$.foreigners").value("I_DONT_KNOW"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreatePopulation() throws Exception {
        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setId(1L);
        populationDTO.setNumberOfPopulation(NumberOfPopulation.FROM_11_TO_50_PEOPLE);
        populationDTO.setResidents(Residents.FROM_2_TO_5_PERCENT);
        populationDTO.setChildren(Children.BELOW_10);
        populationDTO.setForeigners(Foreigners.YES);
        when(populationService.createPopulation(any(PopulationDTO.class))).thenReturn(populationDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/populations")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numberOfPopulation").value("FROM_11_TO_50_PEOPLE"))
                .andExpect(jsonPath("$.residents").value("FROM_2_TO_5_PERCENT"))
                .andExpect(jsonPath("$.children").value("BELOW_10"))
                .andExpect(jsonPath("$.foreigners").value("YES"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdatePopulation() throws Exception {
        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setId(1L);
        populationDTO.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);
        populationDTO.setResidents(Residents.FROM_2_TO_5_PERCENT);
        populationDTO.setChildren(Children.BELOW_10);
        populationDTO.setForeigners(Foreigners.I_DONT_KNOW);

        when(populationService.updatePopulation(anyLong(), any(PopulationDTO.class))).thenReturn(populationDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/populations/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numberOfPopulation").value("UP_TO_10_PEOPLE"))
                .andExpect(jsonPath("$.residents").value("FROM_2_TO_5_PERCENT"))
                .andExpect(jsonPath("$.children").value("BELOW_10"))
                .andExpect(jsonPath("$.foreigners").value("I_DONT_KNOW"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testDeletePopulationById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/populations/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Population with id: 1 has been deleted successfully!!"));
    }


    @Test
    void testGetAllPopulationWhenNoPopulationExists() throws Exception {
        when(populationService.getAllPopulation()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }

    @Test
    void testGetPopulationByIdWhenPopulationDoesNotExist() throws Exception {
        Long invalidId = 100000L;

        when(populationService.getPopulationById(invalidId))
                .thenThrow(new ApiRequestException("Population with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populations/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Population with id: " + invalidId + " Not Found"))
                .andReturn();
    }


    @Test
    void testCreatePopulationWithInvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/populations")
                        .content("{\"id\": }")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    void testUpdatePopulationWhenPopulationDoesNotExist() throws Exception {
        Long invalidId = 100000L;

        when(populationService.updatePopulation(eq(invalidId), any(PopulationDTO.class)))
                .thenThrow(new ApiRequestException("Population with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/populations/{id}", invalidId)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Population with id: " + invalidId + " Not Found"))
                .andReturn();
    }

    @Test
    void testDeletePopulationByIdWhenPopulationDoesNotExist() throws Exception {
        Long invalidId = 10L;
        String errorMessage = "Population not found for id " + invalidId;

        doThrow(new ApiRequestException(errorMessage))
                .when(populationService).deletePopulationById(invalidId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/populations/{id}", invalidId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andReturn();
    }

}

