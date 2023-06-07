package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.LivingConditionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.services.LivingConditionService;
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
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivingConditionController.class)
@AutoConfigureMockMvc
class LivingConditionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivingConditionService livingConditionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLivingConditions() throws Exception {

        LivingConditionDTO livingConditionDTO1 = new LivingConditionDTO();
        livingConditionDTO1.setId(1L);
        livingConditionDTO1.setLivingConditionName("Living condition 1");
        LivingConditionDTO livingConditionDTO2 = new LivingConditionDTO();
        livingConditionDTO2.setId(2L);
        livingConditionDTO2.setLivingConditionName("Living condition 2");
        List<LivingConditionDTO> livingConditionDTOList = Arrays.asList(livingConditionDTO1, livingConditionDTO2);

        when(livingConditionService.getAllLivingConditions()).thenReturn(livingConditionDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/livingConditions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].livingConditionName").value("Living condition 1"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].livingConditionName").value("Living condition 2"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetLivingConditionById() throws Exception {
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setId(1L);
        livingConditionDTO.setLivingConditionName("Living condition 1");

        when(livingConditionService.getLivingConditionById(anyLong())).thenReturn(livingConditionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/livingConditions/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.livingConditionName").value("Living condition 1"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateLivingCondition() throws Exception {
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setId(1L);
        livingConditionDTO.setLivingConditionName("New Living condition");

        when(livingConditionService.createLivingCondition(any(LivingConditionDTO.class))).thenReturn(livingConditionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/livingConditions")
                        .content("{\"id\": 1, \"livingConditionName\": \"New Living condition\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.livingConditionName").value("New Living condition"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateLivingCondition() throws Exception {
        LivingConditionDTO livingConditionDTO = new LivingConditionDTO();
        livingConditionDTO.setId(1L);
        livingConditionDTO.setLivingConditionName("Updated Living condition");

        when(livingConditionService.updateLivingCondition(anyLong(), any(LivingConditionDTO.class))).thenReturn(livingConditionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/livingConditions/{id}", 1)
                        .content("{\"id\": 1, \"livingConditionName\": \"Updated Living condition\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.livingConditionName").value("Updated Living condition"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testDeleteLivingConditionById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/livingConditions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("LivingCondition with id: 1 has been deleted successfully!!"));
    }


    @Test
    void testGetLivingConditionByIdWhenLivingConditionDoesNotExist() throws Exception {
        long livingConditionId = 1L;
        when(livingConditionService.getLivingConditionById(livingConditionId))
                .thenThrow(new ApiRequestException("LivingCondition with id: " + livingConditionId + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/livingConditions/{id}", livingConditionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("LivingCondition with id: " + livingConditionId + " not found")));

    }


    @Test
    void testCreateLivingConditionWithInvalidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/livingConditions")
                        .content("{\"id\": 1, \"livingConditionName\": null}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    void testDeleteLivingConditionByIdWhenLivingConditionDoesNotExist() throws Exception {
        long livingConditionId = 1L;
        doThrow(new ApiRequestException("LivingCondition with id: " + livingConditionId + " not found"))
                .when(livingConditionService).deleteLivingCondition(livingConditionId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/livingConditions/{id}", livingConditionId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("LivingCondition with id: " + livingConditionId + " not found")));
    }

}

