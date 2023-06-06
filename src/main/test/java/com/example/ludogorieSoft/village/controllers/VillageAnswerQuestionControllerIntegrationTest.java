package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageAnswerQuestionDTO;
import com.example.ludogorieSoft.village.model.VillageAnswerQuestion;
import com.example.ludogorieSoft.village.services.VillageAnswerQuestionService;
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

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VillageAnswerQuestionController.class)
@AutoConfigureMockMvc
class VillageAnswerQuestionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VillageAnswerQuestionService villageAnswerQuestionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVillageAnswerQuestions() throws Exception {

        VillageAnswerQuestionDTO villageAnswerQuestionDTO1 = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO1.setId(1L);
        VillageAnswerQuestionDTO villageAnswerQuestionDTO2 = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO2.setId(2L);
        List<VillageAnswerQuestionDTO> villageAnswerQuestionDTOList = Arrays.asList(villageAnswerQuestionDTO1, villageAnswerQuestionDTO2);

        when(villageAnswerQuestionService.getAllVillageAnswerQuestions()).thenReturn(villageAnswerQuestionDTOList);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageAnswerQuestion")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetVillageAnswerQuestionById() throws Exception {

        VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO.setId(1L);

        when(villageAnswerQuestionService.getVillageAnswerQuestionById(anyLong())).thenReturn(villageAnswerQuestionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageAnswerQuestion/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateVillageAnswerQuestion() throws Exception {

        VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO.setId(1L);

        when(villageAnswerQuestionService.createVillageAnswerQuestion(any(VillageAnswerQuestionDTO.class))).thenReturn(villageAnswerQuestionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/villageAnswerQuestion")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateVillageAnswerQuestion() throws Exception {

        VillageAnswerQuestionDTO villageAnswerQuestionDTO = new VillageAnswerQuestionDTO();
        villageAnswerQuestionDTO.setId(1L);

        when(villageAnswerQuestionService.updateVillageAnswerQuestion(anyLong(), any(VillageAnswerQuestionDTO.class))).thenReturn(villageAnswerQuestionDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/villageAnswerQuestion/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }


    @Test
    void testDeleteVillageAnswerQuestionById() throws Exception {

        int rowsAffected = 1;
        when(villageAnswerQuestionService.deleteVillageAnswerQuestionById(anyLong())).thenReturn(rowsAffected);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageAnswerQuestion/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteVillageAnswerQuestionById_NotFound() throws Exception {

        int rowsAffected = 0;
        when(villageAnswerQuestionService.deleteVillageAnswerQuestionById(anyLong())).thenReturn(rowsAffected);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageAnswerQuestion/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
