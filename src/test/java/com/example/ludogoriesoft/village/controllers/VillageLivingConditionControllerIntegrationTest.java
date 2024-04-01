package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageLivingConditionDTO;
import com.example.ludogorieSoft.village.enums.Consents;
import com.example.ludogorieSoft.village.services.VillageLivingConditionService;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = VillageLivingConditionController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = VillageLivingConditionController.class
                )
        }
)
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
    void testCreateVillageLivingConditions() throws Exception {
        VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO();
        villageLivingConditionDTO.setId(4L);
        villageLivingConditionDTO.setVillageId(3L);
        villageLivingConditionDTO.setLivingConditionId(6L);
        villageLivingConditionDTO.setConsents(Consents.RATHER_AGREE);

        when(villageLivingConditionService.createVillageLivingCondition(any(VillageLivingConditionDTO.class))).thenReturn(villageLivingConditionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/villageLivingConditions")
                        .content("{\"id\": 4}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.villageId").value(3))
                .andExpect(jsonPath("$.livingConditionId").value(6))
                .andExpect(jsonPath("$.consents").value("RATHER_AGREE"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateVillageLivingConditions() throws Exception {
        VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO();
        villageLivingConditionDTO.setId(1L);
        villageLivingConditionDTO.setVillageId(3L);
        villageLivingConditionDTO.setLivingConditionId(7L);
        villageLivingConditionDTO.setConsents(Consents.COMPLETELY_AGREED);

        when(villageLivingConditionService.updateVillageLivingCondition(anyLong(), any(VillageLivingConditionDTO.class))).thenReturn(villageLivingConditionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/villageLivingConditions/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.villageId").value(3))
                .andExpect(jsonPath("$.livingConditionId").value(7))
                .andExpect(jsonPath("$.consents").value("COMPLETELY_AGREED"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }


}
