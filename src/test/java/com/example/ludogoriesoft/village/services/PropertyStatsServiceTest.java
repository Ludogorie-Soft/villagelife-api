package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyStatsDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.PropertyStats;
import com.example.ludogorieSoft.village.repositories.PropertyStatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PropertyStatsServiceTest {

    @Mock
    private PropertyStatsRepository propertyStatsRepository;

    @Mock
    private PropertyService propertyService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PropertyStatsService propertyStatsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void decrementSavesForPropertyWhenShouldCallRepositoryWithProperty() {
        Property property = new Property();
        propertyStatsService.decrementSavesForProperty(property);
        verify(propertyStatsRepository, times(1)).decrementSavesForProperty(property);
    }

    @Test
    void decrementSavesForPropertyWhenShouldHandleNullProperty() {
        propertyStatsService.decrementSavesForProperty(null);
        verify(propertyStatsRepository, times(1)).decrementSavesForProperty(null);
    }

    @Test
    void incrementSavesForPropertyWhenShouldCallRepositoryWithProperty() {
        Property property = new Property();
        propertyStatsService.incrementSavesForProperty(property);
        verify(propertyStatsRepository, times(1)).incrementSavesForProperty(property);
    }

    @Test
    void incrementSavesForPropertyWhenShouldHandleNullProperty() {
        propertyStatsService.incrementSavesForProperty(null);
        verify(propertyStatsRepository, times(1)).incrementSavesForProperty(null);
    }

    @Test
    void incrementViewsByPropertyIdWhenShouldIncrementViewsAndReturnDTO() {
        Long propertyId = 1L;
        Property property = new Property();
        property.setId(propertyId);
        PropertyStats propertyStats = new PropertyStats();
        PropertyStatsDTO expectedDto = new PropertyStatsDTO();

        property.setPropertyStats(propertyStats);

        when(propertyService.getPropertyById(propertyId)).thenReturn(property);
        when(modelMapper.map(propertyStats, PropertyStatsDTO.class)).thenReturn(expectedDto);
        PropertyStatsDTO result = propertyStatsService.incrementViewsByPropertyId(propertyId);

        verify(propertyService, times(1)).getPropertyById(propertyId);
        verify(propertyStatsRepository, times(1)).incrementViewsForProperty(property);
        assertEquals(expectedDto, result);
    }

    @Test
    void incrementViewsByPropertyIdWhenShouldThrowExceptionWhenPropertyNotFound() {
        Long propertyId = 1L;
        when(propertyService.getPropertyById(propertyId)).thenThrow(new ApiRequestException("Property not found"));

        Exception exception = assertThrows(ApiRequestException.class, () ->
                propertyStatsService.incrementViewsByPropertyId(propertyId)
        );

        assertEquals("Property not found", exception.getMessage());
        verify(propertyService, times(1)).getPropertyById(propertyId);
        verifyNoInteractions(propertyStatsRepository);
    }

    @Test
    void checkPropertyStatsValidationsWhenShouldPassWithValidStats() {
        PropertyStatsDTO propertyStatsDTO = new PropertyStatsDTO();
        propertyStatsDTO.setSeenInResults(10);
        propertyStatsDTO.setViews(20);
        propertyStatsDTO.setShares(5);
        propertyStatsDTO.setSaves(3);

        assertDoesNotThrow(() -> propertyStatsService.checkPropertyStatsValidations(propertyStatsDTO));
    }

    @Test
    void checkPropertyStatsValidationsWhenShouldThrowExceptionWhenSeenInResultsIsNegative() {
        PropertyStatsDTO propertyStatsDTO = new PropertyStatsDTO();
        propertyStatsDTO.setSeenInResults(-1);
        propertyStatsDTO.setViews(20);
        propertyStatsDTO.setShares(5);
        propertyStatsDTO.setSaves(3);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> propertyStatsService.checkPropertyStatsValidations(propertyStatsDTO));
        assertEquals("Seen in results cannot be less than 0!", exception.getMessage());
    }

    @Test
    void checkPropertyStatsValidationsWhenShouldThrowExceptionWhenViewsAreNegative() {
        PropertyStatsDTO propertyStatsDTO = new PropertyStatsDTO();
        propertyStatsDTO.setSeenInResults(10);
        propertyStatsDTO.setViews(-1);
        propertyStatsDTO.setShares(5);
        propertyStatsDTO.setSaves(3);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> propertyStatsService.checkPropertyStatsValidations(propertyStatsDTO));
        assertEquals("Views cannot be less than 0!", exception.getMessage());
    }

    @Test
    void checkPropertyStatsValidationsWhenShouldThrowExceptionWhenSharesAreNegative() {
        PropertyStatsDTO propertyStatsDTO = new PropertyStatsDTO();
        propertyStatsDTO.setSeenInResults(10);
        propertyStatsDTO.setViews(20);
        propertyStatsDTO.setShares(-1);
        propertyStatsDTO.setSaves(3);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> propertyStatsService.checkPropertyStatsValidations(propertyStatsDTO));
        assertEquals("Shares cannot be less than 0!", exception.getMessage());
    }

    @Test
    void checkPropertyStatsValidationsWhenShouldThrowExceptionWhenSavesAreNegative() {
        PropertyStatsDTO propertyStatsDTO = new PropertyStatsDTO();
        propertyStatsDTO.setSeenInResults(10);
        propertyStatsDTO.setViews(20);
        propertyStatsDTO.setShares(5);
        propertyStatsDTO.setSaves(-1);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> propertyStatsService.checkPropertyStatsValidations(propertyStatsDTO));
        assertEquals("Saves cannot be less than 0!", exception.getMessage());
    }

    @Test
    void createPropertyStatsShouldSaveAndReturnDTOWhenValid() {
        PropertyStatsDTO propertyStatsDTO = new PropertyStatsDTO();
        propertyStatsDTO.setSeenInResults(10);
        propertyStatsDTO.setViews(20);
        propertyStatsDTO.setShares(5);
        propertyStatsDTO.setSaves(3);

        PropertyStats propertyStats = new PropertyStats();
        PropertyStatsDTO expectedDTO = new PropertyStatsDTO();

        when(propertyStatsService.propertyStatsDTOToPropertyStats(propertyStatsDTO)).thenReturn(propertyStats);
        when(propertyStatsRepository.save(propertyStats)).thenReturn(propertyStats);
        when(propertyStatsService.propertyStatsToPropertyStatsDTO(propertyStats)).thenReturn(expectedDTO);

        PropertyStatsDTO result = propertyStatsService.createPropertyStats(propertyStatsDTO);

        verify(propertyStatsRepository, times(1)).save(propertyStats);
        assertEquals(expectedDTO, result);
    }

    @Test
    void createPropertyStatsShouldThrowExceptionWhenValidationFails() {
        PropertyStatsDTO invalidDTO = new PropertyStatsDTO();
        invalidDTO.setSeenInResults(-1);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> propertyStatsService.createPropertyStats(invalidDTO));
        assertEquals("Seen in results cannot be less than 0!", exception.getMessage());

        verifyNoInteractions(propertyStatsRepository);
    }
}
