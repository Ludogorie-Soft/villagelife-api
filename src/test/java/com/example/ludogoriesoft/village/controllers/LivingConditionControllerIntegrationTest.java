package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.LivingConditionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.services.LivingConditionService;
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
@WebMvcTest(value = LivingConditionController.class
        , useDefaultFilters = false
        , includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = LivingConditionController.class
        )
}
)
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
    void testGetAllLivingConditionsWhenNoLivingConditionExist() throws Exception {
        when(livingConditionService.getAllLivingConditions()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/livingConditions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }

    @Test
    void testGetLivingConditionByIdWhenLivingConditionDoesNotExist() throws Exception {
        long livingConditionId = 1L;
        when(livingConditionService.getLivingConditionById(livingConditionId))
                .thenThrow(new ApiRequestException("Living Condition with id: " + livingConditionId + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/livingConditions/{id}", livingConditionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Living Condition with id: " + livingConditionId + " not found")));

    }


    @Test
    void testShouldNotCreateLivingConditionWithBlankLivingConditionName() throws Exception {
        String blankLivingConditionName = "";

        doThrow(new ApiRequestException("Living Condition is blank"))
                .when(livingConditionService).createLivingCondition(any(LivingConditionDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/livingConditions")
                        .content("{\"id\": 1, \"livingConditionName\": \"" + blankLivingConditionName + "\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Living Condition is blank"))
                .andReturn();
    }


    @Test
    void testUpdateLivingConditionWithInvalidIdShouldReturnNotFound() throws Exception {
        Long id = 1L;

        LivingConditionDTO updatedLivingCondition = new LivingConditionDTO();

        when(livingConditionService.updateLivingCondition(id, updatedLivingCondition))
                .thenThrow(new ApiRequestException("Living Condition id: " + id + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/livingConditions/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedLivingCondition)))
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
    void testDeleteLivingConditionByIdWhenLivingConditionDoesNotExist() throws Exception {
        long livingConditionId = 1L;
        doThrow(new ApiRequestException("Living Condition with id: " + livingConditionId + " not found"))
                .when(livingConditionService).deleteLivingCondition(livingConditionId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/livingConditions/{id}", livingConditionId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Living Condition with id: " + livingConditionId + " not found")));
    }

}
