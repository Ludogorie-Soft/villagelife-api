package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.services.VillageImageService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;


import org.mockito.Mockito;
import static org.mockito.Mockito.when;


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
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAllImagesForVillageByStatusAndDateNotFound() throws Exception {
        Long villageId = 1L;

        Mockito.when(villageImageService.getAllImagesForVillageByStatusAndDate(villageId,true,null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/village/{villageId}/images", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

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

        Page<VillageDTO> villageDTOList = new PageImpl<>(List.of(villageDTO1,villageDTO2));

        when(villageImageService.getApprovedVillageDTOsWithImages(1,1)).thenReturn(villageDTOList);


        mockMvc.perform(get("/api/v1/villageImages/approved/1/1")
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
        when(villageImageService.getApprovedVillageDTOsWithImages(1, 1)).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageImages/approved/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testGetAllImagesForVillageByStatusAndDateWithoutImages() throws Exception {
        Long villageId = 1L;
        List<String> emptyList = Collections.emptyList();

        when(villageImageService.getAllImagesForVillageByStatusAndDate(villageId,true,null)).thenReturn(emptyList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/villageImages/village/{villageId}/images", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    void testUpdateVillageImageWhenValidInputThenReturnsUpdatedImage() throws Exception {
        Long imageId = 1L;

        VillageImageDTO inputImage = new VillageImageDTO();
        inputImage.setId(imageId);
        inputImage.setVillageId(2L);
        inputImage.setImageName("Sample Image");
        inputImage.setStatus(true);
        inputImage.setDateUpload(LocalDateTime.now());

        VillageImageDTO updatedImage = new VillageImageDTO();
        updatedImage.setId(imageId);
        updatedImage.setVillageId(2L);
        updatedImage.setImageName("Updated Image Name");
        updatedImage.setStatus(true);
        updatedImage.setDateUpload(LocalDateTime.now());

        when(villageImageService.updateVillageImage(eq(imageId), any(VillageImageDTO.class)))
                .thenReturn(updatedImage);

        mockMvc.perform(put("/api/v1/villageImages/{id}", imageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(inputImage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.imageName").value("Updated Image Name"));

        verify(villageImageService).updateVillageImage(eq(imageId), any(VillageImageDTO.class));
    }

    @Test
    void testResumeImageVillageByIdWhenValidInputThenReturnsImageDTO() throws Exception {
        Long imageId = 1L;
        String imageName = "Sample Image";

        VillageImageDTO resumedImageDTO = new VillageImageDTO();
        resumedImageDTO.setId(imageId);
        resumedImageDTO.setImageName(imageName);
        resumedImageDTO.setDateDeleted(null);

        when(villageImageService.resumeImageById(imageId)).thenReturn(resumedImageDTO);

        mockMvc.perform(put("/api/v1/villageImages/resume/{id}", imageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.imageName").value("Sample Image"))
                .andExpect(jsonPath("$.dateDeleted", Matchers.nullValue()));

        verify(villageImageService).resumeImageById(imageId);
    }

    @Test
    void testGetDeletedVillageImageDTOsByVillageIdThenReturnsListOfDeletedImages() throws Exception {
        Long villageId = 1L;

        List<VillageImageDTO> deletedImages = new ArrayList<>();
        VillageImageDTO image1 = new VillageImageDTO();
        image1.setId(1L);
        image1.setVillageId(villageId);
        image1.setImageName("Deleted Image 1");
        image1.setStatus(false);
        image1.setDateUpload(LocalDateTime.parse("2023-08-19T10:00:00"));
        image1.setDateDeleted(LocalDateTime.parse("2023-08-20T15:30:00"));
        image1.setBase64Image("base64EncodedImageData1");
        deletedImages.add(image1);

        VillageImageDTO image2 = new VillageImageDTO();
        image2.setId(2L);
        image2.setVillageId(villageId);
        image2.setImageName("Deleted Image 2");
        image2.setStatus(false);
        image2.setDateUpload(LocalDateTime.parse("2023-08-19T11:00:00"));
        image2.setDateDeleted(LocalDateTime.parse("2023-08-20T16:30:00"));
        image2.setBase64Image("base64EncodedImageData2");
        deletedImages.add(image2);

        when(villageImageService.getDeletedVillageImageDTOsByVillageId(villageId)).thenReturn(deletedImages);

        mockMvc.perform(get("/api/v1/villageImages/deleted/with-base64/village/{villageId}", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].villageId").value(villageId))
                .andExpect(jsonPath("$[0].imageName").value("Deleted Image 1"))
                .andExpect(jsonPath("$[0].status").value(false))
                .andExpect(jsonPath("$[0].dateUpload").value("2023-08-19 10:00:00"))
                .andExpect(jsonPath("$[0].dateDeleted").value("2023-08-20 15:30:00"))
                .andExpect(jsonPath("$[0].base64Image").value("base64EncodedImageData1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].villageId").value(villageId))
                .andExpect(jsonPath("$[1].imageName").value("Deleted Image 2"))
                .andExpect(jsonPath("$[1].status").value(false))
                .andExpect(jsonPath("$[1].dateUpload").value("2023-08-19 11:00:00"))
                .andExpect(jsonPath("$[1].dateDeleted").value("2023-08-20 16:30:00"))
                .andExpect(jsonPath("$[1].base64Image").value("base64EncodedImageData2"));

        verify(villageImageService).getDeletedVillageImageDTOsByVillageId(villageId);
    }

    @Test
    void testGetNotDeletedVillageImageDTOsByVillageIdThenReturnsListOfNotDeletedImages() throws Exception {
        Long villageId = 1L;

        List<VillageImageDTO> notDeletedImages = new ArrayList<>();
        VillageImageDTO image1 = new VillageImageDTO();
        image1.setId(1L);
        image1.setVillageId(villageId);
        image1.setImageName("Image 1");
        image1.setStatus(true);
        image1.setDateUpload(LocalDateTime.parse("2023-08-19T10:00:00"));
        image1.setBase64Image("base64EncodedImageData1");
        notDeletedImages.add(image1);

        VillageImageDTO image2 = new VillageImageDTO();
        image2.setId(2L);
        image2.setVillageId(villageId);
        image2.setImageName("Image 2");
        image2.setStatus(true);
        image2.setDateUpload(LocalDateTime.parse("2023-08-19T11:00:00"));
        image2.setBase64Image("base64EncodedImageData2");
        notDeletedImages.add(image2);

        when(villageImageService.getNotDeletedVillageImageDTOsByVillageId(villageId)).thenReturn(notDeletedImages);

        mockMvc.perform(get("/api/v1/villageImages/with-base64/village/{villageId}", villageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].villageId").value(villageId))
                .andExpect(jsonPath("$[0].imageName").value("Image 1"))
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[0].dateUpload").value("2023-08-19 10:00:00"))
                .andExpect(jsonPath("$[0].base64Image").value("base64EncodedImageData1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].villageId").value(villageId))
                .andExpect(jsonPath("$[1].imageName").value("Image 2"))
                .andExpect(jsonPath("$[1].status").value(true))
                .andExpect(jsonPath("$[1].dateUpload").value("2023-08-19 11:00:00"))
                .andExpect(jsonPath("$[1].base64Image").value("base64EncodedImageData2"));

        verify(villageImageService).getNotDeletedVillageImageDTOsByVillageId(villageId);
    }


    @Test
    void testGetVillageImageByIdThenReturnsVillageImageDTO() throws Exception {
        Long imageId = 1L;

        VillageImageDTO villageImageDTO = new VillageImageDTO();
        villageImageDTO.setId(imageId);
        villageImageDTO.setVillageId(2L);
        villageImageDTO.setImageName("Image");
        villageImageDTO.setStatus(true);
        villageImageDTO.setDateUpload(LocalDateTime.parse("2023-08-19T10:00:00"));
        villageImageDTO.setBase64Image("base64EncodedImageData");

        when(villageImageService.getVillageImageById(imageId)).thenReturn(villageImageDTO);

        mockMvc.perform(get("/api/v1/villageImages/{id}", imageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(imageId))
                .andExpect(jsonPath("$.villageId").value(2))
                .andExpect(jsonPath("$.imageName").value("Image"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.dateUpload").value("2023-08-19 10:00:00"))
                .andExpect(jsonPath("$.base64Image").value("base64EncodedImageData"));

        verify(villageImageService).getVillageImageById(imageId);
    }

    @Test
    void testAdminUploadImagesWhenMissingVillageIdThenReturnsBadRequest() throws Exception {
        List<byte[]> imageBytesList = new ArrayList<>();
        byte[] imageBytes1 = "Image1".getBytes(StandardCharsets.UTF_8);
        byte[] imageBytes2 = "Image2".getBytes(StandardCharsets.UTF_8);
        imageBytesList.add(imageBytes1);
        imageBytesList.add(imageBytes2);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/villageImages/admin-upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(imageBytesList)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(villageImageService);
    }

    @Test
    void testDeleteImageById() throws Exception {
        Long imageIdToDelete = 1L;

        doNothing().when(villageImageService).deleteImageFileById(imageIdToDelete);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/villageImages/" + imageIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Image with id " + imageIdToDelete + " has been deleted successfully!"));

        verify(villageImageService, times(1)).deleteImageFileById(imageIdToDelete);
    }
    @Test
    void testUploadImagesSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/villageImages/upload-images"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Images uploaded successfully.")));
    }
}
