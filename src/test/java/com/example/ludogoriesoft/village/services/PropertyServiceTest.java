package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.PropertyImageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.repositories.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private VillageService villageService;

    @Mock
    private PropertyImageService propertyImageService;

    @Mock
    private ImageService imageService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PropertyService propertyService;

    private Property property;
    private PropertyDTO propertyDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        property = new Property();
        property.setImageUrl("image1.jpg");

        propertyDTO = new PropertyDTO();
        propertyDTO.setImageUrl("image1.jpg");

        propertyDTO.setMainImageBytes(new byte[]{1, 2, 3});
        propertyDTO.setHeating(Arrays.asList("Heating1", "Heating2"));
        propertyDTO.setHeatingText("ExtraHeating1,ExtraHeating2");
        PropertyImageDTO propertyImageDTO = new PropertyImageDTO();
        propertyImageDTO.setImageName("image1.jpg");
        propertyDTO.setImages(Collections.singletonList(propertyImageDTO));
        when(modelMapper.map(any(PropertyDTO.class), eq(Property.class))).thenReturn(property);
        when(modelMapper.map(any(Property.class), eq(PropertyDTO.class))).thenReturn(propertyDTO);
    }

    @Test
    void whenImageUrlIsNullThenDoesNotCallImageService() {
        PropertyDTO testPropertyDTO = new PropertyDTO();
        testPropertyDTO.setImageUrl(null);
        propertyService.addMainImageToPropertyDTO(testPropertyDTO);
        verify(imageService, never()).getImageFromSpace(anyString());
        assertNull(testPropertyDTO.getImageUrl());
    }

    @Test
    void whenImageUrlIsEmptyThenDoesNotCallImageService() {
        PropertyDTO testPropertyDTO = new PropertyDTO();
        testPropertyDTO.setImageUrl("");
        propertyService.addMainImageToPropertyDTO(testPropertyDTO);
        verify(imageService, never()).getImageFromSpace(anyString());
        assertEquals("", testPropertyDTO.getImageUrl());
    }

    @Test
    void whenImageUrlIsValidThenCallsImageServiceAndSetsImageUrl() {
        PropertyDTO testPropertyDTO = new PropertyDTO();
        String imageUrl = "some-image-path";
        testPropertyDTO.setImageUrl(imageUrl);
        String base64Image = "base64EncodedImage";
        when(imageService.getImageFromSpace(imageUrl)).thenReturn(base64Image);
        propertyService.addMainImageToPropertyDTO(testPropertyDTO);
        verify(imageService, times(1)).getImageFromSpace(imageUrl);
        assertEquals(base64Image, testPropertyDTO.getImageUrl());
    }

    @Test
    void testGetAllPropertiesAndMainImage() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Property> properties = Arrays.asList(property);
        Page<Property> propertyPage = new PageImpl<>(properties, pageable, 1);
        when(propertyRepository.findByDeletedAtIsNullOrderByCreatedAtDesc(pageable)).thenReturn(propertyPage);
        when(modelMapper.map(any(Property.class), eq(PropertyDTO.class))).thenReturn(propertyDTO);

        Page<PropertyDTO> result = propertyService.getAllPropertiesAndMainImage(0, 5);

        assertEquals(1, result.getTotalElements());
        verify(propertyRepository, times(1)).findByDeletedAtIsNullOrderByCreatedAtDesc(pageable);
        verify(modelMapper, times(1)).map(property, PropertyDTO.class);
    }

    @Test
    void testGetAllPropertiesByVillageIdAndMainImage() {
        List<Property> properties = Arrays.asList(property);
        when(propertyRepository.findByVillageIdAndDeletedAtIsNullOrderByCreatedAtDesc(1L)).thenReturn(properties);
        when(modelMapper.map(any(Property.class), eq(PropertyDTO.class))).thenReturn(propertyDTO);

        List<PropertyDTO> result = propertyService.getAllPropertiesByVillageIdAndMainImage(1L);

        assertEquals(1, result.size());
        verify(propertyRepository, times(1)).findByVillageIdAndDeletedAtIsNullOrderByCreatedAtDesc(1L);
        verify(modelMapper, times(1)).map(property, PropertyDTO.class);
    }

    @Test
    void testGetPropertyWithMainImageById_Found() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

        PropertyDTO result = propertyService.getPropertyWithMainImageById(1L);

        assertEquals(propertyDTO, result);
        verify(propertyRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(property, PropertyDTO.class);
    }

    @Test
    void testGetPropertyWithMainImageById_NotFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            propertyService.getPropertyWithMainImageById(1L);
        });

        assertEquals("Property with id: 1 Not Found", exception.getMessage());
        verify(propertyRepository, times(1)).findById(1L);
    }
    @Test
    void splitHeatingText_ShouldReturnListOfHeatingTypes() {

        String heatingText = "Wood, Gas, Electric, Oil";

        List<String> result = propertyService.splitHeatingText(heatingText);

        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains("Wood"));
        assertTrue(result.contains("Gas"));
        assertTrue(result.contains("Electric"));
        assertTrue(result.contains("Oil"));
    }

    @Test
    void splitHeatingText_ShouldTrimExtraSpaces() {
        String heatingText = " Wood ;  Gas  , Electric   ;   Oil ";

        List<String> result = propertyService.splitHeatingText(heatingText);

        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains("Wood"));
        assertTrue(result.contains("Gas"));
        assertTrue(result.contains("Electric"));
        assertTrue(result.contains("Oil"));
    }
    @Test
    void splitHeatingText_ShouldReturnEmptyList_WhenInputIsEmpty() {
        String heatingText = "";

        List<String> result = propertyService.splitHeatingText(heatingText);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @Test
    void splitHeatingText_ShouldReturnEmptyList_WhenInputIsNull() {
        String heatingText = null;

        List<String> result = propertyService.splitHeatingText(heatingText);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @Test
    void splitHeatingText_ShouldReturnListWithOneHeatingType_WhenOnlyOneTypeIsProvided() {
        String heatingText = "Gas";

        List<String> result = propertyService.splitHeatingText(heatingText);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("Gas"));
    }
    @Test
    void splitHeatingText_ShouldIgnoreExtraSpacesBetweenHeatingTypes() {
        String heatingText = " Wood   ,   Gas , Electric  ,    Oil ";

        List<String> result = propertyService.splitHeatingText(heatingText);

        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains("Wood"));
        assertTrue(result.contains("Gas"));
        assertTrue(result.contains("Electric"));
        assertTrue(result.contains("Oil"));
    }

    @Test
    void createProperty_success() {
        Property property = new Property();
        property.setImageUrl("mockedImageName.jpg");
        property.setHeating(Arrays.asList("Heating1", "Heating2", "ExtraHeating1", "ExtraHeating2"));

        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setMainImageBytes(new byte[]{1, 2, 3});
        propertyDTO.setHeating(Arrays.asList("Heating1", "Heating2"));
        propertyDTO.setHeatingText("ExtraHeating1,ExtraHeating2");

        when(imageService.uploadImage(any(byte[].class), any(String.class))).thenReturn("mockedImageName.jpg");
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(modelMapper.map(any(Property.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Property sourceProperty = invocation.getArgument(0);
            PropertyDTO mappedPropertyDTO = new PropertyDTO();
            mappedPropertyDTO.setImageUrl(sourceProperty.getImageUrl());
            mappedPropertyDTO.setHeating(sourceProperty.getHeating());
            return mappedPropertyDTO;
        });

        PropertyDTO result = propertyService.createProperty(propertyDTO);

        assertNotNull(result);
        assertEquals("mockedImageName.jpg", result.getImageUrl());
        assertEquals(Arrays.asList("Heating1", "Heating2", "ExtraHeating1", "ExtraHeating2"), result.getHeating());
        verify(propertyRepository, times(1)).save(any(Property.class));
        verify(imageService, times(1)).uploadImage(any(byte[].class), any(String.class));
    }
    @Test
    void createProperty_ShouldHandleNoHeatingText() {
        Property property = new Property();
        property.setImageUrl("mockedImageName.jpg");
        property.setHeating(Arrays.asList("Heating1", "Heating2"));

        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setMainImageBytes(new byte[]{1, 2, 3});
        propertyDTO.setHeating(Arrays.asList("Heating1", "Heating2"));
        propertyDTO.setHeatingText("");

        when(imageService.uploadImage(any(byte[].class), any(String.class))).thenReturn("mockedImageName.jpg");
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(modelMapper.map(any(Property.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Property sourceProperty = invocation.getArgument(0);
            PropertyDTO mappedPropertyDTO = new PropertyDTO();
            mappedPropertyDTO.setImageUrl(sourceProperty.getImageUrl());
            mappedPropertyDTO.setHeating(sourceProperty.getHeating());
            return mappedPropertyDTO;
        });

        PropertyDTO result = propertyService.createProperty(propertyDTO);

        assertNotNull(result);
        assertEquals("mockedImageName.jpg", result.getImageUrl());
        assertEquals(Arrays.asList("Heating1", "Heating2"), result.getHeating());
        verify(propertyRepository, times(1)).save(any(Property.class));
        verify(imageService, times(1)).uploadImage(any(byte[].class), any(String.class));
    }
    @Test
    void createProperty_ShouldHandleSpecialCharactersInHeatingText() {
        Property property = new Property();
        property.setImageUrl("mockedImageName.jpg");
        property.setHeating(Arrays.asList("Heating1", "Heating2", "ExtraHeating1", "ExtraHeating2"));

        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setMainImageBytes(new byte[]{1, 2, 3});
        propertyDTO.setHeating(Arrays.asList("Heating1", "Heating2"));
        propertyDTO.setHeatingText("ExtraHeating1@#,$^%&ExtraHeating2");

        when(imageService.uploadImage(any(byte[].class), any(String.class))).thenReturn("mockedImageName.jpg");
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(modelMapper.map(any(Property.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Property sourceProperty = invocation.getArgument(0);
            PropertyDTO mappedPropertyDTO = new PropertyDTO();
            mappedPropertyDTO.setImageUrl(sourceProperty.getImageUrl());
            mappedPropertyDTO.setHeating(sourceProperty.getHeating());
            return mappedPropertyDTO;
        });

        PropertyDTO result = propertyService.createProperty(propertyDTO);

        assertNotNull(result);
        assertEquals("mockedImageName.jpg", result.getImageUrl());
        assertEquals(Arrays.asList("Heating1", "Heating2", "ExtraHeating1", "ExtraHeating2"), result.getHeating());
        verify(propertyRepository, times(1)).save(any(Property.class));
        verify(imageService, times(1)).uploadImage(any(byte[].class), any(String.class));
    }
    @Test
    void createProperty_ShouldHandleNullHeatingList() {
        Property property = new Property();
        property.setImageUrl("mockedImageName.jpg");
        property.setHeating(Arrays.asList("Heating1", "Heating2", "ExtraHeating1", "ExtraHeating2"));

        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setMainImageBytes(new byte[]{1, 2, 3});
        propertyDTO.setHeating(null);
        propertyDTO.setHeatingText("ExtraHeating1,ExtraHeating2");

        when(imageService.uploadImage(any(byte[].class), any(String.class))).thenReturn("mockedImageName.jpg");
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(modelMapper.map(any(Property.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Property sourceProperty = invocation.getArgument(0);
            PropertyDTO mappedPropertyDTO = new PropertyDTO();
            mappedPropertyDTO.setImageUrl(sourceProperty.getImageUrl());
            mappedPropertyDTO.setHeating(sourceProperty.getHeating());
            return mappedPropertyDTO;
        });

        PropertyDTO result = propertyService.createProperty(propertyDTO);

        assertNotNull(result);
        assertEquals("mockedImageName.jpg", result.getImageUrl());
        assertEquals(Arrays.asList("Heating1", "Heating2", "ExtraHeating1", "ExtraHeating2"), result.getHeating());
        verify(propertyRepository, times(1)).save(any(Property.class));
        verify(imageService, times(1)).uploadImage(any(byte[].class), any(String.class));
    }

}
