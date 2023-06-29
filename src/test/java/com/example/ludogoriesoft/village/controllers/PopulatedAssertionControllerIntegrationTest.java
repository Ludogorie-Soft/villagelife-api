package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PopulatedAssertionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.services.PopulatedAssertionService;
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

@WebMvcTest(PopulatedAssertionController.class)
@AutoConfigureMockMvc
class PopulatedAssertionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PopulatedAssertionService populatedAssertionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPopulatedAssertion() throws Exception {
        PopulatedAssertionDTO populatedAssertionDTO1 = new PopulatedAssertionDTO();
        populatedAssertionDTO1.setId(1L);
        populatedAssertionDTO1.setPopulatedAssertionName("Populated Assertion 1");
        PopulatedAssertionDTO populatedAssertionDTO2 = new PopulatedAssertionDTO();
        populatedAssertionDTO2.setId(2L);
        populatedAssertionDTO2.setPopulatedAssertionName("Populated Assertion 2");

        List<PopulatedAssertionDTO> populatedAssertionDTOList = Arrays.asList(populatedAssertionDTO1, populatedAssertionDTO2);

        when(populatedAssertionService.getAllPopulatedAssertion()).thenReturn(populatedAssertionDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populatedAssertions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].populatedAssertionName").value("Populated Assertion 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].populatedAssertionName").value("Populated Assertion 2"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetPopulatedAssertionById() throws Exception {
        PopulatedAssertionDTO populatedAssertionDTO = new PopulatedAssertionDTO();
        populatedAssertionDTO.setId(1L);
        populatedAssertionDTO.setPopulatedAssertionName("Populated Assertion 1");

        when(populatedAssertionService.getPopulatedAssertionById(anyLong())).thenReturn(populatedAssertionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populatedAssertions/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.populatedAssertionName").value("Populated Assertion 1"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreatePopulatedAssertion() throws Exception {
        PopulatedAssertionDTO populatedAssertionDTO = new PopulatedAssertionDTO();
        populatedAssertionDTO.setId(1L);
        populatedAssertionDTO.setPopulatedAssertionName("New Populated Assertion");

        when(populatedAssertionService.createPopulatedAssertion(any(PopulatedAssertionDTO.class))).thenReturn(populatedAssertionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/populatedAssertions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"populatedAssertionName\": \"New Populated Assertion\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.populatedAssertionName").value("New Populated Assertion"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdatePopulatedAssertionById() throws Exception {
        PopulatedAssertionDTO populatedAssertionDTO = new PopulatedAssertionDTO();
        populatedAssertionDTO.setId(1L);
        populatedAssertionDTO.setPopulatedAssertionName("Updated Populated Assertion");

        when(populatedAssertionService.updatePopulatedAssertion(anyLong(), any(PopulatedAssertionDTO.class))).thenReturn(populatedAssertionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/populatedAssertions/{id}", 1)
                        .content("{\"id\": 1, \"populatedAssertionName\": \"Updated Populated Assertion\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.populatedAssertionName").value("Updated Populated Assertion"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testDeletePopulatedAssertionById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/populatedAssertions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("PopulatedAssertion with id: 1 has been deleted successfully!!"));
    }


    @Test
    void testGetAllPopulatedAssertionWhenNoPopulatedAssertionExists() throws Exception {
        when(populatedAssertionService.getAllPopulatedAssertion()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populatedAssertions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }

    @Test
    void testGetPopulatedAssertionByIdWhenPopulatedAssertionNotFound() throws Exception {
        Long invalidId = 100000L;

        when(populatedAssertionService.getPopulatedAssertionById(invalidId))
                .thenThrow(new ApiRequestException("Populated Assertion with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populatedAssertions/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Populated Assertion with id: " + invalidId + " Not Found"))
                .andReturn();
    }


    @Test
    void testShouldNotCreatePopulatedAssertionWithBlankPopulatedAssertionName() throws Exception {
        String blankPopulatedAssertionName = "";

        doThrow(new ApiRequestException("Populated Assertion is blank"))
                .when(populatedAssertionService).createPopulatedAssertion(any(PopulatedAssertionDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/populatedAssertions")
                        .content("{\"id\": 1, \"populatedAssertionName\": \"" + blankPopulatedAssertionName + "\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Populated Assertion is blank"))
                .andReturn();
    }

    @Test
    void testUpdatePopulatedAssertionByIdWhenPopulatedAssertionNotFound() throws Exception {
        Long invalidId = 100000L;

        when(populatedAssertionService.updatePopulatedAssertion(eq(invalidId), any(PopulatedAssertionDTO.class)))
                .thenThrow(new ApiRequestException("Populated Assertion with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/populatedAssertions/{id}", invalidId)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Populated Assertion with id: " + invalidId + " Not Found"))
                .andReturn();
    }



}
