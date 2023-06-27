package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.VillageImageDTO;
import com.example.ludogoriesoft.village.model.Village;
import com.example.ludogoriesoft.village.model.VillageImage;
import com.example.ludogoriesoft.village.repositories.VillageImageRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VillageImageServiceTest {
    @Mock
    private VillageImageRepository villageImageRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private VillageService villageService;
    @InjectMocks
    private VillageImageService villageImageService;
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/village_images";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testCreateVillageImageDTO() {
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

        assertEquals(villageImageDTO, result);
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

    @Test
    public void testCreateImagePathsWithInvalidImageExpectErrorMessage() throws Exception {
        List<byte[]> imageBytes = new ArrayList<>();
        byte[] invalidImageData = "Invalid image data".getBytes();
        imageBytes.add(invalidImageData);
        Long villageId = 1L;

        List<String> imagePaths = villageImageService.createImagePaths(imageBytes, villageId);

        Assert.assertEquals(1, imagePaths.size());
        assertTrue(imagePaths.get(0).startsWith("Invalid file format."));
        verifyNoInteractions(villageService);
        verifyNoInteractions(modelMapper);
        verifyNoInteractions(villageImageRepository);
    }
    //@Test
    //public void testGetUploadDirectoryPath() {
    //    String expectedPath = System.getProperty("user.dir") + File.separator + "src/main/resources/static/village_images";
    //    String actualPath = villageImageService.getUploadDirectoryPath();
    //    assertEquals(expectedPath, actualPath);
    //}
    @Test
    public void testGetImageBytesFromMultipartFile() throws IOException {
        MockMultipartFile image1 = new MockMultipartFile("image1.jpg", "image1.jpg", "image/jpeg", "Test image data".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("image2.jpg", "image2.jpg", "image/jpeg", "Another image data".getBytes());

        List<MultipartFile> images = List.of(image1, image2);
        List<byte[]> imageBytes = villageImageService.getImageBytesFromMultipartFile(images);

        assertEquals(2, imageBytes.size());
        assertEquals("Test image data", new String(imageBytes.get(0)));
        assertEquals("Another image data", new String(imageBytes.get(1)));
    }
}
