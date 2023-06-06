package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.LandscapeDTO;
import com.example.ludogorieSoft.village.services.LandscapeService;
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

@WebMvcTest(LandscapeController.class)
@AutoConfigureMockMvc
class LandscapeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LandscapeService landscapeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLandscapes() throws Exception {

        LandscapeDTO landscapeDTO1 = new LandscapeDTO();
        landscapeDTO1.setId(1L);
        LandscapeDTO landscapeDTO2 = new LandscapeDTO();
        landscapeDTO2.setId(2L);
        List<LandscapeDTO> landscapeDTOList = Arrays.asList(landscapeDTO1, landscapeDTO2);

        when(landscapeService.getAllLandscapes()).thenReturn(landscapeDTOList);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/landscapes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetLandscapeById() throws Exception {

        LandscapeDTO landscapeDTO = new LandscapeDTO();
        landscapeDTO.setId(1L);

        when(landscapeService.getLandscapeById(anyLong())).thenReturn(landscapeDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/landscapes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateLandscape() throws Exception {

        LandscapeDTO landscapeDTO = new LandscapeDTO();
        landscapeDTO.setId(1L);

        when(landscapeService.createLandscape(any(LandscapeDTO.class))).thenReturn(landscapeDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/landscapes")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateLandscape() throws Exception {

        LandscapeDTO landscapeDTO = new LandscapeDTO();
        landscapeDTO.setId(1L);

        when(landscapeService.updateLandscape(anyLong(), any(LandscapeDTO.class))).thenReturn(landscapeDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/landscapes/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testDeleteLandscapeById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/landscapes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Landscape with id: 1 has been deleted successfully!!"));
    }
}

