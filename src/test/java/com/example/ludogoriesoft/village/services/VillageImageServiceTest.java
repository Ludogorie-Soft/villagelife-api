package com.example.ludogorieSoft.village.services;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageImage;
import com.example.ludogorieSoft.village.repositories.VillageImageRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.tika.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final String UPLOAD_DIRECTORY = "src/main/resources/static/village_images/";

    private static final Logger logger = LoggerFactory.getLogger(VillageImageService.class);
    @Mock
    private File mockFile;

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
        assertTrue(fileName.endsWith(".jpg"));
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

        assertTrue(existingDir.exists());
        assertTrue(existingDir.isDirectory());
    }

    @Test
    void testCreateUploadDirectoryNonExistingDirectory() {
        String nonExistingDirPath = "path/to/non/existing/directory";
        File nonExistingDir = new File(nonExistingDirPath);
        villageImageService.createUploadDirectory(nonExistingDirPath);
        assertTrue(nonExistingDir.exists());
        assertTrue(nonExistingDir.isDirectory());
    }

    @Test
    void testCreateUploadDirectoryNullPath() {
        assertThrows(NullPointerException.class, () -> villageImageService.createUploadDirectory(null));
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

        villageImageService.createVillageImageDTO(villageId, fileName, fixedTimestamp, false, null);

        verify(villageImageRepository).save(any(VillageImage.class));
    }


    @Test
    void testCreateImagePathsWithInvalidImages() {
        byte[] emptyImage = {};
        Long villageId = 123L;
        List<String> result = villageImageService.createImagePaths(List.of(emptyImage), villageId, null, false, null);
        assertTrue(result.isEmpty());
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
            byte[] dummyImageData = {0x01, 0x02, 0x03};
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

    @Test
    void testGetAllVillageDTOsWithImagesWhenNotFound() {
        List<VillageDTO> result = villageImageService.getAllApprovedVillageDTOsWithImages();
        assertTrue(result.isEmpty());
    }

    @Test
    void testAddVillageImagesImageFileDoesNotExist() {
        VillageImage villageImage1 = new VillageImage();
        villageImage1.setImageName("image1.png");

        List<String> base64Images = new ArrayList<>();

        villageImageService.addVillageImages(base64Images, List.of(villageImage1));

        Assertions.assertEquals(0, base64Images.size());
    }


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

        assertTrue(result.isEmpty());
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

        assertTrue(result.isEmpty());
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


    @Test
    void testUpdateVillageImageWhenValidId() {
        Long id = 1L;
        VillageImageDTO villageImageDTO = new VillageImageDTO();
        villageImageDTO.setVillageId(2L);
        villageImageDTO.setImageName("NewImageName");
        villageImageDTO.setStatus(true);
        villageImageDTO.setDateUpload(LocalDateTime.now());

        VillageImage villageImage = new VillageImage();
        when(villageImageRepository.findById(id)).thenReturn(Optional.of(villageImage));

        Village village = new Village();
        when(villageService.checkVillage(villageImageDTO.getVillageId())).thenReturn(village);

        VillageImage updatedVillageImage = new VillageImage();
        when(villageImageRepository.save(any())).thenReturn(updatedVillageImage);

        when(modelMapper.map(any(), eq(VillageImageDTO.class))).thenReturn(villageImageDTO);

        VillageImageDTO updatedImageDTO = villageImageService.updateVillageImage(id, villageImageDTO);

        verify(villageImageRepository).findById(id);
        verify(villageService).checkVillage(villageImageDTO.getVillageId());
        verify(villageImageRepository).save(any());

        Assertions.assertNotNull(updatedImageDTO);
        assertEquals(villageImageDTO.getImageName(), updatedImageDTO.getImageName());
        assertEquals(villageImageDTO.getStatus(), updatedImageDTO.getStatus());
        assertEquals(villageImageDTO.getDateUpload(), updatedImageDTO.getDateUpload());
    }

    @Test
    void testUpdateVillageImageWhenInvalidId() {
        Long id = 1L;
        VillageImageDTO villageImageDTO = new VillageImageDTO();
        villageImageDTO.setVillageId(2L);

        when(villageImageRepository.findById(id)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> villageImageService.updateVillageImage(id, villageImageDTO));

        assertEquals("VillageImage with id: " + id + " not found", exception.getMessage());
    }


    @Test
    void testRejectSingleVillageImage() {
        Long villageId = 1L;
        VillageImageDTO villageImageDTO = new VillageImageDTO();
        villageImageDTO.setVillageId(villageId);

        LocalDateTime dateDelete = LocalDateTime.now();

        Village village = new Village();
        VillageImage villageImage = new VillageImage();

        when(villageService.checkVillage(villageId)).thenReturn(village);
        when(villageImageService.villageImageDTOToVillageImage(villageImageDTO)).thenReturn(villageImage);
        when(villageImageRepository.save(any(VillageImage.class))).thenReturn(villageImage);

        VillageImageDTO result = villageImageService.rejectSingleVillageImage(villageImageDTO, dateDelete);

        verify(villageService).checkVillage(villageId);
        verify(villageImageService).villageImageDTOToVillageImage(villageImageDTO);
        verify(villageImageRepository).save(villageImage);

        Assertions.assertNotNull(result);
        assertEquals(dateDelete, result.getDateDeleted());
        assertEquals(villageImage, villageImageService.villageImageDTOToVillageImage(villageImageDTO));
    }

    @Test
    void testDeleteVillageImageByIdWhenSuccessfulDeletion() {
        Long id = 1L;
        when(villageImageRepository.existsById(id)).thenReturn(true);
        villageImageService.deleteVillageImageById(id);
        verify(villageImageRepository).deleteById(id);
    }

    @Test
    void testFileExistsWhenFileExists() {
        when(mockFile.exists()).thenReturn(true);
        boolean result = villageImageService.fileExists(mockFile);
        assertTrue(result);
        verify(mockFile).exists();
    }

    @Test
    void testFileExistsWhenFileDoesNotExist() {
        when(mockFile.exists()).thenReturn(false);
        boolean result = villageImageService.fileExists(mockFile);
        assertFalse(result);
        verify(mockFile).exists();
    }


    @Test
    void deleteFileWithRetriesWhenTrue() {
        String filePath = "src/main/resources/static/village_images/test.jpg";

        try {
            File file = new File(filePath);
            if (file.createNewFile()) {
                System.out.println("File created successfully.");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean result = villageImageService.deleteFileWithRetries(new File(filePath));

        assertTrue(result);

        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete file.");
            }
        }
    }

    @Test
    void deleteFileWithRetriesWhenFalse() {
        String filePath = UPLOAD_DIRECTORY + "testFile";
        File file = new File(filePath);
        boolean result = villageImageService.deleteFileWithRetries(file);
        assertFalse(result);
    }

    @Test
    void testDeleteImageFileByIdWhenFileDoesNotExistAndDeletionFails() {
        Long id = 1L;
        String imageName = "testImage.jpg";

        VillageImageDTO villageImageDTO = new VillageImageDTO();
        villageImageDTO.setId(id);
        villageImageDTO.setImageName(imageName);

        VillageImage villageImage = new VillageImage();
        villageImage.setId(id);
        villageImage.setImageName(imageName);

        when(villageImageRepository.findById(id)).thenReturn(Optional.of(villageImage));

        villageImageService.deleteImageFileById(id);

        verify(villageImageService, never()).deleteVillageImageById(id);
    }

    @Test
    void testDeleteAllImageFilesByVillageId() {
        Long villageId = 1L;

        List<VillageImage> villageImages = new ArrayList<>();
        VillageImage villageImage = new VillageImage();
        villageImage.setId(1L);
        villageImages.add(villageImage);

        when(villageImageRepository.findByVillageId(villageId)).thenReturn(villageImages);

        doNothing().when(villageImageService).deleteImageFileById(anyLong());

        villageImageService.deleteAllImageFilesByVillageId(villageId);

        verify(villageImageRepository, times(1)).findByVillageId(villageId);
        verify(villageImageService, times(1)).deleteImageFileById(1L);
    }

//    @Test
//    void testIsImageFileWithValidImage() {
//        String validImageFileName = "image.jpg";
//        boolean isImage = villageImageService.isImageFile(validImageFileName);
//        assertTrue(isImage);
//    }
//
//    @Test
//    void testIsImageFileWithInvalidImage() {
//        String invalidFileName = "document.pdf";
//        boolean isImage = villageImageService.isImageFile(invalidFileName);
//        assertFalse(isImage);
//    }
//
//    @Test
//    void testIsImageFileWithMixedCase() {
//        String mixedCaseFileName = "Image.PnG";
//        boolean isImage = villageImageService.isImageFile(mixedCaseFileName);
//        assertTrue(isImage);
//    }
//
//    @Test
//    void testIsImageFileWithNoExtension() {
//        String fileNameWithNoExtension = "fileWithNoExtension";
//        boolean isImage = villageImageService.isImageFile(fileNameWithNoExtension);
//        assertFalse(isImage);
//    }
    @Test
    void testGetVillageNameFromFileNameWhenValid() {
        String villageName = "име на село";
        String result = villageImageService.getVillageNameFromFileName(villageName + ".jpg");
        assertEquals(villageName, result);
    }

    @Test
    void testGetVillageNameFromFileNameOnlyVillageName() {
        String villageName = "Черковна";
        String result = villageImageService.getVillageNameFromFileName(villageName + ".png");
        assertEquals(villageName, result);
    }

    @Test
    void testGetVillageNameFromFileNameWithMixedCaseInput() {
        String fileName = "село-VilLaGe-2x.jpg";
        String villageName = villageImageService.getVillageNameFromFileName(fileName);
        assertEquals("село VilLaGe 2x", villageName);
    }

    @Test
    void testGetVillageNameFromFileNameWithDigits() {
        String fileName = "село-123-4x.png";
        String villageName = villageImageService.getVillageNameFromFileName(fileName);
        assertEquals("село 123 4x", villageName);
    }
    @Test
    void testUploadImages() {
        List<File> images = new ArrayList<>();
        File image1 = new File("image1.jpg");
        File image2 = new File("image2.png");
        images.add(image1);
        images.add(image2);

        when(villageImageService.getAllImageFilesFromDirectory()).thenReturn(images);
        when(villageImageService.getVillageNameFromFileName("image1.jpg")).thenReturn("Village1");
        when(villageImageService.getVillageNameFromFileName("image2.png")).thenReturn("Village2");
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(1L);
        villageDTO.setName("Village1");
        when(villageService.getVillageByName("Village1")).thenReturn(villageDTO);
        doThrow(new ApiRequestException("Village not found")).when(villageService).getVillageByName("Village2");

        villageImageService.uploadImages();

        verify(villageService, times(1)).getVillageByName("Village1");
        verify(villageService, times(1)).getVillageByName("Village2");
    }

}
