package com.example.ludogorieSoft.village.controllers;
import com.example.ludogorieSoft.village.dtos.ObjectVillageDTO;
import com.example.ludogorieSoft.village.services.ObjectVillageService;
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

@WebMvcTest(ObjectVillageController.class)
@AutoConfigureMockMvc
class ObjectVillageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObjectVillageService objectVillageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllObjectVillages() throws Exception {

        ObjectVillageDTO objectVillageDTO1 = new ObjectVillageDTO();
        objectVillageDTO1.setId(1L);
        ObjectVillageDTO objectVillageDTO2 = new ObjectVillageDTO();
        objectVillageDTO2.setId(2L);
        List<ObjectVillageDTO> objectVillageDTOList = Arrays.asList(objectVillageDTO1, objectVillageDTO2);

        when(objectVillageService.getAllObjectVillages()).thenReturn(objectVillageDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectVillages")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetObjectVillageByID() throws Exception {

        ObjectVillageDTO objectVillageDTO = new ObjectVillageDTO();
        objectVillageDTO.setId(1L);

        when(objectVillageService.getObjectVillageById(anyLong())).thenReturn(objectVillageDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectVillages/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateObjectVillageByID() throws Exception {

        ObjectVillageDTO objectVillageDTO = new ObjectVillageDTO();
        objectVillageDTO.setId(1L);

        when(objectVillageService.updateObjectVillageById(anyLong(), any(ObjectVillageDTO.class))).thenReturn(objectVillageDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/objectVillages/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateObjectVillage() throws Exception {

        ObjectVillageDTO objectVillageDTO = new ObjectVillageDTO();
        objectVillageDTO.setId(1L);

        when(objectVillageService.createObjectVillage(any(ObjectVillageDTO.class))).thenReturn(objectVillageDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/objectVillages")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testDeleteObjectVillageById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/objectVillages/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("ObjectVillage with id: 1 has been deleted successfully!!"));
    }
}
