package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.BusinessCardDTO;
import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.PropertyStatsDTO;
import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import com.example.ludogorieSoft.village.model.BusinessCard;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.PropertyStats;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private VillageService villageService;

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
    void testPropertyToPropertyDTO() {
        Property property = mock(Property.class);
        AlternativeUser alternativeUser = mock(AlternativeUser.class);
        BusinessCard businessCard = mock(BusinessCard.class);

        when(property.getAlternativeUser()).thenReturn(alternativeUser);
        when(alternativeUser.getBusinessCard()).thenReturn(businessCard);

        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);
        when(villageService.villageToVillageDTO(property.getVillage())).thenReturn(propertyDTO.getVillageDTO());
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(new AlternativeUserDTO());
        when(modelMapper.map(property.getPropertyStats(), PropertyStatsDTO.class)).thenReturn(new PropertyStatsDTO());
        when(modelMapper.map(businessCard, BusinessCardDTO.class)).thenReturn(new BusinessCardDTO());

        PropertyDTO result = propertyService.propertyToPropertyDTO(property);

        assertEquals(propertyDTO, result);
        verify(modelMapper, times(1)).map(property, PropertyDTO.class);
        verify(villageService, times(1)).villageToVillageDTO(property.getVillage());
    }


    @Test
    void testGetAllPropertiesAndMainImage() {
        Pageable pageable = PageRequest.of(0, 5);
        Property property = mock(Property.class);
        PropertyDTO propertyDTO = new PropertyDTO();
        AlternativeUser alternativeUser = mock(AlternativeUser.class);
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setRole(Role.USER);

        List<Property> properties = Arrays.asList(property);
        Page<Property> propertyPage = new PageImpl<>(properties, pageable, 1);

        when(propertyRepository.findByDeletedAtIsNullOrderByCreatedAtDesc(pageable)).thenReturn(propertyPage);
        when(property.getAlternativeUser()).thenReturn(alternativeUser);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(alternativeUserDTO);
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

        Page<PropertyDTO> result = propertyService.getAllPropertiesAndMainImage(0, 5);

        assertEquals(1, result.getTotalElements());
        assertEquals(propertyDTO, result.getContent().get(0));
        verify(propertyRepository, times(1)).findByDeletedAtIsNullOrderByCreatedAtDesc(pageable);
        verify(modelMapper, times(1)).map(property, PropertyDTO.class);
        verify(modelMapper, times(1)).map(alternativeUser, AlternativeUserDTO.class);
    }

    @Test
    void testGetAllPropertiesByVillageIdAndMainImage() {
        Property property = mock(Property.class);
        PropertyDTO propertyDTO = new PropertyDTO();
        AlternativeUser alternativeUser = mock(AlternativeUser.class);
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setRole(Role.USER);

        List<Property> properties = List.of(property);
        when(propertyRepository.findByVillageIdAndDeletedAtIsNullOrderByCreatedAtDesc(1L)).thenReturn(properties);

        when(property.getAlternativeUser()).thenReturn(alternativeUser);
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(alternativeUserDTO);

        List<PropertyDTO> result = propertyService.getAllPropertiesByVillageIdAndMainImage(1L);

        assertEquals(1, result.size());
        assertEquals(propertyDTO, result.get(0));
        verify(propertyRepository, times(1)).findByVillageIdAndDeletedAtIsNullOrderByCreatedAtDesc(1L);
        verify(modelMapper, times(1)).map(property, PropertyDTO.class);
        verify(modelMapper, times(1)).map(alternativeUser, AlternativeUserDTO.class);
    }

    @Test
    void testGetPropertyWithMainImageByIdWhenFound() {
        Property property = mock(Property.class);
        AlternativeUser alternativeUser = mock(AlternativeUser.class);
        PropertyDTO propertyDTO = new PropertyDTO();
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setRole(Role.USER);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(property.getAlternativeUser()).thenReturn(alternativeUser);
        when(property.getPropertyStats()).thenReturn(new PropertyStats());
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(alternativeUserDTO);

        PropertyDTO result = propertyService.getPropertyWithMainImageById(1L);

        assertEquals(propertyDTO, result);
        verify(propertyRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(property, PropertyDTO.class);
        verify(modelMapper, times(1)).map(alternativeUser, AlternativeUserDTO.class);
    }

    @Test
    void testGetPropertyWithMainImageByIdWhenNotFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            propertyService.getPropertyWithMainImageById(1L);
        });

        assertEquals("Property with id: 1 Not Found", exception.getMessage());
        verify(propertyRepository, times(1)).findById(1L);
    }

    @Test
    void testGetSearchProperties_withValidParams() {
        List<String> propertyTypes = List.of("HOUSE");
        String propertyTransferType = "SALE";
        Double minBuiltUpArea = 100.0;
        Double maxBuiltUpArea = 200.0;
        Double minYardArea = 50.0;
        Double maxYardArea = 150.0;
        Short minRoomsCount = 2;
        Short maxRoomsCount = 4;
        Short minBathroomsCount = 1;
        Short maxBathroomsCount = 2;
        List<String> heating = List.of("Electric");
        List<String> constructionTypes = List.of("WOOD");
        Short minConstructionYear = 1990;
        Short maxConstructionYear = 2020;
        BigDecimal minPrice = BigDecimal.valueOf(50000);
        BigDecimal maxPrice = BigDecimal.valueOf(100000);
        List<String> ownershipTypes = List.of("AGENCY");
        String villageName = "Sample Village";
        String regionName = "Sample Region";
        Pageable pageable = PageRequest.of(0, 2);

        AlternativeUser alternativeUser = new AlternativeUser();
        alternativeUser.setRole(Role.USER);
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setRole(Role.USER);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(alternativeUserDTO);
        property.setAlternativeUser(alternativeUser);
        Page<Property> propertyPage = new PageImpl<>(List.of(property), pageable, 1);
        when(propertyRepository.searchProperties(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any()
        )).thenReturn(propertyPage);

        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

        Page<PropertyDTO> result = propertyService.getSearchProperties(
                propertyTypes, propertyTransferType, minBuiltUpArea, maxBuiltUpArea,
                minYardArea, maxYardArea, minRoomsCount, maxRoomsCount,
                minBathroomsCount, maxBathroomsCount, heating, constructionTypes,
                minConstructionYear, maxConstructionYear, minPrice, maxPrice,
                ownershipTypes, villageName, regionName, pageable
        );

        verify(propertyRepository, times(1)).searchProperties(
                any(), any(), eq(minBuiltUpArea), eq(maxBuiltUpArea),
                eq(minYardArea), eq(maxYardArea), eq(minRoomsCount), eq(maxRoomsCount),
                eq(minBathroomsCount), eq(maxBathroomsCount), eq(heating), any(),
                eq(minConstructionYear), eq(maxConstructionYear), eq(minPrice), eq(maxPrice),
                any(), eq(villageName), eq(regionName), eq(pageable)
        );
        assertEquals(1, result.getTotalElements());
        assertEquals(propertyDTO, result.getContent().get(0));
    }

    @Test
    void testGetSearchProperties_withEmptyResults() {
        List<String> propertyTypes = List.of("APARTMENT");
        BigDecimal minPrice = BigDecimal.valueOf(1000000);
        Pageable pageable = PageRequest.of(0, 2);

        when(propertyRepository.searchProperties(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any()
        )).thenReturn(Page.empty(pageable));

        Page<PropertyDTO> result = propertyService.getSearchProperties(
                propertyTypes, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, minPrice, null, null, null, null, pageable
        );

        verify(propertyRepository, times(1)).searchProperties(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), eq(minPrice), any(), any(), any(),
                any(), eq(pageable)
        );
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testGetSearchProperties_withNullParams() {
        Pageable pageable = PageRequest.of(0, 2);
        AlternativeUser alternativeUser = new AlternativeUser();
        alternativeUser.setRole(Role.USER);
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setRole(Role.USER);
        property.setAlternativeUser(alternativeUser);
        Page<Property> propertyPage = new PageImpl<>(List.of(property), pageable, 1);
        when(propertyRepository.searchProperties(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any()
        )).thenReturn(propertyPage);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(alternativeUserDTO);
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

        Page<PropertyDTO> result = propertyService.getSearchProperties(
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, pageable
        );

        verify(propertyRepository, times(1)).searchProperties(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), eq(pageable)
        );
        assertEquals(1, result.getTotalElements());
        assertEquals(propertyDTO, result.getContent().get(0));
    }
}
