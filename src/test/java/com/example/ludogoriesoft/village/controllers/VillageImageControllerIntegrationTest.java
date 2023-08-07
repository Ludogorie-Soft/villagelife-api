package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
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

import java.util.ArrayList;
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

//    @Test
//    void testGetAllImagesForVillage() throws Exception {
//        List<String> base64Images = Arrays.asList("image1", "image2");
//        when(villageImageService.getAllImagesForVillage(anyLong())).thenReturn(base64Images);
//
//        Long villageId = 1L;
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageImages/village/{villageId}/images", villageId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        MockHttpServletResponse response = mvcResult.getResponse();
//        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//        String responseBody = response.getContentAsString();
//        List<String> responseImages = new ObjectMapper().readValue(responseBody, new TypeReference<>() {
//        });
//        Assertions.assertEquals(base64Images, responseImages);
//    }

//    @Test
//    void testGetAllImagesForVillageNotFound() throws Exception {
//        Long villageId = 1L;
//
//        Mockito.when(villageImageService.getAllImagesForVillage(villageId)).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/api/v1/village/{villageId}/images", villageId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }

    @Test
    void testGetAllVillageDTOsWithImages() throws Exception {
        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village 1");
        villageDTO1.setRegion("Region 1");
        List<String> images1 = new ArrayList<>();
        images1.add("img1");
        images1.add("img2");
        villageDTO1.setImages(images1);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village 2");
        villageDTO2.setRegion("Region 2");
        List<String> images2 = new ArrayList<>();
        images2.add("img3");
        images2.add("img4");
        images2.add("img5");
        villageDTO2.setImages(images2);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);

        when(villageImageService.getAllVillageDTOsWithImages()).thenReturn(villageDTOList);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageImages/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Village 1"))
                .andExpect(jsonPath("$.[0].region").value("Region 1"))
                .andExpect(jsonPath("$.[0].images").value(images1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("Village 2"))
                .andExpect(jsonPath("$.[1].region").value("Region 2"))
                .andExpect(jsonPath("$.[1].images").value(images2))
                .andReturn();
    }

    @Test
    void testGetAllVillageDTOsWithImagesWhenNotFound() throws Exception {
        when(villageImageService.getAllVillageDTOsWithImages()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageImages/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }


//    @Test
//    void testGetAllImagesForVillageWithImages() throws Exception {
//        Long villageId = 1L;
//        List<String> base64Images = Arrays.asList("image1", "image2");
//
//        when(villageImageService.getAllImagesForVillage(villageId)).thenReturn(base64Images);
//
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageImages/village/{villageId}/images", villageId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        MockHttpServletResponse response = mvcResult.getResponse();
//        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
//
//        String responseBody = response.getContentAsString();
//        List<String> responseImages = new ObjectMapper().readValue(responseBody, new TypeReference<>() {
//        });
//        Assertions.assertEquals(base64Images, responseImages);
//    }

//    @Test
//    void testGetAllImagesForVillageWithoutImages() throws Exception {
//        Long villageId = 1L;
//        List<String> emptyList = Collections.emptyList();
//
//        when(villageImageService.getAllImagesForVillage(villageId)).thenReturn(emptyList);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageImages/village/{villageId}/images", villageId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andReturn();
//    }
}

