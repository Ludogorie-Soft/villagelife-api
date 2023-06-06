package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageGroundCategoryDTO;
import com.example.ludogorieSoft.village.model.VillageGroundCategory;
import com.example.ludogorieSoft.village.services.VillageGroundCategoryService;
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
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VillageGroundCategoryController.class)
@AutoConfigureMockMvc
public class VillageGroundCategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VillageGroundCategoryService villageGroundCategoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllVillageGroundCategories() throws Exception {

        VillageGroundCategoryDTO villageGroundCategoryDTO1 = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO1.setId(1L);
        VillageGroundCategoryDTO villageGroundCategoryDTO2 = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO2.setId(2L);
        List<VillageGroundCategoryDTO> villageGroundCategoryDTOList = Arrays.asList(villageGroundCategoryDTO1, villageGroundCategoryDTO2);

        when(villageGroundCategoryService.getAllVillageGroundCategories()).thenReturn(villageGroundCategoryDTOList);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageGroundCategory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetVillageGroundCategoryByID() throws Exception {

        VillageGroundCategoryDTO villageGroundCategoryDTO = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO.setId(1L);

        when(villageGroundCategoryService.getByID(anyLong())).thenReturn(villageGroundCategoryDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageGroundCategory/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testCreateVillageGroundCategories() throws Exception {

        VillageGroundCategoryDTO villageGroundCategoryDTO = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO.setId(1L);
        URI location = UriComponentsBuilder.fromPath("/api/v1/villageGroundCategory/{id}")
                .buildAndExpand(1L)
                .toUri();

        when(villageGroundCategoryService.createVillageGroundCategoryDTO(any(VillageGroundCategoryDTO.class))).thenReturn(villageGroundCategoryDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/villageGroundCategory")
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())

                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testUpdateVillageGroundCategory() throws Exception {

        VillageGroundCategoryDTO villageGroundCategoryDTO = new VillageGroundCategoryDTO();
        villageGroundCategoryDTO.setId(1L);

        when(villageGroundCategoryService.updateVillageGroundCategory(anyLong(), any(VillageGroundCategoryDTO.class))).thenReturn(villageGroundCategoryDTO);


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/villageGroundCategory/{id}", 1)
                        .content("{\"id\": 1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testDeleteVillageGroundCategoryById() throws Exception {

        when(villageGroundCategoryService.deleteVillageGroundCategory(anyLong())).thenReturn(1);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageGroundCategory/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
