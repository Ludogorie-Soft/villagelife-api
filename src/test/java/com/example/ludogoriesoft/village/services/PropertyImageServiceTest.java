package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyImageDTO;
import com.example.ludogorieSoft.village.model.PropertyImage;
import com.example.ludogorieSoft.village.repositories.PropertyImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
