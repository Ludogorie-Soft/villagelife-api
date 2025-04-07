package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.BusinessCardDTO;
import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.PropertyStatsDTO;
import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.dtos.PropertyImageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import com.example.ludogorieSoft.village.model.BusinessCard;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.PropertyStats;
import com.example.ludogorieSoft.village.repositories.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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

    @Mock
    private Property property;
    @Mock
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
    void propertyDTOToProperty_ShouldMapPropertyDTOToEntity() {
        when(modelMapper.map(propertyDTO, Property.class)).thenReturn(property);
        Property result = propertyService.propertyDTOToProperty(propertyDTO);
        assertEquals(property, result);
        verify(modelMapper, times(1)).map(propertyDTO, Property.class);

    }
    @Test
    void propertyDTOToProperty_ShouldReturnNullIfInputIsNull() {
        Property result = propertyService.propertyDTOToProperty(null);
        assertNull(result);
    }
    @Test
    void propertyToPropertyDTO_ShouldThrowExceptionIfInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> propertyService.propertyToPropertyDTO(null));
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
    void testPropertyToPropertyDTO_WithNullVillage() {
        Property testProperty = mock(Property.class);
        AlternativeUser alternativeUser = mock(AlternativeUser.class);
        BusinessCard businessCard = mock(BusinessCard.class);

        when(testProperty.getAlternativeUser()).thenReturn(alternativeUser);
        when(testProperty.getVillage()).thenReturn(null);
        when(testProperty.getPropertyStats()).thenReturn(mock(PropertyStats.class));
        when(alternativeUser.getBusinessCard()).thenReturn(businessCard);

        when(modelMapper.map(testProperty, PropertyDTO.class)).thenReturn(propertyDTO);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(new AlternativeUserDTO());
        when(modelMapper.map(testProperty.getPropertyStats(), PropertyStatsDTO.class)).thenReturn(new PropertyStatsDTO());
        when(modelMapper.map(businessCard, BusinessCardDTO.class)).thenReturn(new BusinessCardDTO());

        PropertyDTO result = propertyService.propertyToPropertyDTO(testProperty);

        assertEquals(propertyDTO, result);
        verify(modelMapper, times(1)).map(testProperty, PropertyDTO.class);
        verify(villageService, never()).villageToVillageDTO(any());
    }



    @Test
    void testGetAllPropertiesAndMainImage() {
        Pageable pageable = PageRequest.of(0, 5);
        Property testProperty = mock(Property.class);
        PropertyDTO testPropertyDTO = new PropertyDTO();
        AlternativeUser alternativeUser = mock(AlternativeUser.class);
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setRole(Role.USER);

        List<Property> properties = List.of(testProperty);
        Page<Property> propertyPage = new PageImpl<>(properties, pageable, 1);

        when(propertyRepository.findByDeletedAtIsNullOrderByCreatedAtDesc(pageable)).thenReturn(propertyPage);
        when(testProperty.getAlternativeUser()).thenReturn(alternativeUser);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(alternativeUserDTO);
        when(modelMapper.map(testProperty, PropertyDTO.class)).thenReturn(testPropertyDTO);

        Page<PropertyDTO> result = propertyService.getAllPropertiesAndMainImage(0, 5);

        assertEquals(1, result.getTotalElements());
        assertEquals(testPropertyDTO, result.getContent().get(0));
        verify(propertyRepository, times(1)).findByDeletedAtIsNullOrderByCreatedAtDesc(pageable);
        verify(modelMapper, times(1)).map(testProperty, PropertyDTO.class);
        verify(modelMapper, times(1)).map(alternativeUser, AlternativeUserDTO.class);
    }

    @Test
    void testGetAllPropertiesByVillageIdAndMainImage() {
        Property testProperty = mock(Property.class);
        PropertyDTO testPropertyDTO = new PropertyDTO();
        AlternativeUser alternativeUser = mock(AlternativeUser.class);
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setRole(Role.USER);

        List<Property> properties = List.of(testProperty);
        when(propertyRepository.findByVillageIdAndDeletedAtIsNullOrderByCreatedAtDesc(1L)).thenReturn(properties);

        when(testProperty.getAlternativeUser()).thenReturn(alternativeUser);
        when(modelMapper.map(testProperty, PropertyDTO.class)).thenReturn(testPropertyDTO);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(alternativeUserDTO);

        List<PropertyDTO> result = propertyService.getAllPropertiesByVillageIdAndMainImage(1L);

        assertEquals(1, result.size());
        assertEquals(testPropertyDTO, result.get(0));
        verify(propertyRepository, times(1)).findByVillageIdAndDeletedAtIsNullOrderByCreatedAtDesc(1L);
        verify(modelMapper, times(1)).map(testProperty, PropertyDTO.class);
        verify(modelMapper, times(1)).map(alternativeUser, AlternativeUserDTO.class);
    }

    @Test
    void testGetPropertyWithMainImageByIdWhenFound() {
        Property testProperty = mock(Property.class);
        AlternativeUser alternativeUser = mock(AlternativeUser.class);
        PropertyDTO testPropertyDTO = new PropertyDTO();
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setRole(Role.USER);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(testProperty.getAlternativeUser()).thenReturn(alternativeUser);
        when(testProperty.getPropertyStats()).thenReturn(new PropertyStats());
        when(modelMapper.map(testProperty, PropertyDTO.class)).thenReturn(testPropertyDTO);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(alternativeUserDTO);

        PropertyDTO result = propertyService.getPropertyWithMainImageById(1L);

        assertEquals(testPropertyDTO, result);
        verify(propertyRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(testProperty, PropertyDTO.class);
        verify(modelMapper, times(1)).map(alternativeUser, AlternativeUserDTO.class);
    }

    @Test
    void testGetPropertyWithMainImageByIdWhenNotFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> propertyService.getPropertyWithMainImageById(1L));

        assertEquals("Property with id: 1 Not Found", exception.getMessage());
        verify(propertyRepository, times(1)).findById(1L);
    }
    @Test
    void shouldReturnEmptyListWhenHeatingTextIsNull() {
        List<String> result = propertyService.splitHeatingText(null);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenHeatingTextIsEmpty() {
        String heatingText = "   ";

        List<String> result = propertyService.splitHeatingText(heatingText);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnSingleElementWhenHeatingTextContainsOneWord() {
        String heatingText = "Gas";

        List<String> result = propertyService.splitHeatingText(heatingText);

        assertThat(result).containsExactly("Gas");
    }

    @ParameterizedTest
    @CsvSource({
            "'Gas;Oil', 'Gas', 'Oil'",
            "'Gas,Oil', 'Gas', 'Oil'",
            "'Gas Oil', 'Gas', 'Oil'",
            "'  Gas ;  Oil ', 'Gas', 'Oil'"
    })
    void shouldSplitHeatingTextCorrectly(String heatingText, String expected1, String expected2) {
        List<String> result = propertyService.splitHeatingText(heatingText);

        assertThat(result).containsExactly(expected1, expected2);
    }

    @Test
    void shouldHandleMultipleDelimitersInHeatingText() {
        String heatingText = "Gas; Oil,Electric Heating";

        List<String> result = propertyService.splitHeatingText(heatingText);

        assertThat(result).containsExactly("Gas", "Oil", "Electric", "Heating");
    }

    @Test
    void shouldIgnoreExtraDelimiters() {
        String heatingText = "Gas;;; Oil, , ,Electric";

        List<String> result = propertyService.splitHeatingText(heatingText);

        assertThat(result).containsExactly("Gas", "Oil", "Electric");
    }
    @Test
    void createProperty_success() {
        Property testProperty = new Property();
        testProperty.setImageUrl("mockedImageName.jpg");
        testProperty.setHeating(Arrays.asList("Heating1", "Heating2", "ExtraHeating1", "ExtraHeating2"));

        PropertyDTO testPropertyDTO = new PropertyDTO();
        testPropertyDTO.setMainImageBytes(new byte[]{1, 2, 3});
        testPropertyDTO.setHeating(Arrays.asList("Heating1", "Heating2"));
        testPropertyDTO.setHeatingText("ExtraHeating1,ExtraHeating2");

        when(imageService.uploadImage(any(byte[].class), any(String.class))).thenReturn("mockedImageName.jpg");
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);
        when(modelMapper.map(any(Property.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Property sourceProperty = invocation.getArgument(0);
            PropertyDTO mappedPropertyDTO = new PropertyDTO();
            mappedPropertyDTO.setImageUrl(sourceProperty.getImageUrl());
            mappedPropertyDTO.setHeating(sourceProperty.getHeating());
            return mappedPropertyDTO;
        });

        PropertyDTO result = propertyService.createProperty(testPropertyDTO);

        assertNotNull(result);
        assertEquals("mockedImageName.jpg", result.getImageUrl());
        assertEquals(Arrays.asList("Heating1", "Heating2", "ExtraHeating1", "ExtraHeating2"), result.getHeating());
        verify(propertyRepository, times(1)).save(any(Property.class));
        verify(imageService, times(1)).uploadImage(any(byte[].class), any(String.class));
    }
    static Stream<TestData> provideCreatePropertyTestData() {
        return Stream.of(
                new TestData(
                        Arrays.asList("Heating1", "Heating2"),
                        "",
                        Arrays.asList("Heating1", "Heating2"),
                        "mockedImageName.jpg"
                ),
                new TestData(
                        Arrays.asList("Heating1", "Heating2", "ExtraHeating1", "ExtraHeating2"),
                        "ExtraHeating1@#,$^%&ExtraHeating2",
                        Arrays.asList("Heating1", "Heating2", "ExtraHeating1", "ExtraHeating2"),
                        "mockedImageName.jpg"
                ),
                new TestData(
                        null,
                        "ExtraHeating1,ExtraHeating2",
                        Arrays.asList("Heating1", "Heating2", "ExtraHeating1", "ExtraHeating2"),
                        "mockedImageName.jpg"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideCreatePropertyTestData")
    void createProperty_ShouldHandleVariousHeatingText(TestData testData) {
        Property testProperty = new Property();
        testProperty.setImageUrl(testData.expectedImageUrl);
        testProperty.setHeating(testData.expectedHeating);

        PropertyDTO testPropertyDTO = new PropertyDTO();
        testPropertyDTO.setMainImageBytes(new byte[]{1, 2, 3});
        testPropertyDTO.setHeating(testData.inputHeating);
        testPropertyDTO.setHeatingText(testData.inputHeatingText);

        when(imageService.uploadImage(any(byte[].class), any(String.class))).thenReturn(testData.expectedImageUrl);
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);
        when(modelMapper.map(any(Property.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Property sourceProperty = invocation.getArgument(0);
            PropertyDTO mappedPropertyDTO = new PropertyDTO();
            mappedPropertyDTO.setImageUrl(sourceProperty.getImageUrl());
            mappedPropertyDTO.setHeating(sourceProperty.getHeating());
            return mappedPropertyDTO;
        });

        PropertyDTO result = propertyService.createProperty(testPropertyDTO);

        assertNotNull(result);
        assertEquals(testData.expectedImageUrl, result.getImageUrl());
        assertEquals(testData.expectedHeating, result.getHeating());
        verify(propertyRepository, times(1)).save(any(Property.class));
        verify(imageService, times(1)).uploadImage(any(byte[].class), any(String.class));
    }

    static class TestData {
        List<String> inputHeating;
        String inputHeatingText;
        List<String> expectedHeating;
        String expectedImageUrl;

        TestData(List<String> inputHeating, String inputHeatingText, List<String> expectedHeating, String expectedImageUrl) {
            this.inputHeating = inputHeating;
            this.inputHeatingText = inputHeatingText;
            this.expectedHeating = expectedHeating;
            this.expectedImageUrl = expectedImageUrl;
        }
    }
    @Test
    void createProperty_ShouldHandleNullHeatingText() {
        Property testProperty = new Property();
        testProperty.setImageUrl("mockedImageName.jpg");
        testProperty.setHeating(Arrays.asList("Heating1", "Heating2"));

        PropertyDTO testPropertyDTO = new PropertyDTO();
        testPropertyDTO.setMainImageBytes(new byte[]{1, 2, 3});
        testPropertyDTO.setHeating(Arrays.asList("Heating1", "Heating2"));
        testPropertyDTO.setHeatingText(null);

        when(imageService.uploadImage(any(byte[].class), any(String.class))).thenReturn("mockedImageName.jpg");
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);
        when(modelMapper.map(any(Property.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Property sourceProperty = invocation.getArgument(0);
            PropertyDTO mappedPropertyDTO = new PropertyDTO();
            mappedPropertyDTO.setImageUrl(sourceProperty.getImageUrl());
            mappedPropertyDTO.setHeating(sourceProperty.getHeating());
            return mappedPropertyDTO;
        });

        PropertyDTO result = propertyService.createProperty(testPropertyDTO);

        assertNotNull(result);
        assertEquals("mockedImageName.jpg", result.getImageUrl());
        assertEquals(Arrays.asList("Heating1", "Heating2"), result.getHeating());
        verify(propertyRepository, times(1)).save(any(Property.class));
        verify(imageService, times(1)).uploadImage(any(byte[].class), any(String.class));
    }
    @Test
    void createProperty_ShouldHandleWhitespaceOnlyHeatingText() {
        Property testProperty = new Property();
        testProperty.setImageUrl("mockedImageName.jpg");
        testProperty.setHeating(Arrays.asList("Heating1", "Heating2"));

        PropertyDTO testPropertyDTO = new PropertyDTO();
        testPropertyDTO.setMainImageBytes(new byte[]{1, 2, 3});
        testPropertyDTO.setHeating(Arrays.asList("Heating1", "Heating2"));
        testPropertyDTO.setHeatingText("   ");

        when(imageService.uploadImage(any(byte[].class), any(String.class))).thenReturn("mockedImageName.jpg");
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);
        when(modelMapper.map(any(Property.class), eq(PropertyDTO.class))).thenAnswer(invocation -> {
            Property sourceProperty = invocation.getArgument(0);
            PropertyDTO mappedPropertyDTO = new PropertyDTO();
            mappedPropertyDTO.setImageUrl(sourceProperty.getImageUrl());
            mappedPropertyDTO.setHeating(sourceProperty.getHeating());
            return mappedPropertyDTO;
        });

        PropertyDTO result = propertyService.createProperty(testPropertyDTO);

        assertNotNull(result);
        assertEquals("mockedImageName.jpg", result.getImageUrl());
        assertEquals(Arrays.asList("Heating1", "Heating2"), result.getHeating());
        verify(propertyRepository, times(1)).save(any(Property.class));
        verify(imageService, times(1)).uploadImage(any(byte[].class), any(String.class));
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
        List<String> propertyConditions = List.of("NEW");
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
                any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any()
        )).thenReturn(propertyPage);

        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

        Page<PropertyDTO> result = propertyService.getSearchProperties(
                propertyTypes, propertyTransferType, minBuiltUpArea, maxBuiltUpArea,
                minYardArea, maxYardArea, minRoomsCount, maxRoomsCount,
                minBathroomsCount, maxBathroomsCount, heating, constructionTypes, propertyConditions,
                minConstructionYear, maxConstructionYear, minPrice, maxPrice,
                ownershipTypes, villageName, regionName, pageable
        );

        verify(propertyRepository, times(1)).searchProperties(
                any(), any(), eq(minBuiltUpArea), eq(maxBuiltUpArea),
                eq(minYardArea), eq(maxYardArea), eq(minRoomsCount), eq(maxRoomsCount),
                eq(minBathroomsCount), eq(maxBathroomsCount), eq(heating), any(), any(),
                eq(minConstructionYear.toString()), eq(maxConstructionYear.toString()), eq(minPrice), eq(maxPrice),
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
                any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any()
        )).thenReturn(Page.empty(pageable));

        Page<PropertyDTO> result = propertyService.getSearchProperties(
                propertyTypes, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, minPrice,  null, null, null, null, pageable
        );

        verify(propertyRepository, times(1)).searchProperties(
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(),
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
                any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any()
        )).thenReturn(propertyPage);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(alternativeUserDTO);
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

        Page<PropertyDTO> result = propertyService.getSearchProperties(
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, pageable
        );

        verify(propertyRepository, times(1)).searchProperties(
                any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(),
                any(), eq(pageable)
        );
        assertEquals(1, result.getTotalElements());
        assertEquals(propertyDTO, result.getContent().get(0));
    }
}
