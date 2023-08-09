package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageGroundCategoryDTO;
import com.example.ludogorieSoft.village.services.VillageGroundCategoryService;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = VillageGroundCategoryController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = VillageGroundCategoryController.class
                )
        }
)
class VillageGroundCategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VillageGroundCategoryService villageGroundCategoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVillageGroundCategories() throws Exception {
        VillageGroundCategoryDTO villageGroundCategoryDTO1 = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO1.setId(1L);
        villageGroundCategoryDTO1.setVillageId(1L);
        villageGroundCategoryDTO1.setGroundCategoryId(1L);
        VillageGroundCategoryDTO villageGroundCategoryDTO2 = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO2.setId(2L);
        villageGroundCategoryDTO2.setVillageId(2L);
        villageGroundCategoryDTO2.setGroundCategoryId(2L);

        List<VillageGroundCategoryDTO> villageGroundCategoryDTOList = Arrays.asList(villageGroundCategoryDTO1, villageGroundCategoryDTO2);

        when(villageGroundCategoryService.getAllVillageGroundCategories()).thenReturn(villageGroundCategoryDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageGroundCategory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].villageId").value(1))
                .andExpect(jsonPath("$.[0].groundCategoryId").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].villageId").value(2))
                .andExpect(jsonPath("$.[1].groundCategoryId").value(2))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetVillageGroundCategoryByID() throws Exception {
        VillageGroundCategoryDTO villageGroundCategoryDTO = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO.setId(1L);
        villageGroundCategoryDTO.setVillageId(3L);
        villageGroundCategoryDTO.setGroundCategoryId(5L);

        when(villageGroundCategoryService.getByID(anyLong())).thenReturn(villageGroundCategoryDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageGroundCategory/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.villageId").value(3))
                .andExpect(jsonPath("$.groundCategoryId").value(5))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testCreateVillageGroundCategories() throws Exception {
        VillageGroundCategoryDTO villageGroundCategoryDTO = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO.setId(1L);
        villageGroundCategoryDTO.setVillageId(5L);
        villageGroundCategoryDTO.setGroundCategoryId(5L);

        when(villageGroundCategoryService.createVillageGroundCategoryDTO(any(VillageGroundCategoryDTO.class))).thenReturn(villageGroundCategoryDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/villageGroundCategory")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.villageId").value(5))
                .andExpect(jsonPath("$.groundCategoryId").value(5))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testUpdateVillageGroundCategory() throws Exception {
        VillageGroundCategoryDTO villageGroundCategoryDTO = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO.setId(1L);
        villageGroundCategoryDTO.setVillageId(4L);
        villageGroundCategoryDTO.setGroundCategoryId(2L);

        when(villageGroundCategoryService.updateVillageGroundCategory(anyLong(), any(VillageGroundCategoryDTO.class))).thenReturn(villageGroundCategoryDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/villageGroundCategory/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.villageId").value(4))
                .andExpect(jsonPath("$.groundCategoryId").value(2))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    void testGetAllVillageGroundCategoriesWhenNoneExist() throws Exception {
        when(villageGroundCategoryService.getAllVillageGroundCategories()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageGroundCategory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }


    @Test
    void testDeleteVillageGroundCategoryById() throws Exception {

        when(villageGroundCategoryService.deleteVillageGroundCategory(anyLong())).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageGroundCategory/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteVillageGroundCategoryByIdWhenDeletedSuccessfully() throws Exception {
        Long idToDelete = 1L;

        when(villageGroundCategoryService.deleteVillageGroundCategory(idToDelete)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageGroundCategory/{id}", idToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteVillageGroundCategoryByIdWhenNotExists() throws Exception {
        Long nonExistentId = 100L;

        when(villageGroundCategoryService.deleteVillageGroundCategory(nonExistentId)).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageGroundCategory/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

}
