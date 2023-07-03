package com.example.ludogorieSoft.village.services;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageImage;
import com.example.ludogorieSoft.village.repositories.VillageImageRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.tika.Tika;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Test
    void testGetUploadDirectoryPath() {
        String expectedPath = System.getProperty("user.dir") + File.separator + "src/main/resources/static/village_images/";
        String actualPath = villageImageService.getUploadDirectoryPath();
        Assertions.assertEquals(expectedPath, actualPath);
    }
    @Test
    void testGenerateFileName() {
        String fileName = villageImageService.generateFileName();
        Assertions.assertTrue(fileName.endsWith(".jpg"));
        Assertions.assertNotNull(fileName);
        Assertions.assertNotEquals("", fileName);
        String anotherFileName = villageImageService.generateFileName();
        Assertions.assertNotEquals(fileName, anotherFileName);
    }

    @Test
    void testCreateUploadDirectoryExistingDirectory() {
        String existingDirPath = "src/main/resources/static/village_images";
        File existingDir = new File(existingDirPath);

        villageImageService.createUploadDirectory(existingDirPath);

        Assertions.assertTrue(existingDir.exists());
        Assertions.assertTrue(existingDir.isDirectory());
    }

    @Test
    void testCreateUploadDirectoryNonExistingDirectory() {
        String nonExistingDirPath = "path/to/non/existing/directory";
        File nonExistingDir = new File(nonExistingDirPath);
        villageImageService.createUploadDirectory(nonExistingDirPath);
        Assertions.assertTrue(nonExistingDir.exists());
        Assertions.assertTrue(nonExistingDir.isDirectory());
    }

    @Test
    void testCreateUploadDirectoryNullPath() {
        String nullPath = null;
        assertThrows(NullPointerException.class, () -> {
            villageImageService.createUploadDirectory(nullPath);
        });
    }

    @Test
    void testWriteImageToFileEmptyImage() {
        byte[] emptyImageBytes = new byte[0];
        String testFileName = "test_image.jpg";
        String testDirectory = "src/test/resources";
        String testFilePath = testDirectory + File.separator + testFileName;
        assertThrows(IOException.class, () -> villageImageService.writeImageToFile(emptyImageBytes, testFilePath));
        Assertions.assertFalse(Files.exists(Path.of(testFilePath)));
    }

    @Test
    void testWriteImageToFileNullImage() {
        byte[] nullImageBytes = null;
        String testFileName = "test_image.jpg";
        String testDirectory = "src/test/resources";
        String testFilePath = testDirectory + File.separator + testFileName;
        assertThrows(IOException.class, () -> villageImageService.writeImageToFile(nullImageBytes, testFilePath));
        Assertions.assertFalse(Files.exists(Path.of(testFilePath)));
    }

    @Test
    void testWriteImageToFileInvalidFilePath() {
        byte[] imageBytes = {0x12, 0x34, 0x56, 0x78};
        String invalidDirectory = "non_existing_directory";
        String invalidFilePath = invalidDirectory + File.separator + "test_image.jpg";
        assertThrows(IOException.class, () -> villageImageService.writeImageToFile(imageBytes, invalidFilePath));
    }
    @Test
    void testCreateVillageImageDTOWithVillageIdAndFileName() {
        Long villageId = 123L;
        String fileName = "image.jpg";
        VillageImageDTO villageImageDTO = new VillageImageDTO(null, villageId, fileName);

        Village village = new Village();
        VillageService villageService = Mockito.mock(VillageService.class);
        VillageImageRepository villageImageRepository = Mockito.mock(VillageImageRepository.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);

        VillageImageService villageImageService = new VillageImageService(
                villageImageRepository, modelMapper, villageService);

        Mockito.when(villageService.checkVillage(villageId)).thenReturn(village);
        Mockito.when(villageImageRepository.save(Mockito.any(VillageImage.class))).thenReturn(new VillageImage());
        Mockito.when(modelMapper.map(villageImageDTO, VillageImage.class)).thenReturn(new VillageImage());

        villageImageService.createVillageImageDTO(villageId, fileName);

        Mockito.verify(villageService, Mockito.times(1)).checkVillage(villageId);
        Mockito.verify(villageImageRepository, Mockito.times(1)).save(Mockito.any(VillageImage.class));
        Mockito.verify(modelMapper, Mockito.times(1)).map(villageImageDTO, VillageImage.class);
    }

    @Test
    void testCreateImagePathsWithInvalidImages() {
        byte[] emptyImage = {};
        Long villageId = 123L;
        List<String> result = villageImageService.createImagePaths(List.of(emptyImage), villageId);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testProcessImage_WithInvalidImage() {
        byte[] image = {0x12, 0x34, 0x56};
        String result = villageImageService.processImage(image);
        Assertions.assertNull(result);
    }

    @Test
    void testProcessImage_WithIOException() {
        byte[] image = {0x12, 0x34, 0x56, 0x78};
        String result = villageImageService.processImage(image);
        Assertions.assertNull(result);
    }
}
