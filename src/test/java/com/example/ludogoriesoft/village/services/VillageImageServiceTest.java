package com.example.ludogorieSoft.village.services;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import org.mockito.*;
import org.mockito.stubbing.Answer;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageImage;
import com.example.ludogorieSoft.village.repositories.VillageImageRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.tika.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @BeforeEach
    void setUp() {
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
        assertThrows(NullPointerException.class, () -> villageImageService.createUploadDirectory(null));
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
        String testFileName = "test_image.jpg";
        String testDirectory = "src/test/resources";
        String testFilePath = testDirectory + File.separator + testFileName;
        assertThrows(IOException.class, () -> villageImageService.writeImageToFile(null, testFilePath));
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
        LocalDateTime fixedTimestamp = LocalDateTime.of(2023, 7, 27, 18, 36);

        Village village = new Village();
        when(villageService.checkVillage(villageId)).thenReturn(village);
        when(villageImageRepository.save(any(VillageImage.class))).thenReturn(new VillageImage());

        doAnswer((Answer<VillageImage>) invocation -> {
            VillageImageDTO dto = invocation.getArgument(0);
            VillageImage villageImage = new VillageImage();
            villageImage.setId(dto.getId());
            villageImage.setId(dto.getVillageId());
            villageImage.setImageName(dto.getImageName());
            villageImage.setVillageStatus(dto.getStatus());
            villageImage.setDateUpload(fixedTimestamp);
            return villageImage;
        }).when(modelMapper).map(any(VillageImageDTO.class), eq(VillageImage.class));

        villageImageService.createVillageImageDTO(villageId, fileName,fixedTimestamp, false);

        verify(villageImageRepository).save(any(VillageImage.class));
    }


    @Test
    void testCreateImagePathsWithInvalidImages() {
        byte[] emptyImage = {};
        Long villageId = 123L;
        List<String> result = villageImageService.createImagePaths(List.of(emptyImage), villageId,null, false);
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
        Assertions.assertThrows(NullPointerException.class, () -> villageImageService.encodeImageToBase64(null));
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

//    @Test
//    void testGetAllVillageDTOsWithImagesWhenFound() {
//        List<VillageDTO> villageDTOs = new ArrayList<>();
//        VillageDTO villageDTO1 = new VillageDTO();
//        villageDTO1.setId(1L);
//        villageDTO1.setName("Village 1");
//        villageDTO1.setRegion("Region 1");
//        villageDTOs.add(villageDTO1);
//
//        VillageDTO villageDTO2 = new VillageDTO();
//        villageDTO2.setId(2L);
//        villageDTO2.setName("Village 2");
//        villageDTO2.setRegion("Region 2");
//        villageDTOs.add(villageDTO2);
//
//        when(villageService.getAllVillages()).thenReturn(villageDTOs);
//
//        when(villageImageService.getAllImagesForVillageByStatusAndDate(anyLong(),true,null)).thenReturn(Arrays.asList("image1.jpg", "image2.jpg"));
//
//        List<VillageDTO> result = villageImageService.getAllApprovedVillageDTOsWithImages();
//
//        Assertions.assertEquals(2, result.size());
//        Assertions.assertEquals(Arrays.asList("image1.jpg", "image2.jpg"), result.get(0).getImages());
//        Assertions.assertEquals(Arrays.asList("image1.jpg", "image2.jpg"), result.get(1).getImages());
//    }

    @Test
    void testGetAllVillageDTOsWithImagesWhenNotFound() {
        List<VillageDTO> result = villageImageService.getAllApprovedVillageDTOsWithImages();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testAddVillageImagesImageFileDoesNotExist() {
        VillageImage villageImage1 = new VillageImage();
        villageImage1.setImageName("image1.png");

        List<String> base64Images = new ArrayList<>();

        villageImageService.addVillageImages(base64Images, List.of(villageImage1));

        Assertions.assertEquals(0, base64Images.size());
    }


//    @Test
//    void testGetAllApprovedVillageDTOsWithImagesWhenFound() {
//        List<VillageDTO> villageDTOs = new ArrayList<>();
//        VillageDTO villageDTO1 = new VillageDTO();
//        villageDTO1.setId(1L);
//        villageDTO1.setName("Village 1");
//        villageDTO1.setRegion("Region 1");
//        villageDTOs.add(villageDTO1);
//
//        VillageDTO villageDTO2 = new VillageDTO();
//        villageDTO2.setId(2L);
//        villageDTO2.setName("Village 2");
//        villageDTO2.setRegion("Region 2");
//        villageDTOs.add(villageDTO2);
//
//        when(villageService.getVillagesByStatus(true)).thenReturn(villageDTOs);
//
//        when(villageImageService.getAllImagesForVillageByStatusAndDate(anyLong())).thenReturn(Arrays.asList("image1.jpg", "image2.jpg"));
//
//        List<VillageDTO> result = villageImageService.getAllApprovedVillageDTOsWithImages();
//
//        Assertions.assertEquals(2, result.size());
//        Assertions.assertEquals(Arrays.asList("image1.jpg", "image2.jpg"), result.get(0).getImages());
//        Assertions.assertEquals(Arrays.asList("image1.jpg", "image2.jpg"), result.get(1).getImages());
//
//        verify(villageService, times(1)).getVillagesByStatus(true);
//        verify(villageImageService, times(2)).getAllImagesForVillageByStatusAndDate(anyLong());
//    }


//    @Test
//    void testUpdateVillageImagesStatus() {
//        Long villageId = 1L;
//        String localDateTime = "2023-08-10T00:00:00";
//        boolean status = true;
//
//        Village village = new Village();
//        village.setId(1L);
//
//        VillageImage villageImage = new VillageImage();
//        villageImage.setVillage(village);
//
//        List<VillageImage> villageImageList = new ArrayList<>();
//        villageImageList.add(villageImage);
//
//        when(villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId, status, localDateTime))
//                .thenReturn(villageImageList);
//
//        when(villageService.checkVillage(villageId)).thenReturn(village);
//
//        villageImageService.updateVillageImagesStatus(villageId, status, localDateTime);
//
//        verify(villageService, times(1)).checkVillage(villageId);
//        verify(villageImageRepository, times(1)).saveAll(villageImageListCaptor.capture());
//
//        List<VillageImage> capturedList = villageImageListCaptor.getValue();
//        assertEquals(1, capturedList.size());
//        VillageImage capturedVillageImage = capturedList.get(0);
//        assertTrue(capturedVillageImage.getVillageStatus());
//    }

//    @Test
//    void testRejectVillageImages() {
//        Long villageId = 1L;
//        String responseDate = "2023-08-10";
//        boolean status = true;
//        LocalDateTime deleteDate = LocalDateTime.now();
//
//        Village village = new Village();
//        village.setId(1L);
//
//        VillageImage villageImage = new VillageImage();
//        villageImage.setVillage(village);
//
//        List<VillageImage> villageImageList = new ArrayList<>();
//        villageImageList.add(villageImage);
//
//        when(villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId, status, responseDate))
//                .thenReturn(villageImageList);
//
//        when(villageService.checkVillage(villageId)).thenReturn(village);
//
//        villageImageService.rejectVillageImages(villageId, status, responseDate, deleteDate);
//
//        verify(villageService, times(1)).checkVillage(villageId);
//        verify(villageImageRepository, times(1)).saveAll(villageImageListCaptor.capture());
//
//        List<VillageImage> capturedList = villageImageListCaptor.getValue();
//        assertEquals(1, capturedList.size());
//        VillageImage capturedVillageImage = capturedList.get(0);
//        assertEquals(deleteDate, capturedVillageImage.getDateDeleted());
//    }


    @Test
    void testResumeImageByIdWhenValidIdThenImageResumed() {
        Long id = 1L;
        VillageImage villageImage = new VillageImage();
        villageImage.setId(id);
        villageImage.setDateDeleted(LocalDateTime.now());

        VillageImageDTO villageImageDTO = new VillageImageDTO();
        villageImageDTO.setId(id);

        when(villageImageRepository.findById(id)).thenReturn(Optional.of(villageImage));
        when(villageImageRepository.save(any())).thenReturn(villageImage);

        when(villageImageService.villageImageToVillageImageDTO(villageImage)).thenReturn(villageImageDTO);

        VillageImageDTO result = villageImageService.resumeImageById(id);

        Assertions.assertNotNull(result);
        assertEquals(id, result.getId());
        Assertions.assertNull(result.getDateDeleted());

        verify(villageImageRepository, times(1)).findById(id);
        verify(villageImageRepository, times(1)).save(villageImage);
        verify(villageImageService, times(1)).villageImageToVillageImageDTO(villageImage);
    }


    @Test
    void testResumeImageByIdWhenInvalidIdThenThrowApiRequestException() {
        Long id = 1L;

        when(villageImageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageImageService.resumeImageById(id));

        verify(villageImageRepository, times(1)).findById(id);
        verify(villageImageRepository, never()).save(any());
    }

    @Test
    void testDeleteVillageImageByIdWithExistingIdThenDeleteVillageImage() {
        Long id = 1L;

        when(villageImageRepository.existsById(id)).thenReturn(true);

        villageImageService.deleteVillageImageById(id);

        verify(villageImageRepository, times(1)).existsById(id);
        verify(villageImageRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteVillageImageByIdWhenNonExistingIdThenThrowApiRequestException() {
        Long id = 1L;

        when(villageImageRepository.existsById(id)).thenReturn(false);

        assertThrows(ApiRequestException.class, () -> villageImageService.deleteVillageImageById(id));

        verify(villageImageRepository, times(1)).existsById(id);
        verify(villageImageRepository, never()).deleteById(any());
    }

    @Test
    void testGetVillageImageByIdWithExistingIdThenReturnVillageImageDTO() {
        Long id = 1L;
        VillageImage villageImage = new VillageImage();
        villageImage.setId(id);

        VillageImageDTO villageImageDTO = new VillageImageDTO();
        villageImageDTO.setId(id);

        when(villageImageRepository.findById(id)).thenReturn(Optional.of(villageImage));

        when(villageImageService.villageImageToVillageImageDTO(villageImage)).thenReturn(villageImageDTO);

        VillageImageDTO result = villageImageService.getVillageImageById(id);

        Assertions.assertNotNull(result);
        assertEquals(id, result.getId());

        verify(villageImageRepository, times(1)).findById(id);
        verify(villageImageService, times(1)).villageImageToVillageImageDTO(villageImage);
    }

    @Test
    void testGetVillageImageByIdWithNonExistingIdThenThrowApiRequestException() {
        Long id = 1L;

        when(villageImageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageImageService.getVillageImageById(id));

        verify(villageImageRepository, times(1)).findById(id);
        verify(villageImageService, never()).villageImageToVillageImageDTO(any());
    }

    @Test
    void testGetDeletedVillageImageDTOsByVillageIdWhenValidVillageIdThenReturnDTOs() {
        Long villageId = 1L;

        VillageImage villageImage1 = new VillageImage();
        villageImage1.setId(1L);
        villageImage1.setImageName("image1.jpg");

        VillageImage villageImage2 = new VillageImage();
        villageImage2.setId(2L);
        villageImage2.setImageName("image2.jpg");

        List<VillageImage> villageImages = new ArrayList<>();
        villageImages.add(villageImage1);
        villageImages.add(villageImage2);

        when(villageImageRepository.findDeletedByVillageId(villageId)).thenReturn(villageImages);

        when(villageImageService.getVillageImageDTOsByVillageId(villageImages)).thenReturn(new ArrayList<>());

        List<VillageImageDTO> result = villageImageService.getDeletedVillageImageDTOsByVillageId(villageId);

        Assertions.assertNotNull(result);
        assertEquals(0, result.size());

        verify(villageImageRepository, times(1)).findDeletedByVillageId(villageId);
        verify(villageImageService, times(2)).getVillageImageDTOsByVillageId(villageImages);
    }

    @Test
    void testGetNotDeletedVillageImageDTOsByVillageIdWhenValidVillageIdThenReturnDTOs() {
        Long villageId = 1L;

        VillageImage villageImage1 = new VillageImage();
        villageImage1.setId(1L);
        villageImage1.setImageName("image1.jpg");

        VillageImage villageImage2 = new VillageImage();
        villageImage2.setId(2L);
        villageImage2.setImageName("image2.jpg");

        List<VillageImage> villageImages = new ArrayList<>();
        villageImages.add(villageImage1);
        villageImages.add(villageImage2);

        when(villageImageRepository.findNotDeletedByVillageId(villageId)).thenReturn(villageImages);

        when(villageImageService.getVillageImageDTOsByVillageId(villageImages)).thenReturn(new ArrayList<>());

        List<VillageImageDTO> result = villageImageService.getNotDeletedVillageImageDTOsByVillageId(villageId);

        Assertions.assertNotNull(result);
        assertEquals(0, result.size());

        verify(villageImageRepository, times(1)).findNotDeletedByVillageId(villageId);
        verify(villageImageService, times(2)).getVillageImageDTOsByVillageId(villageImages);
    }

    @Test
    void testRejectVillageImagesWhenNoImagesToRejectThenNoActionTaken() {
        Long villageId = 1L;
        boolean status = true;
        String responseDate = "2023-08-19";
        LocalDateTime dateDelete = LocalDateTime.now();

        when(villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId, status, responseDate))
                .thenReturn(new ArrayList<>());

        villageImageService.rejectVillageImages(villageId, status, responseDate, dateDelete);

        verify(villageImageRepository, times(1))
                .findByVillageIdAndVillageStatusAndDateUpload(villageId, status, responseDate);
        verify(villageImageService, never()).rejectSingleVillageImage(any(), any());
    }

    @Test
    void testVillageImageToVillageImageDTO() {
        VillageImage mockVillageImage = new VillageImage();
        mockVillageImage.setId(1L);
        mockVillageImage.setImageName("example.jpg");

        when(modelMapper.map(mockVillageImage, VillageImageDTO.class)).thenReturn(new VillageImageDTO());

        VillageImageDTO villageImageDTO = villageImageService.villageImageToVillageImageDTO(mockVillageImage);

        verify(modelMapper).map(mockVillageImage, VillageImageDTO.class);
        assertEquals(VillageImageDTO.class, villageImageDTO.getClass());
    }


    @Test
    void testGetAllImagesForVillageByStatusAndDateWhenStatusTrue() {
        Long villageId = 1L;
        boolean status = true;

        List<VillageImage> mockVillageImages = new ArrayList<>();
        mockVillageImages.add(new VillageImage());
        when(villageImageRepository.findByVillageIdAndVillageStatusAndDateDeletedIsNull(villageId, status))
                .thenReturn(mockVillageImages);

        List<String> result = villageImageService.getAllImagesForVillageByStatusAndDate(villageId, status, null);

        verify(villageImageRepository).findByVillageIdAndVillageStatusAndDateDeletedIsNull(villageId, status);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllImagesForVillageByStatusAndDateWhenStatusFalse() {
        Long villageId = 1L;
        boolean status = false;
        String date = "2023-08-19";

        List<VillageImage> mockVillageImages = new ArrayList<>();
        mockVillageImages.add(new VillageImage());
        when(villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(villageId, status, date))
                .thenReturn(mockVillageImages);

        List<String> result = villageImageService.getAllImagesForVillageByStatusAndDate(villageId, status, date);

        verify(villageImageRepository).findByVillageIdAndVillageStatusAndDateUpload(villageId, status, date);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllImagesForVillageByStatusAndDateWhenNoImages() {
        Long villageId = 1L;
        boolean status = true;

        when(villageImageRepository.findByVillageIdAndVillageStatusAndDateDeletedIsNull(villageId, status))
                .thenReturn(new ArrayList<>());

        List<String> result = villageImageService.getAllImagesForVillageByStatusAndDate(villageId, status, null);

        verify(villageImageRepository).findByVillageIdAndVillageStatusAndDateDeletedIsNull(villageId, status);

        assertEquals(Arrays.asList((String) null), result);
    }
}
