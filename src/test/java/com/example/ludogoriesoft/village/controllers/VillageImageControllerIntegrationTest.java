package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageResponse;
import com.example.ludogorieSoft.village.services.VillageImageService;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = VillageImageController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = VillageImageController.class
                )
        }
)
class VillageImageControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VillageImageService villageImageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllImagesForVillage() throws Exception {
        List<String> base64Images = Arrays.asList("image1", "image2");
        when(villageImageService.getAllImagesForVillage(anyLong())).thenReturn(base64Images);

        Long villageId = 1L;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageImages/village/{villageId}/images", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

        String responseBody = response.getContentAsString();
        List<String> responseImages = new ObjectMapper().readValue(responseBody, new TypeReference<>() {
        });
        Assertions.assertEquals(base64Images, responseImages);
    }

    @Test
    void testGetAllVillageImageResponses() throws Exception {
        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("vl1");

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("vl2");

        List<String> images1 = Arrays.asList("image1", "image2");
        List<String> images2 = Arrays.asList("image3", "image4");

        VillageImageResponse response1 = new VillageImageResponse(villageDTO1, images1);
        VillageImageResponse response2 = new VillageImageResponse();
        response2.setVillageDTO(villageDTO2);
        response2.setImages(images2);

        List<VillageImageResponse> villageImageResponses = Arrays.asList(response1, response2);

        when(villageImageService.getAllVillageImages()).thenReturn(villageImageResponses);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageImages/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].villageDTO.id").value(response1.getVillageDTO().getId()))
                .andExpect(jsonPath("$.[0].villageDTO.name").value(response1.getVillageDTO().getName()))
                .andExpect(jsonPath("$.[0].images.[0]").value(response1.getImages().get(0)))
                .andExpect(jsonPath("$.[0].images.[1]").value(response1.getImages().get(1)))
                .andExpect(jsonPath("$.[1].villageDTO.id").value(response2.getVillageDTO().getId()))
                .andExpect(jsonPath("$.[1].villageDTO.name").value(response2.getVillageDTO().getName()))
                .andExpect(jsonPath("$.[1].images.[0]").value(response2.getImages().get(0)))
                .andExpect(jsonPath("$.[1].images.[1]").value(response2.getImages().get(1)))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        String responseBody = response.getContentAsString();
        List<VillageImageResponse> actualResponses = new ObjectMapper().readValue(responseBody, new TypeReference<>() {
        });

        for (int i = 0; i < villageImageResponses.size(); i++) {
            VillageImageResponse expectedResponse = villageImageResponses.get(i);
            VillageImageResponse actualResponse = actualResponses.get(i);
            Assertions.assertEquals(expectedResponse.getVillageDTO().getId(), actualResponse.getVillageDTO().getId());
            Assertions.assertEquals(expectedResponse.getVillageDTO().getName(), actualResponse.getVillageDTO().getName());
            Assertions.assertEquals(expectedResponse.getImages(), actualResponse.getImages());
        }
    }

    @Test
    void testGetAllImagesForVillageNotFound() throws Exception {
        Long villageId = 1L;

        Mockito.when(villageImageService.getAllImagesForVillage(villageId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/village/{villageId}/images", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllVillageImageResponsesNotFound() throws Exception {
        Mockito.when(villageImageService.getAllVillageImages()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/villageImages/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

