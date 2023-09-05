package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.ObjectVillageDTO;
import com.example.ludogorieSoft.village.enums.Distance;
import com.example.ludogorieSoft.village.exeptions.handler.ApiExceptionHandler;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.services.ObjectVillageService;
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

import static java.time.LocalDateTime.now;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = ObjectVillageController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = ObjectVillageController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = ApiExceptionHandler.class
                )
        }
)
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
        objectVillageDTO1.setVillageId(1L);
        objectVillageDTO1.setObjectAroundVillageId(1L);
        objectVillageDTO1.setDistance(Distance.IN_THE_VILLAGE);
        ObjectVillageDTO objectVillageDTO2 = new ObjectVillageDTO();
        objectVillageDTO2.setId(2L);
        objectVillageDTO2.setVillageId(2L);
        objectVillageDTO2.setObjectAroundVillageId(2L);
        objectVillageDTO2.setDistance(Distance.ON_10_KM);

        List<ObjectVillageDTO> objectVillageDTOList = Arrays.asList(objectVillageDTO1, objectVillageDTO2);

        when(objectVillageService.getAllObjectVillages()).thenReturn(objectVillageDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectVillages")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].villageId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].objectAroundVillageId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].distance").value("IN_THE_VILLAGE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].villageId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].objectAroundVillageId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].distance").value("ON_10_KM"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetObjectVillageByID() throws Exception {
        ObjectVillageDTO objectVillageDTO = new ObjectVillageDTO();
        objectVillageDTO.setId(1L);
        objectVillageDTO.setVillageId(1L);
        objectVillageDTO.setObjectAroundVillageId(1L);
        objectVillageDTO.setDistance(Distance.ON_31_TO_50_KM);
        when(objectVillageService.getObjectVillageById(anyLong())).thenReturn(objectVillageDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectVillages/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.villageId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.objectAroundVillageId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.distance").value("ON_31_TO_50_KM"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateObjectVillage() throws Exception {
        ObjectVillageDTO objectVillageDTO = new ObjectVillageDTO();
        objectVillageDTO.setId(2L);
        objectVillageDTO.setVillageId(3L);
        objectVillageDTO.setObjectAroundVillageId(4L);
        objectVillageDTO.setDistance(Distance.ON_10_KM);

        when(objectVillageService.createObjectVillage(any(ObjectVillageDTO.class))).thenReturn(objectVillageDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/objectVillages")
                        .content("{\"id\": 2}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.villageId").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.objectAroundVillageId").value(4L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.distance").value("ON_10_KM"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateObjectVillageByID() throws Exception {
        ObjectVillageDTO objectVillageDTO = new ObjectVillageDTO();
        objectVillageDTO.setId(1L);
        objectVillageDTO.setVillageId(2L);
        objectVillageDTO.setObjectAroundVillageId(3L);
        objectVillageDTO.setDistance(Distance.OVER_50_KM);

        when(objectVillageService.updateObjectVillageById(anyLong(), any(ObjectVillageDTO.class))).thenReturn(objectVillageDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/objectVillages/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.villageId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.objectAroundVillageId").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.distance").value("OVER_50_KM"))
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


    @Test
    void testGetObjectVillageByIdWhenObjectVillageNotFound() throws Exception {
        Long invalidId = 100000L;

        when(objectVillageService.getObjectVillageById(invalidId))
                .thenThrow(new ApiRequestException("Object Village with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectVillages/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Object Village with id: " + invalidId + " Not Found"))
                .andReturn();
    }

    @Test
    void testCreateObjectVillageWithInvalidData() throws Exception {
        String invalidData = "";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/objectVillages")
                        .content("{\"id\": 1, }" + invalidData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    void testUpdateObjectVillageByIdWhenObjectVillageNotFound() throws Exception {
        Long invalidId = 1L;

        when(objectVillageService.updateObjectVillageById(eq(invalidId), any(ObjectVillageDTO.class)))
                .thenThrow(new ApiRequestException("Object Village with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/objectVillages/{id}", invalidId)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Object Village with id: " + invalidId + " Not Found"))
                .andReturn();
    }

    @Test
    void testGetAllObjectVillagesWhenNoObjectVillageExists() throws Exception {
        when(objectVillageService.getAllObjectVillages()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectVillages")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }


    @Test
    void testDeleteObjectVillageByIdWhenObjectVillageDoesNotExist() throws Exception {
        Long invalidId = 100000L;
        String errorMessage = "Object Village not found for id " + invalidId;

        doThrow(new ApiRequestException(errorMessage))
                .when(objectVillageService).deleteObjectVillageById(invalidId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/objectVillages/{id}", invalidId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage))
                .andReturn();
    }

    @Test
    void testGetObjectVillageByVillageID() throws Exception {
        Long villageId = 1L;
        ObjectVillageDTO objectVillageDTO1 = new ObjectVillageDTO(1L, villageId, 1L, Distance.IN_THE_VILLAGE,true,now(),now());
        ObjectVillageDTO objectVillageDTO2 = new ObjectVillageDTO(2L, villageId, 2L, Distance.ON_10_KM,true,now(),now());

        List<ObjectVillageDTO> objectVillageDTOList = Arrays.asList(objectVillageDTO1, objectVillageDTO2);

        when(objectVillageService.getObjectVillageByVillageId(villageId)).thenReturn(objectVillageDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/objectVillages/village/{id}", villageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].villageId").value(villageId))
                .andExpect(jsonPath("$[0].objectAroundVillageId").value(1))
                .andExpect(jsonPath("$[0].distance").value("IN_THE_VILLAGE"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].villageId").value(villageId))
                .andExpect(jsonPath("$[1].objectAroundVillageId").value(2))
                .andExpect(jsonPath("$[1].distance").value("ON_10_KM"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

}
