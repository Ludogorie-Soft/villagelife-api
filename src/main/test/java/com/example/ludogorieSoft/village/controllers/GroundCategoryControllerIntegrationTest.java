package com.example.ludogoriesoft.village.controllers;

import com.example.ludogoriesoft.village.dtos.GroundCategoryDTO;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import com.example.ludogoriesoft.village.services.GroundCategoryService;
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

@WebMvcTest(GroundCategoryController.class)
@AutoConfigureMockMvc
class GroundCategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroundCategoryService groundCategoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGroundCategories() throws Exception {
        GroundCategoryDTO groundCategoryDTO1 = new GroundCategoryDTO();
        groundCategoryDTO1.setId(1L);
        groundCategoryDTO1.setGroundCategoryName("Ground Category 1");
        GroundCategoryDTO groundCategoryDTO2 = new GroundCategoryDTO();
        groundCategoryDTO2.setId(2L);
        groundCategoryDTO2.setGroundCategoryName("Ground Category 2");
        List<GroundCategoryDTO> groundCategoryDTOList = Arrays.asList(groundCategoryDTO1, groundCategoryDTO2);

        when(groundCategoryService.getAllGroundCategories()).thenReturn(groundCategoryDTOList);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/groundCategory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].groundCategoryName").value("Ground Category 1"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].groundCategoryName").value("Ground Category 2"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetGroundCategoryById() throws Exception {
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setId(1L);
        groundCategoryDTO.setGroundCategoryName("Ground Category 1");

        when(groundCategoryService.getByID(anyLong())).thenReturn(groundCategoryDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/groundCategory/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.groundCategoryName").value("Ground Category 1"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateGroundCategory() throws Exception {
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setId(1L);
        groundCategoryDTO.setGroundCategoryName("New Ground Category");

        when(groundCategoryService.createGroundCategoryDTO(any(GroundCategoryDTO.class))).thenReturn(groundCategoryDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/groundCategory")
                        .content("{\"id\": 1, \"groundCategoryName\": \"New Ground Category\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.groundCategoryName").value("New Ground Category"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateGroundCategory() throws Exception {
        GroundCategoryDTO groundCategoryDTO = new GroundCategoryDTO();
        groundCategoryDTO.setId(1L);
        groundCategoryDTO.setGroundCategoryName("Updated Ground Category");

        when(groundCategoryService.updateGroundCategory(anyLong(), any(GroundCategoryDTO.class))).thenReturn(groundCategoryDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/groundCategory/{id}", 1)
                        .content("{\"id\": 1, \"groundCategoryName\": \"Updated Ground Category\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.groundCategoryName").value("Updated Ground Category"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testDeleteGroundCategoryById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/groundCategory/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Ground Category with id: 1 has been deleted successfully!!"));
    }

    @Test
    void testGetAllGroundCategoriesWhenNoGroundCategoryExist() throws Exception {
        when(groundCategoryService.getAllGroundCategories()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/groundCategory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }

    @Test
    void testGetGroundCategoryByIdWhenGroundCategoryDoesNotExist() throws Exception {
        Long invalidId = 100000L;

        when(groundCategoryService.getByID(invalidId))
                .thenThrow(new ApiRequestException("Ground Category with id: " + invalidId + " Not Found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/groundCategory/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ground Category with id: " + invalidId + " Not Found"))
                .andReturn();
    }


    @Test
    void testShouldNotCreateGroundCategoryWithBlankGroundCategory() throws Exception {
        String blankGroundCategoryName = "";

        doThrow(new ApiRequestException("Ground Category is blank"))
                .when(groundCategoryService).createGroundCategoryDTO(any(GroundCategoryDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/groundCategory")
                        .content("{\"id\": 1, \"groundCategoryName\": \"" + blankGroundCategoryName + "\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ground Category is blank"))
                .andReturn();
    }


    @Test
    void testUpdateGroundCategoryWithInvalidIdShouldReturnNotFound() throws Exception {
        Long id = 1L;

        GroundCategoryDTO updatedGroundCategory = new GroundCategoryDTO();

        when(groundCategoryService.updateGroundCategory(id, updatedGroundCategory))
                .thenThrow(new ApiRequestException("Ground Category with id: " + id + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/groundCategory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedGroundCategory)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testDeleteGroundCategoryWithInvalidIdShouldReturnBadRequest() throws Exception {
        Long id = 1L;

        doThrow(new ApiRequestException("Ground Category with id " + id + " not found"))
                .when(groundCategoryService).deleteGroundCategory(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/groundCategory/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Ground Category with id " + id + " not found")));
    }



    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
