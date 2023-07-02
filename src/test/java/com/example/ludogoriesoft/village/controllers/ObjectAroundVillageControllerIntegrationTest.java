package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.ObjectAroundVillageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.ObjectAroundVillage;
import com.example.ludogorieSoft.village.services.ObjectAroundVillageService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.containsString;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ObjectAroundVillageController.class)
@AutoConfigureMockMvc
class ObjectAroundVillageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObjectAroundVillageService objectAroundVillageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllObjectsAroundVillage() throws Exception {
        ObjectAroundVillageDTO objectAroundVillageDTO1 = new ObjectAroundVillageDTO();
        objectAroundVillageDTO1.setId(1L);
        objectAroundVillageDTO1.setType("Object 1");
        ObjectAroundVillageDTO objectAroundVillageDTO2 = new ObjectAroundVillageDTO();
        objectAroundVillageDTO2.setId(2L);
        objectAroundVillageDTO2.setType("Object 2");
        List<ObjectAroundVillageDTO> objectAroundVillageDTOList = Arrays.asList(objectAroundVillageDTO1, objectAroundVillageDTO2);

        when(objectAroundVillageService.getAllObjectsAroundVillage()).thenReturn(objectAroundVillageDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectsAroundVillage")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].type").value("Object 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].type").value("Object 2"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetObjectAroundVillageByID() throws Exception {
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setId(1L);
        objectAroundVillageDTO.setType("Object 1");

        when(objectAroundVillageService.getObjectAroundVillageById(anyLong())).thenReturn(objectAroundVillageDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectsAroundVillage/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("Object 1"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateObjectAroundVillage() throws Exception {
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setId(1L);
        objectAroundVillageDTO.setType("New Object");

        when(objectAroundVillageService.createObjectAroundVillage(any(ObjectAroundVillageDTO.class))).thenReturn(objectAroundVillageDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/objectsAroundVillage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"type\": \"New Object\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("New Object"))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateObjectAroundVillage() throws Exception {
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setId(1L);
        objectAroundVillageDTO.setType("Updated Object");

        when(objectAroundVillageService.updateObjectAroundVillage(anyLong(), any(ObjectAroundVillageDTO.class))).thenReturn(objectAroundVillageDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/objectsAroundVillage/{id}", 1)
                        .content("{\"id\": 1, \"type\": \"Updated Object\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("Updated Object"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testDeleteObjectAroundVillageById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/objectsAroundVillage/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("ObjectAroundVillage with id: 1 has been deleted successfully!!"));
    }

    @Test
    void testGetAllLObjectsAroundVillageWhenNoObjectAroundVillageExist() throws Exception {
        when(objectAroundVillageService.getAllObjectsAroundVillage()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectsAroundVillage")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }

    @Test
    void testGetObjectAroundVillageByIdWhenObjectDoesNotExist() throws Exception {
        long objectId = 1L;
        when(objectAroundVillageService.getObjectAroundVillageById(objectId))
                .thenThrow(new ApiRequestException("ObjectAroundVillage with id: " + objectId + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectsAroundVillage/{id}", objectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("ObjectAroundVillage with id: " + objectId + " not found")));
    }



    @Test
    void testShouldNotCreateObjectAroundWithBlankObjectAround() throws Exception {
        String blankObjectAroundVillage = "";

        doThrow(new ApiRequestException("Object Around Village is blank"))
                .when(objectAroundVillageService).createObjectAroundVillage(any(ObjectAroundVillageDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/objectsAroundVillage")
                        .content("{\"id\": 1, \"type\": \"" + blankObjectAroundVillage + "\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Object Around Village is blank"))
                .andReturn();
    }
    @Test
    void testGetObjectAroundVillageWithInvalidId() throws Exception {
        Long invalidId = 100000L;

        when(objectAroundVillageService.getObjectAroundVillageById(invalidId))
                .thenThrow(new ApiRequestException("Object Around Village with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectsAroundVillage/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Object Around Village with id: " + invalidId + " Not Found"))
                .andReturn();
    }


    @Test
    void testUpdateObjectAroundVillageWithInvalidData() throws Exception {
        String invalidData = "";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/objectsAroundVillage/{id}", 1)
                        .content("{\"id\": 1, \"type\": }" + invalidData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateObjectAroundVillageWithInvalidIdShouldReturnNotFound() throws Exception {
        Long id = 1L;

        ObjectAroundVillageDTO updatedObjectAroundVillage = new ObjectAroundVillageDTO();

        when(objectAroundVillageService.updateObjectAroundVillage(id, updatedObjectAroundVillage))
                .thenThrow(new ApiRequestException("Object Around Village id: " + id + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/objectsAroundVillage/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedObjectAroundVillage)))
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
    void testDeleteObjectAroundVillageByIdWhenObjectDoesNotExist() throws Exception {
        long objectId = 1L;
        doThrow(new ApiRequestException("ObjectAroundVillage with id: " + objectId + " not found"))
                .when(objectAroundVillageService).deleteObjectAroundVillageById(objectId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/objectsAroundVillage/{id}", objectId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("ObjectAroundVillage with id: " + objectId + " not found")));
    }
    @Test
    void testGetObjectAroundVillageByVillageID() throws Exception {
        Long villageId = 1L;
        ObjectAroundVillageDTO objectAroundVillageDTO = new ObjectAroundVillageDTO();
        objectAroundVillageDTO.setId(1L);
        objectAroundVillageDTO.setType("New Object");

        when(objectAroundVillageService.getObjectAroundVillageById(villageId)).thenReturn(objectAroundVillageDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectsAroundVillage/village/{id}", villageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("New Object"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

}
