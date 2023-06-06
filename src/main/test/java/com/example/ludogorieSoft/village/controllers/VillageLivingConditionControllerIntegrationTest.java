package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageLivingConditionDTO;
import com.example.ludogorieSoft.village.model.VillageLivingConditions;
import com.example.ludogorieSoft.village.services.VillageLivingConditionService;
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

@WebMvcTest(VillageLivingConditionController.class)
@AutoConfigureMockMvc
class VillageLivingConditionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VillageLivingConditionService villageLivingConditionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVillageLivingConditions() throws Exception {

        VillageLivingConditionDTO villageLivingConditionDTO1 = new VillageLivingConditionDTO();
        villageLivingConditionDTO1.setId(1L);
        VillageLivingConditionDTO villageLivingConditionDTO2 = new VillageLivingConditionDTO();
        villageLivingConditionDTO2.setId(2L);
        List<VillageLivingConditionDTO> villageLivingConditionDTOList = Arrays.asList(villageLivingConditionDTO1, villageLivingConditionDTO2);

        when(villageLivingConditionService.getAllVillageLivingConditions()).thenReturn(villageLivingConditionDTOList);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetVillageLivingConditionsById() throws Exception {

        VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO();
        villageLivingConditionDTO.setId(1L);

        when(villageLivingConditionService.getByID(anyLong())).thenReturn(villageLivingConditionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateVillageLivingConditions() throws Exception {

        VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO();
        villageLivingConditionDTO.setId(1L);
        URI location = UriComponentsBuilder.fromPath("/api/v1/villageLivingConditions/{id}")
                .buildAndExpand(1L)
                .toUri();

        when(villageLivingConditionService.createVillageLivingCondition(any(VillageLivingConditionDTO.class))).thenReturn(villageLivingConditionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/villageLivingConditions")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateVillageLivingConditions() throws Exception {

        VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO();
        villageLivingConditionDTO.setId(1L);

        when(villageLivingConditionService.updateVillageLivingCondition(anyLong(), any(VillageLivingConditionDTO.class))).thenReturn(villageLivingConditionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/villageLivingConditions/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testDeleteVillageLivingConditionsById() throws Exception {

        when(villageLivingConditionService.deleteVillageLivingConditions(anyLong())).thenReturn(1);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageLivingConditions/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
