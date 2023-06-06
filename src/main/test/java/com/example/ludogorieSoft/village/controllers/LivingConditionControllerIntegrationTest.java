package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.LivingConditionDTO;
import com.example.ludogorieSoft.village.services.LivingConditionService;
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

@WebMvcTest(LivingConditionController.class)
@AutoConfigureMockMvc
public class LivingConditionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivingConditionService livingConditionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllLivingConditions() throws Exception {

        LivingConditionDTO livingConditionDTO1 = new LivingConditionDTO();
        livingConditionDTO1.setId(1L);
        LivingConditionDTO livingConditionDTO2 = new LivingConditionDTO();
        livingConditionDTO2.setId(2L);
        List<LivingConditionDTO> livingConditionDTOList = Arrays.asList(livingConditionDTO1, livingConditionDTO2);

        when(livingConditionService.getAllLivingConditions()).thenReturn(livingConditionDTOList);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/livingConditions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetLivingConditionById() throws Exception {

        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setId(1L);

        when(livingConditionService.getLivingConditionById(anyLong())).thenReturn(livingConditionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/livingConditions/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testCreateLivingCondition() throws Exception {

        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setId(1L);

        when(livingConditionService.createLivingCondition(any(LivingConditionDTO.class))).thenReturn(livingConditionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/livingConditions")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testUpdateLivingCondition() throws Exception {

        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setId(1L);

        when(livingConditionService.updateLivingCondition(anyLong(), any(LivingConditionDTO.class))).thenReturn(livingConditionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/livingConditions/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testDeleteLivingConditionById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/livingConditions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("LivingCondition with id: 1 has been deleted successfully!!"));
    }
}

