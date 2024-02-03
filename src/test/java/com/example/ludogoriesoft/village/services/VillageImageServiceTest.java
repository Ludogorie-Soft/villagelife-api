package com.example.ludogorieSoft.village.services;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.example.ludogorieSoft.village.dtos.UserDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.dtos.VillageImageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.model.VillageImage;
import com.example.ludogorieSoft.village.repositories.VillageImageRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class VillageImageServiceTest {
    @Mock
    private VillageImageRepository villageImageRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private VillageService villageService;
    @Mock
    private ImageService imageService;
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
    public void testGetAllImagesForVillageByStatusAndDate() {

        VillageImage villageImage1 = new VillageImage();
        villageImage1.setImageName("image1.png");
        List<VillageImage> villageImages = new ArrayList<>();
        villageImages.add(villageImage1);
        when(villageImageRepository.findByVillageIdAndVillageStatusAndDateDeletedIsNull(anyLong(), eq(true))).thenReturn(villageImages);

        when(imageService.getImageFromSpace(anyString())).thenReturn("image1.png");

        List<String> resultImages = villageImageService.getAllImagesForVillageByStatusAndDate(1L, true, null);

        verify(villageImageRepository).findByVillageIdAndVillageStatusAndDateDeletedIsNull(anyLong(), eq(true));
        verify(imageService, times(villageImages.size())).getImageFromSpace(anyString());

        assertNotNull(resultImages);
        assertEquals(villageImages.size(), resultImages.size());
        assertEquals("image1.png", resultImages.get(0));
    }

    @Test
    void testAddVillageImages() {
        List<String> base64Images = new ArrayList<>();
        VillageImage villageImage1 = new VillageImage();
        villageImage1.setImageName("image1.png");
        List<VillageImage> villageImages = new ArrayList<>();
        villageImages.add(villageImage1);

        when(imageService.getImageFromSpace(anyString())).thenReturn("mockedBase64Image");

        villageImageService.addVillageImages(base64Images, villageImages);

        verify(imageService, times(villageImages.size())).getImageFromSpace(anyString());
        assertEquals(villageImages.size(), base64Images.size());
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
    void testGetDeletedVillageImageDTOsByVillageId() {
        Long villageId = 1L;

        VillageImage villageImage = new VillageImage();
        villageImage.setId(villageId);
        villageImage.setImageName("image1.png");

        VillageImageDTO villageImageDTO = new VillageImageDTO();
        villageImageDTO.setId(villageId);
        villageImageDTO.setImageName("image1.png");

        List<VillageImage> villageImages = new ArrayList<>();
        villageImages.add(villageImage);

        when(villageImageRepository.findDeletedByVillageId(villageId)).thenReturn(villageImages);
        when(imageService.getImageFromSpace("image1.png")).thenReturn("image1.png");
        when(modelMapper.map(villageImage, VillageImageDTO.class)).thenReturn(villageImageDTO);

        List<VillageImageDTO> result = villageImageService.getDeletedVillageImageDTOsByVillageId(villageId);

        verify(villageImageRepository).findDeletedByVillageId(villageId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(villageImages.size(), result.size());

        assertEquals(villageImages.get(0).getId(), result.get(0).getId());
        assertEquals(villageImages.get(0).getImageName(), result.get(0).getImageName());
    }

    @Test
    public void testGetNotDeletedVillageImageDTOsByVillageId() {
        Long villageId = 1L;

        VillageImage villageImage1 = new VillageImage();
        villageImage1.setImageName("image1.png");
        VillageImageDTO villageImageDTO = new VillageImageDTO();
        villageImageDTO.setId(villageId);
        villageImageDTO.setImageName("image1.png");
        villageImageDTO.setBase64Image("image1.png");

        List<VillageImage> villageImages = new ArrayList<>();
        villageImages.add(villageImage1);

        when(villageImageRepository.findNotDeletedByVillageId(anyLong())).thenReturn(villageImages);

        when(imageService.getImageFromSpace(anyString())).thenReturn("image1.png");
        when(modelMapper.map(villageImage1, VillageImageDTO.class)).thenReturn(villageImageDTO);

        List<VillageImageDTO> resultImageDTOs = villageImageService.getNotDeletedVillageImageDTOsByVillageId(villageId);

        verify(villageImageRepository).findNotDeletedByVillageId(villageId);
        verify(imageService, times(villageImages.size())).getImageFromSpace(anyString());

        assertNotNull(resultImageDTOs);
        assertEquals(villageImages.size(), resultImageDTOs.size());
        assertEquals("image1.png", resultImageDTOs.get(0).getBase64Image());
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
        VillageImage villageImage = new VillageImage();
        villageImage.setId(1L);
        villageImage.setImageName("image");
        List<VillageImage> mockVillageImages = new ArrayList<>();
        mockVillageImages.add(villageImage);

        when(villageImageRepository.findByVillageIdAndVillageStatusAndDateDeletedIsNull(villageId, status))
                .thenReturn(mockVillageImages);
        when(imageService.getImageFromSpace("image")).thenReturn("image");


        List<String> result = villageImageService.getAllImagesForVillageByStatusAndDate(villageId, status, null);

        verify(villageImageRepository).findByVillageIdAndVillageStatusAndDateDeletedIsNull(villageId, status);

        assertTrue(result.contains("image"));
        assertNotNull(result.get(0));
        assertEquals(villageImage.getImageName(), result.get(0));
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

        assertFalse(result.isEmpty());
        assertNull(result.get(0));
    }

    @Test
    void testGetAllImagesForVillageByStatusAndDateWhenNoImages() {
        Long villageId = 1L;
        boolean status = true;

        when(villageImageRepository.findByVillageIdAndVillageStatusAndDateDeletedIsNull(villageId, status))
                .thenReturn(new ArrayList<>());

        List<String> result = villageImageService.getAllImagesForVillageByStatusAndDate(villageId, status, null);

        verify(villageImageRepository).findByVillageIdAndVillageStatusAndDateDeletedIsNull(villageId, status);

        assertTrue(result.contains(null));
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
    public void testGetApprovedVillageDTOsWithImages() {
        int pageNumber = 0;
        int elementsCount = 10;

        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village 1");

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village 1");

        when(villageService.getVillagesByStatus(eq(true), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(villageDTO1)));
        when(villageImageRepository.findByVillageIdAndVillageStatusAndDateDeletedIsNull(eq(1L), eq(true)))
                .thenReturn(Collections.emptyList());

        Page<VillageDTO> resultPage = villageImageService.getApprovedVillageDTOsWithImages(pageNumber, elementsCount);

        assertEquals(1, resultPage.getContent().size());
        VillageDTO resultVillageDTO = resultPage.getContent().get(0);
        assertEquals(1L, resultVillageDTO.getId().longValue());
        assertEquals("Village 1", resultVillageDTO.getName());

        verify(villageService).getVillagesByStatus(eq(true), any(Pageable.class));
        verify(villageImageRepository).findByVillageIdAndVillageStatusAndDateDeletedIsNull(eq(1L), eq(true));
        verify(imageService, never()).getImageFromSpace(anyString()); // Assuming imageService should not be called in this test
    }

    @Test
    public void testUpdateVillageImagesStatus() {
        Long id = 1L;
        boolean status = true;
        String localDateTime = "2022-01-01T12:00:00";

        VillageImage villageImage1 = new VillageImage();
        villageImage1.setId(id);
        villageImage1.setVillageStatus(status);

        VillageImageDTO villageImageDTO = new VillageImageDTO();
        villageImageDTO.setId(id);
        villageImageDTO.setImageName("image");

        List<VillageImage> villageImages = List.of(villageImage1);

        when(villageImageRepository.findByVillageIdAndVillageStatusAndDateUpload(eq(id), eq(status), eq(localDateTime)))
                .thenReturn(villageImages);
        when(modelMapper.map(any(VillageImage.class), eq(VillageImageDTO.class)))
                .thenReturn(villageImageDTO);
        when(villageImageRepository.findById(id)).thenReturn(Optional.of(villageImage1));

        villageImageService.updateVillageImagesStatus(id, status, localDateTime);

        verify(villageImageRepository).findByVillageIdAndVillageStatusAndDateUpload(eq(id), eq(status), eq(localDateTime));
        verify(modelMapper).map(any(VillageImage.class), eq(VillageImageDTO.class));
        verify(villageImageService).updateVillageImage(eq(id), any(VillageImageDTO.class));
    }
    @Test
    void testCreateImagePaths2222() {
        List<String> imageUUIDs = Arrays.asList("uuid1", "uuid2");
        long villageId = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();
        boolean status = true;
        UserDTO userDTO = new UserDTO();

        doNothing().when(villageImageService).createVillageImageDTO(eq(villageId), anyString(), eq(localDateTime), eq(status), eq(userDTO));

        villageImageService.createImagePaths2222(imageUUIDs, villageId, localDateTime, status, userDTO);

        verify(villageImageService, times(imageUUIDs.size()))
                .createVillageImageDTO(eq(villageId), anyString(), eq(localDateTime), eq(status), eq(userDTO));
    }
}
