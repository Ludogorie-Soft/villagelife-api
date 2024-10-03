package com.example.ludogoriesoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.repositories.PropertyRepository;
import com.example.ludogorieSoft.village.services.ImageService;
import com.example.ludogorieSoft.village.services.PropertyService;
import com.example.ludogorieSoft.village.services.VillageService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PropertyServiceTest {
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private PropertyService propertyService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private VillageService villageService;
    @Mock
    private ImageService imageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenImageUrlIsNullThenDoesNotCallImageService() {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setImageUrl(null);
        propertyService.addMainImageToPropertyDTO(propertyDTO);
        verify(imageService, never()).getImageFromSpace(anyString());
        assertNull(propertyDTO.getImageUrl());
    }

    @Test
    void whenImageUrlIsEmptyThenDoesNotCallImageService() {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setImageUrl("");
        propertyService.addMainImageToPropertyDTO(propertyDTO);
        verify(imageService, never()).getImageFromSpace(anyString());
        assertEquals("", propertyDTO.getImageUrl());
    }

    @Test
    void whenImageUrlIsValidThenCallsImageServiceAndSetsImageUrl() {
        PropertyDTO propertyDTO = new PropertyDTO();
        String imageUrl = "some-image-path";
        propertyDTO.setImageUrl(imageUrl);
        String base64Image = "base64EncodedImage";
        when(imageService.getImageFromSpace(imageUrl)).thenReturn(base64Image);
        propertyService.addMainImageToPropertyDTO(propertyDTO);
        verify(imageService, times(1)).getImageFromSpace(imageUrl);
        assertEquals(base64Image, propertyDTO.getImageUrl());
    }
}
