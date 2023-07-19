package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageLivingConditionDTO;
import com.example.ludogorieSoft.village.enums.Consents;
import com.example.ludogorieSoft.village.exceptions.ApiRequestException;
import com.example.ludogorieSoft.village.services.VillageLivingConditionService;
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
import java.util.Collections;
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
        villageLivingConditionDTO1.setVillageId(1L);
        villageLivingConditionDTO1.setLivingConditionId(1L);
        villageLivingConditionDTO1.setConsents(Consents.COMPLETELY_AGREED);
        VillageLivingConditionDTO villageLivingConditionDTO2 = new VillageLivingConditionDTO();
        villageLivingConditionDTO2.setId(2L);
        villageLivingConditionDTO2.setVillageId(2L);
        villageLivingConditionDTO2.setLivingConditionId(2L);
        villageLivingConditionDTO2.setConsents(Consents.CANT_DECIDE);

        List<VillageLivingConditionDTO> villageLivingConditionDTOList = Arrays.asList(villageLivingConditionDTO1, villageLivingConditionDTO2);

        when(villageLivingConditionService.getAllVillageLivingConditions()).thenReturn(villageLivingConditionDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].villageId").value(1))
                .andExpect(jsonPath("$.[0].livingConditionId").value(1))
                .andExpect(jsonPath("$.[0].consents").value("COMPLETELY_AGREED"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].villageId").value(2))
                .andExpect(jsonPath("$.[1].livingConditionId").value(2))
                .andExpect(jsonPath("$.[1].consents").value("CANT_DECIDE"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetVillageLivingConditionsById() throws Exception {
        VillageLivingConditionDTO villageLivingConditionDTO = new VillageLivingConditionDTO();
        villageLivingConditionDTO.setId(3L);
        villageLivingConditionDTO.setVillageId(5L);
        villageLivingConditionDTO.setLivingConditionId(2L);
        villageLivingConditionDTO.setConsents(Consents.DISAGREE);

        when(villageLivingConditionService.getByID(anyLong())).thenReturn(villageLivingConditionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.villageId").value(5))
                .andExpect(jsonPath("$.livingConditionId").value(2))
                .andExpect(jsonPath("$.consents").value("DISAGREE"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
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

    @Test
    void testGetAllVillageLivingConditionsWhenNoneExist() throws Exception {
        when(villageLivingConditionService.getAllVillageLivingConditions()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }


    @Test
    void testGetVillageLivingConditionsByVillageId() throws Exception {
        Long villageId = 1L;

        VillageLivingConditionDTO villageLivingConditionDTO1 = new VillageLivingConditionDTO();
        villageLivingConditionDTO1.setId(1L);
        villageLivingConditionDTO1.setVillageId(villageId);
        villageLivingConditionDTO1.setLivingConditionId(1L);
        villageLivingConditionDTO1.setConsents(Consents.COMPLETELY_AGREED);

        VillageLivingConditionDTO villageLivingConditionDTO2 = new VillageLivingConditionDTO();
        villageLivingConditionDTO2.setId(2L);
        villageLivingConditionDTO2.setVillageId(villageId);
        villageLivingConditionDTO2.setLivingConditionId(2L);
        villageLivingConditionDTO2.setConsents(Consents.CANT_DECIDE);

        List<VillageLivingConditionDTO> villageLivingConditionDTOList = Arrays.asList(villageLivingConditionDTO1, villageLivingConditionDTO2);

        when(villageLivingConditionService.getVillagePopulationAssertionByVillageId(villageId)).thenReturn(villageLivingConditionDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions/village/{id}", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].villageId").value(villageId))
                .andExpect(jsonPath("$[0].livingConditionId").value(1L))
                .andExpect(jsonPath("$[0].consents").value("COMPLETELY_AGREED"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].villageId").value(villageId))
                .andExpect(jsonPath("$[1].livingConditionId").value(2L))
                .andExpect(jsonPath("$[1].consents").value("CANT_DECIDE"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetVillageLivingConditionsByVillageIdWhenNoneExist() throws Exception {
        Long villageId = 1L;

        when(villageLivingConditionService.getVillagePopulationAssertionByVillageId(villageId)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions/village/{id}", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }

    @Test
    void testGetVillageLivingConditionsByVillageIdWithInvalidId() throws Exception {
        Long invalidId = 100000L;

        when(villageLivingConditionService.getVillagePopulationAssertionByVillageId(anyLong()))
                .thenThrow(new ApiRequestException("Village with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions/village/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Village with id: " + invalidId + " Not Found"))
                .andReturn();
    }


    @Test
    void testGetVillagePopulationAssertionByVillageIdValue() throws Exception {
        Long villageId = 1L;
        double populationAssertionValue = 123.45;

        when(villageLivingConditionService.getVillagePopulationAssertionByVillageIdValue(villageId)).thenReturn(populationAssertionValue);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions/village/value/{id}", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(populationAssertionValue)))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetVillagePopulationAssertionByVillageIdValueWithInvalidId() throws Exception {
        Long invalidId = 100000L;

        when(villageLivingConditionService.getVillagePopulationAssertionByVillageIdValue(anyLong()))
                .thenThrow(new ApiRequestException("Village with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions/village/value/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Village with id: " + invalidId + " Not Found"))
                .andReturn();
    }


    @Test
    void testGetVillagePopulationAssertionByVillageIdDelinquencyValue() throws Exception {
        Long villageId = 1L;
        double delinquencyValue = 78.9;

        when(villageLivingConditionService.getVillagePopulationAssertionByVillageIdDelinquencyValue(villageId)).thenReturn(delinquencyValue);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions/village/delinquencyValue/{id}", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(delinquencyValue)))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetVillagePopulationAssertionByVillageIdDelinquencyValueWithInvalidId() throws Exception {
        Long invalidId = 100000L;

        when(villageLivingConditionService.getVillagePopulationAssertionByVillageIdDelinquencyValue(anyLong()))
                .thenThrow(new ApiRequestException("Village with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions/village/delinquencyValue/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Village with id: " + invalidId + " Not Found"))
                .andReturn();
    }


    @Test
    void testGetVillagePopulationAssertionByVillageIdEcoValue() throws Exception {
        Long villageId = 1L;
        double ecoValue = 34.56;

        when(villageLivingConditionService.getVillagePopulationAssertionByVillageIdEcoValue(villageId)).thenReturn(ecoValue);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions/village/ecoValue/{id}", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(ecoValue)))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetVillagePopulationAssertionByVillageIdEcoValueWithInvalidId() throws Exception {
        Long invalidId = 100000L;

        when(villageLivingConditionService.getVillagePopulationAssertionByVillageIdEcoValue(anyLong()))
                .thenThrow(new ApiRequestException("Village with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageLivingConditions/village/ecoValue/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Village with id: " + invalidId + " Not Found"))
                .andReturn();
    }

    @Test
    void testDeleteVillageLivingConditionsById() throws Exception {
        Long villageLivingConditionId = 1L;

        when(villageLivingConditionService.deleteVillageLivingConditions(villageLivingConditionId)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageLivingConditions/{id}", villageLivingConditionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }


    @Test
    void testDeleteVillageLivingConditionsByIdWhenNoRowsAffected() throws Exception {
        Long villageLivingConditionId = 1L;

        when(villageLivingConditionService.deleteVillageLivingConditions(villageLivingConditionId)).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageLivingConditions/{id}", villageLivingConditionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}
