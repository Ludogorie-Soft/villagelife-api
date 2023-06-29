package com.example.ludogoriesoft.village.controllers;


import com.example.ludogoriesoft.village.dtos.RegionDTO;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import com.example.ludogoriesoft.village.services.RegionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegionController.class)
@AutoConfigureMockMvc
class RegionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegionService regionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRegions() throws Exception {
        RegionDTO regionDTO1 = new RegionDTO();
        regionDTO1.setId(1L);
        regionDTO1.setRegionName("Region 1");
        RegionDTO regionDTO2 = new RegionDTO();
        regionDTO2.setId(2L);
        regionDTO2.setRegionName("Region 2");
        List<RegionDTO> regionDTOList = Arrays.asList(regionDTO1, regionDTO2);

        when(regionService.getAllRegions()).thenReturn(regionDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].regionName").value("Region 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].regionName").value("Region 2"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }
    @Test
    void testGetRegionById() throws Exception {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1L);
        regionDTO.setRegionName("Region 1");

        when(regionService.getRegionById(anyLong())).thenReturn(regionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regions/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.regionName").value("Region 1"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }

    @Test
    void testCreateRegion() throws Exception {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1L);
        regionDTO.setRegionName("New Region");

        when(regionService.createRegion(any(RegionDTO.class))).thenReturn(regionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"regionName\": \"New Region\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.regionName").value("New Region"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }
    @Test
    void testUpdateRegion() throws Exception {
        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1L);
        regionDTO.setRegionName("Updated Region");

        when(regionService.updateRegion(anyLong(), any(RegionDTO.class))).thenReturn(regionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/regions/{id}", 1)
                        .content("{\"id\": 1, \"regionName\": \"Updated Region\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.regionName").value("Updated Region"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }
    @Test
    void testDeleteRegionById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/regions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Region with id: 1 has been deleted successfully!!"));
    }
    @Test
    void testGetAllRegionsWhenNoRegionExist() throws Exception {
        when(regionService.getAllRegions()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }
    @Test
    void testGetRegionByIdWhenRegionDoesNotExist() throws Exception {
        long regionId = 1L;
        when(regionService.getRegionById(regionId))
                .thenThrow(new ApiRequestException("Region with id: " + regionId + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regions/{id}", regionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Region with id: " + regionId + " not found")));
    }
    @Test
    void testShouldNotCreateRegionWithBlankRegionName() throws Exception {
        String blankRegionName = "";

        doThrow(new ApiRequestException("Region name is blank"))
                .when(regionService).createRegion(any(RegionDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/regions")
                        .content("{\"id\": 1, \"regionName\": \"" + blankRegionName + "\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Region name is blank"))
                .andReturn();
    }
    @Test
    void testGetRegionWithInvalidId() throws Exception {
        Long invalidId = 100000L;

        when(regionService.getRegionById(invalidId))
                .thenThrow(new ApiRequestException("Region with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/regions/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Region with id: " + invalidId + " Not Found"))
                .andReturn();
    }
    @Test
    void testUpdateRegionWithInvalidData() throws Exception {
        String invalidData = "";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/regions/{id}", 1)
                        .content("{\"id\": 1, \"name\": " + invalidData + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testUpdateRegionWithInvalidIdShouldReturnNotFound() throws Exception {
        Long id = 1L;

        RegionDTO updatedRegion = new RegionDTO();

        when(regionService.updateRegion(id, updatedRegion))
                .thenThrow(new ApiRequestException("Region with id: " + id + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/regions/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedRegion)))
                .andExpect(status().isBadRequest());
    }
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void testDeleteRegionByIdWhenRegionDoesNotExist() throws Exception {
        long regionId = 1L;
        doThrow(new ApiRequestException("Region with id: " + regionId + " not found"))
                .when(regionService).deleteRegionById(regionId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/regions/{id}", regionId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Region with id: " + regionId + " not found")));
    }


}
