package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.services.ObjectAroundVillageService;
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

@WebMvcTest(ObjectAroundVillageController.class)
@AutoConfigureMockMvc
public class ObjectAroundVillageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObjectAroundVillageService objectAroundVillageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllObjectsAroundVillage() throws Exception {

        ObjectAroundVillageDTO objectAroundVillageDTO1 = new ObjectAroundVillageDTO();
        objectAroundVillageDTO1.setId(1L);
        ObjectAroundVillageDTO objectAroundVillageDTO2 = new ObjectAroundVillageDTO();
        objectAroundVillageDTO2.setId(2L);
        List<ObjectAroundVillageDTO> objectAroundVillageDTOList = Arrays.asList(objectAroundVillageDTO1, objectAroundVillageDTO2);

        when(objectAroundVillageService.getAllObjectsAroundVillage()).thenReturn(objectAroundVillageDTOList);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectAroundVillage")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetObjectAroundVillageByID() throws Exception {

        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setId(1L);

        when(objectAroundVillageService.getObjectAroundVillageById(anyLong())).thenReturn(objectAroundVillageDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectAroundVillage/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testCreateObjectAroundVillage() throws Exception {

        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setId(1L);

        when(objectAroundVillageService.createObjectAroundVillage(any(ObjectAroundVillageDTO.class))).thenReturn(objectAroundVillageDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/objectAroundVillage")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testUpdateObjectAroundVillage() throws Exception {

        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setId(1L);

        when(objectAroundVillageService.updateObjectAroundVillage(anyLong(), any(ObjectAroundVillageDTO.class))).thenReturn(objectAroundVillageDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/objectAroundVillage/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testDeleteObjectAroundVillageById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/objectAroundVillage/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("ObjectAroundVillage with id: 1 has been deleted successfully!!"));
    }
}
