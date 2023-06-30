package com.example.ludogorieSoft.village.services;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageImage;
import com.example.ludogorieSoft.village.repositories.VillageImageRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class VillageImageServiceTest {
    @Mock
    private VillageImageRepository villageImageRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private VillageService villageService;
    @InjectMocks
    private VillageImageService villageImageService;
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/village_images";


    @Test
    void testCreateVillageImageDTO() {
        VillageImageDTO villageImageDTO = new VillageImageDTO();
        villageImageDTO.setVillageId(1L);

        VillageImage villageImage = new VillageImage();
        Village village = new Village();

        when(villageService.checkVillage(villageImageDTO.getVillageId())).thenReturn(village);
        when(villageImageRepository.save(any(VillageImage.class))).thenReturn(villageImage);
        when(modelMapper.map(villageImageDTO, VillageImage.class)).thenReturn(villageImage);

        VillageImageDTO result = villageImageService.createVillageImageDTO(villageImageDTO);

        verify(villageService).checkVillage(villageImageDTO.getVillageId());
        verify(villageImageRepository).save(villageImage);
        verify(modelMapper).map(villageImageDTO, VillageImage.class);

        Assertions.assertEquals(villageImageDTO, result);
    }

    //@Test
    //public void testCreateImagePathsWithValidImageExpectImagePath() throws Exception {
    //    List<byte[]> imageBytes = new ArrayList<>();
    //    byte[] imageData = "Test image data".getBytes();
    //    imageBytes.add(imageData);
    //    Long villageId = 1L;
    //
    //    String expectedImagePath = "generated_image.jpg";
    //    String expectedFullPath = UPLOAD_DIRECTORY + File.separator + expectedImagePath;
    //
    //    VillageImageDTO villageImageDTO = new VillageImageDTO(null, villageId, expectedImagePath);
    //    VillageImage villageImage = new VillageImage();
    //    villageImage.setVillage(new Village());
    //
    //    when(villageService.checkVillage(villageId)).thenReturn(new Village());
    //    when(modelMapper.map(any(VillageImageDTO.class), eq(VillageImage.class))).thenReturn(villageImage);
    //    when(villageImageRepository.save(any(VillageImage.class))).thenReturn(villageImage);
    //
    //    List<String> imagePaths = villageImageService.createImagePaths(imageBytes, villageId);
    //
    //    assertEquals(1, imagePaths.size());
    //    assertEquals(expectedImagePath, imagePaths.get(0));
    //    verify(villageService).checkVillage(villageId);
    //    verify(modelMapper).map(any(VillageImageDTO.class), eq(VillageImage.class));
    //    verify(villageImageRepository).save(any(VillageImage.class));
    //    File expectedFile = new File(expectedFullPath);
    //    assertTrue(expectedFile.exists());
    //    assertTrue(expectedFile.delete());
    //}

    //@Test
    //public void testCreateImagePathsWithInvalidImageExpectErrorMessage() throws Exception {
    //    List<byte[]> imageBytes = new ArrayList<>();
    //    byte[] invalidImageData = "Invalid image data".getBytes();
    //    imageBytes.add(invalidImageData);
    //    Long villageId = 1L;
//
    //    List<String> imagePaths = villageImageService.createImagePaths(imageBytes, villageId);
//
    //    Assert.assertEquals(1, imagePaths.size());
    //    assertTrue(imagePaths.get(0).startsWith("Invalid file format."));
    //    verifyNoInteractions(villageService);
    //    verifyNoInteractions(modelMapper);
    //    verifyNoInteractions(villageImageRepository);
    //}

    @Test
    void testGetUploadDirectoryPath() {
        String expectedPath = System.getProperty("user.dir") + File.separator + "src/main/resources/static/village_images";
        String actualPath = villageImageService.getUploadDirectoryPath();
        Assertions.assertEquals(expectedPath, actualPath);
    }

    @Test
    void testGetImageBytesFromMultipartFile() throws IOException {
        MockMultipartFile image1 = new MockMultipartFile("image1.jpg", "image1.jpg", "image/jpeg", "Test image data".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("image2.jpg", "image2.jpg", "image/jpeg", "Another image data".getBytes());

        List<MultipartFile> images = List.of(image1, image2);
        List<byte[]> imageBytes = villageImageService.getImageBytesFromMultipartFile(images);

        Assertions.assertEquals(2, imageBytes.size());
        Assertions.assertEquals("Test image data", new String(imageBytes.get(0)));
        Assertions.assertEquals("Another image data", new String(imageBytes.get(1)));
    }
}
