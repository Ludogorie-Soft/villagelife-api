package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.LandscapeDTO;
import com.example.ludogorieSoft.village.exeptions.ApiExceptionHandler;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.services.LandscapeService;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = LandscapeController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = LandscapeController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = ApiExceptionHandler.class
                )
        }
)
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
        landscapeDTO1.setLandscapeName("Landscape 1");
        LandscapeDTO landscapeDTO2 = new LandscapeDTO();
        landscapeDTO2.setId(2L);
        landscapeDTO2.setLandscapeName("Landscape 2");
        List<LandscapeDTO> landscapeDTOList = Arrays.asList(landscapeDTO1, landscapeDTO2);

        when(landscapeService.getAllLandscapes()).thenReturn(landscapeDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/landscapes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].landscapeName").value("Landscape 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].landscapeName").value("Landscape 2"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }


    @Test
    void testGetLandscapeById() throws Exception {
        LandscapeDTO landscapeDTO = new LandscapeDTO();
        landscapeDTO.setId(1L);
        landscapeDTO.setLandscapeName("Landscape 1");

        when(landscapeService.getLandscapeById(anyLong())).thenReturn(landscapeDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/landscapes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.landscapeName").value("Landscape 1"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }


    @Test
    void testCreateLandscape() throws Exception {
        long landscapeId = 1L;
        LandscapeDTO landscapeDTO = new LandscapeDTO();
        landscapeDTO.setId(landscapeId);
        landscapeDTO.setLandscapeName("New Landscape");

        when(landscapeService.createLandscape(any(LandscapeDTO.class))).thenReturn(landscapeDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/landscapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"landscapeName\": \"New Landscape\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.landscapeName").value("New Landscape"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }


    @Test
    void testUpdateLandscape() throws Exception {
        long landscapeId = 1L;
        LandscapeDTO ethnicityDTO = new LandscapeDTO();
        ethnicityDTO.setId(landscapeId);
        ethnicityDTO.setLandscapeName("Updated Landscape");

        when(landscapeService.updateLandscape(anyLong(), any(LandscapeDTO.class))).thenReturn(ethnicityDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/landscapes/{id}", landscapeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"landscapeName\": \"Updated Landscape\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.landscapeName").value("Updated Landscape"))
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

    @Test
    void testGetAllLandscapesWhenNoLandscapeExist() throws Exception {
        when(landscapeService.getAllLandscapes()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/landscapes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }

    @Test
    void testGetLandscapeByIdWhenLandscapeDoesNotExist() throws Exception {
        long landscapeId = 1L;
        when(landscapeService.getLandscapeById(landscapeId))
                .thenThrow(new ApiRequestException("Landscape with id: " + landscapeId + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/landscapes/{id}", landscapeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Landscape with id: " + landscapeId + " not found")));
    }

    @Test
    void testShouldNotCreateLandscapeWithBlankLandscapeName() throws Exception {
        String blankLandscapeName = "";

        doThrow(new ApiRequestException("Landscape is blank"))
                .when(landscapeService).createLandscape(any(LandscapeDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/landscapes")
                        .content("{\"id\": 1, \"landscapeName\": \"" + blankLandscapeName + "\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Landscape is blank"))
                .andReturn();
    }

    @Test
    void testUpdateLandscapeWithInvalidIdShouldReturnNotFound() throws Exception {
        Long id = 1L;

        LandscapeDTO updatedLandscape = new LandscapeDTO();

        when(landscapeService.updateLandscape(id, updatedLandscape))
                .thenThrow(new ApiRequestException("Landscape with id: " + id + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/landscapes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedLandscape)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testDeleteLandscapeWithInvalidIdShouldReturnBadRequest() throws Exception {
        Long id = 1L;

        doThrow(new ApiRequestException("Landscape with id " + id + " not found"))
                .when(landscapeService).deleteLandscape(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/landscapes/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Landscape with id " + id + " not found")));
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
