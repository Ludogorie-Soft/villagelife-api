package com.example.ludogorieSoft.village.controllers;
import com.example.ludogorieSoft.village.dtos.PopulatedAssertionDTO;
import com.example.ludogorieSoft.village.services.PopulatedAssertionService;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PopulatedAssertionController.class)
@AutoConfigureMockMvc
public class PopulatedAssertionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PopulatedAssertionService populatedAssertionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPopulatedAssertion() throws Exception {

        PopulatedAssertionDTO populatedAssertionDTO1 = new PopulatedAssertionDTO();
        populatedAssertionDTO1.setId(1L);
        PopulatedAssertionDTO populatedAssertionDTO2 = new PopulatedAssertionDTO();
        populatedAssertionDTO2.setId(2L);
        List<PopulatedAssertionDTO> populatedAssertionDTOList = Arrays.asList(populatedAssertionDTO1, populatedAssertionDTO2);

        when(populatedAssertionService.getAllPopulatedAssertion()).thenReturn(populatedAssertionDTOList);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populated_assertions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetPopulatedAssertionById() throws Exception {

        PopulatedAssertionDTO populatedAssertionDTO = new PopulatedAssertionDTO();
        populatedAssertionDTO.setId(1L);

        when(populatedAssertionService.getPopulatedAssertionById(anyLong())).thenReturn(populatedAssertionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/populated_assertions/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testCreatePopulatedAssertion() throws Exception {

        PopulatedAssertionDTO populatedAssertionDTO = new PopulatedAssertionDTO();
        populatedAssertionDTO.setId(1L);

        when(populatedAssertionService.createPopulatedAssertion(any(PopulatedAssertionDTO.class))).thenReturn(populatedAssertionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/populated_assertions")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())

                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testUpdatePopulatedAssertionById() throws Exception {

        PopulatedAssertionDTO populatedAssertionDTO = new PopulatedAssertionDTO();
        populatedAssertionDTO.setId(1L);

        when(populatedAssertionService.updatePopulatedAssertion(anyLong(), any(PopulatedAssertionDTO.class))).thenReturn(populatedAssertionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/populated_assertions/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testDeletePopulatedAssertionById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/populated_assertions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("PopulatedAssertion with id: 1 has been deleted successfully!!"));
    }
}
