package com.example.ludogorieSoft.village.services;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageResponse;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageImage;
import com.example.ludogorieSoft.village.repositories.VillageImageRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

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
    @Mock
    private Logger logger;
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/village_images";

    @BeforeEach
    void setUp() throws IOException {
        villageImageService = Mockito.spy(villageImageService);
    }

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
    void testProcessImageWithInvalidImage() {
        byte[] image = {0x12, 0x34, 0x56};
        String result = villageImageService.processImage(image);
        Assertions.assertNull(result);
    }

    @Test
    void testProcessImageWithIOException() {
        byte[] image = {0x12, 0x34, 0x56, 0x78};
        String result = villageImageService.processImage(image);
        Assertions.assertNull(result);
    }
    @Test
    void testEncodeImageToBase64EmptyImage() {
        byte[] emptyImageBytes = new byte[0];
        String expectedBase64 = "";

        String result = villageImageService.encodeImageToBase64(emptyImageBytes);

        Assertions.assertEquals(expectedBase64, result);
    }

    @Test
    void testEncodeImageToBase64NullImage() {
        byte[] nullImageBytes = null;

        Assertions.assertThrows(NullPointerException.class, () -> {
            villageImageService.encodeImageToBase64(nullImageBytes);
        });
    }
    @Test
    void testReadImageBytes() throws IOException {
        File imageFile = File.createTempFile("test-image", ".png");

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            byte[] dummyImageData = { 0x01, 0x02, 0x03 };
            outputStream.write(dummyImageData);
            outputStream.close();

            byte[] result = villageImageService.readImageBytes(imageFile);

            FileInputStream inputStream = new FileInputStream(imageFile);
            byte[] expected = IOUtils.toByteArray(inputStream);
            inputStream.close();
            Assertions.assertArrayEquals(expected, result);
        } finally {
            imageFile.delete();
        }
    }

    //@Test
    //void testGetAllVillageImages() {
    //    VillageDTO village1 = new VillageDTO();
    //    village1.setId(1L);
    //    village1.setName("Village 1");
    //    VillageDTO village2 = new VillageDTO();
    //    village2.setId(2L);
    //    village2.setName("Village 2");
    //    List<VillageDTO> villageDTOs = new ArrayList<>();
    //    villageDTOs.add(village1);
    //    villageDTOs.add(village2);
//
    //    List<String> images1 = new ArrayList<>();
    //    images1.add("image1.png");
    //    images1.add("image2.png");
    //    List<String> images2 = new ArrayList<>();
    //    images2.add("image3.png");
    //    images2.add("image4.png");
//
    //    when(villageService.getAllVillages()).thenReturn(villageDTOs);
    //    when(villageImageService.getAllImagesForVillage(1L)).thenReturn(images1);
    //    when(villageImageService.getAllImagesForVillage(2L)).thenReturn(images2);
//
    //    List<VillageImageResponse> result = villageImageService.getAllVillageImages();
//
    //    Assertions.assertEquals(2, result.size());
//
    //    VillageImageResponse response1 = result.get(0);
    //    Assertions.assertEquals(village1.getId(), response1.getVillageDTO().getId());
    //    Assertions.assertEquals(village1.getName(), response1.getVillageDTO().getName());
    //    Assertions.assertEquals(images1, response1.getImages());
//
    //    VillageImageResponse response2 = result.get(1);
    //    Assertions.assertEquals(village2.getId(), response2.getVillageDTO().getId());
    //    Assertions.assertEquals(village2.getName(), response2.getVillageDTO().getName());
    //    Assertions.assertEquals(images2, response2.getImages());
//
    //    verify(villageService, times(1)).getAllVillages();
    //    verify(villageImageService, times(1)).getAllImagesForVillage(1L);
    //    verify(villageImageService, times(1)).getAllImagesForVillage(2L);
    //}

    @Test
    void testAddVillageImagesImageFileDoesNotExist() {
        VillageImage villageImage1 = new VillageImage();
        villageImage1.setImageName("image1.png");

        List<String> base64Images = new ArrayList<>();

        villageImageService.addVillageImages(base64Images, List.of(villageImage1));

        Assertions.assertEquals(0, base64Images.size());
    }

}
