package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.PropertyImageDTO;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.PropertyImage;
import com.example.ludogorieSoft.village.repositories.PropertyImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PropertyImageServiceTest {

    @Mock
    private PropertyImageRepository propertyImageRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PropertyImageService propertyImageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        PropertyImage propertyImage = new PropertyImage();
        PropertyImageDTO propertyImageDTO = new PropertyImageDTO();

        when(modelMapper.map(any(PropertyImageDTO.class), eq(PropertyImage.class))).thenReturn(propertyImage);
        when(modelMapper.map(any(PropertyImage.class), eq(PropertyImageDTO.class))).thenReturn(propertyImageDTO);
    }


    @Test
    void testPropertyImageToPropertyImageDTO() {
        PropertyImage propertyImage = new PropertyImage();
        propertyImage.setImageName("image1.jpg");
        PropertyImageDTO propertyImageDTO = new PropertyImageDTO();
        propertyImageDTO.setImageName("image1.jpg");

        when(modelMapper.map(propertyImage, PropertyImageDTO.class)).thenReturn(propertyImageDTO);

        PropertyImageDTO result = propertyImageService.propertyImageToPropertyImageDTO(propertyImage);

        assertEquals(propertyImageDTO, result);
        verify(modelMapper, times(1)).map(propertyImage, PropertyImageDTO.class);
    }

    @Test
    void testGetPropertyImagesByPropertyId() {
        PropertyImage propertyImage = new PropertyImage();
        propertyImage.setImageName("image1.jpg");
        PropertyImageDTO propertyImageDTO = new PropertyImageDTO();
        propertyImageDTO.setImageName("image1.jpg");

        List<PropertyImage> propertyImages = Arrays.asList(propertyImage);
        when(propertyImageRepository.findByProperty_VillageIdAndDeletedAtIsNull(1L)).thenReturn(propertyImages);
        when(imageService.getImageFromSpace("image1.jpg")).thenReturn("updated_image1.jpg");
        when(modelMapper.map(any(PropertyImage.class), eq(PropertyImageDTO.class))).thenReturn(propertyImageDTO);

        List<PropertyImageDTO> result = propertyImageService.getPropertyImagesByPropertyId(1L);

        assertEquals(1, result.size());
        verify(propertyImageRepository, times(1)).findByProperty_VillageIdAndDeletedAtIsNull(1L);
        verify(imageService, times(1)).getImageFromSpace("image1.jpg");
        verify(modelMapper, times(1)).map(propertyImage, PropertyImageDTO.class);
    }
    @Test
    void createPropertyImage_ShouldSaveAndUploadImages() {
        ImageService imageService = mock(ImageService.class);
        PropertyImageRepository propertyImageRepository = mock(PropertyImageRepository.class);
        ModelMapper modelMapper = mock(ModelMapper.class);

        PropertyImage propertyImage = new PropertyImage();
        when(modelMapper.map(any(PropertyImageDTO.class), eq(PropertyImage.class))).thenReturn(propertyImage);

        PropertyImageDTO propertyImageDTO1 = mock(PropertyImageDTO.class);
        when(propertyImageDTO1.getPropertyImageBytes()).thenReturn(new byte[]{1, 2, 3});
        when(propertyImageDTO1.getImageName()).thenReturn("imageName1");

        PropertyImageDTO propertyImageDTO2 = mock(PropertyImageDTO.class);
        when(propertyImageDTO2.getPropertyImageBytes()).thenReturn(new byte[]{4, 5, 6});
        when(propertyImageDTO2.getImageName()).thenReturn("imageName2");

        PropertyImageService propertyImageServiceToTest = new PropertyImageService(propertyImageRepository, modelMapper, imageService);

        List<PropertyImageDTO> propertyImageDTOs = Arrays.asList(propertyImageDTO1, propertyImageDTO2);
        Property property = new Property();

        List<PropertyImageDTO> result = propertyImageServiceToTest.createPropertyImage(propertyImageDTOs, property);

        verify(imageService, times(2)).uploadImage(any(byte[].class), any(String.class));
        verify(propertyImageRepository, times(2)).save(any(PropertyImage.class));

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
