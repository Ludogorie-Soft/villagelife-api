package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageLandscapeDTO;
import com.example.ludogorieSoft.village.model.VillageLandscape;
import com.example.ludogorieSoft.village.services.VillageLandscapeService;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VillageLandscapeController.class)
@AutoConfigureMockMvc
class VillageLandscapeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VillageLandscapeService villageLandscapeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVillageLandscapes() throws Exception {

        VillageLandscapeDTO villageLandscapeDTO1 = new VillageLandscapeDTO();
        villageLandscapeDTO1.setId(1L);
        VillageLandscapeDTO villageLandscapeDTO2 = new VillageLandscapeDTO();
        villageLandscapeDTO2.setId(2L);
        List<VillageLandscapeDTO> villageLandscapeDTOList = Arrays.asList(villageLandscapeDTO1, villageLandscapeDTO2);

        when(villageLandscapeService.getAllVillageLandscapes()).thenReturn(villageLandscapeDTOList);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLandscapes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetVillageLandscapeById() throws Exception {

        VillageLandscapeDTO villageLandscapeDTO = new VillageLandscapeDTO();
        villageLandscapeDTO.setId(1L);

        when(villageLandscapeService.getVillageLandscapeById(anyLong())).thenReturn(villageLandscapeDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLandscapes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateVillageLandscape() throws Exception {

        VillageLandscapeDTO villageLandscapeDTO = new VillageLandscapeDTO();
        villageLandscapeDTO.setId(1L);
        URI location = UriComponentsBuilder.fromPath("/api/v1/villageLandscape/{id}")
                .buildAndExpand(1L)
                .toUri();

        when(villageLandscapeService.createVillageLandscape(any(VillageLandscapeDTO.class))).thenReturn(villageLandscapeDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/villageLandscapes")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateVillageLandscape() throws Exception {

        VillageLandscapeDTO villageLandscapeDTO = new VillageLandscapeDTO();
        villageLandscapeDTO.setId(1L);

        when(villageLandscapeService.updateVillageLandscape(anyLong(), any(VillageLandscapeDTO.class))).thenReturn(villageLandscapeDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/villageLandscapes/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testDeleteVillageLandscapeById() throws Exception {

        when(villageLandscapeService.deleteVillageLandscapeById(anyLong())).thenReturn(1);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageLandscapes/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
