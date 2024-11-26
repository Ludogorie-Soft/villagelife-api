package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.UserSavedPropertyDTO;
import com.example.ludogorieSoft.village.model.UserSavedProperty;
import com.example.ludogorieSoft.village.repositories.UserSavedPropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserSavedPropertyServiceTest {
    @InjectMocks
    private UserSavedPropertyService userSavedPropertyService;

    @Mock
    private UserSavedPropertyRepository userSavedPropertyRepository;

    @Mock
    private AdministratorService administratorService;

    @Mock
    private PropertyService propertyService;

    private UserSavedProperty userSavedProperty;
    private UserSavedPropertyDTO userSavedPropertyDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userSavedProperty = new UserSavedProperty();
        userSavedPropertyDTO = new UserSavedPropertyDTO();
    }

    @Test
    void testUserSavedPropertyToUserSavedPropertyDTO() {
        when(administratorService.administratorToAdministratorDTO(any())).thenReturn(userSavedPropertyDTO.getUserDTO());
        when(propertyService.propertyToPropertyDTO(any())).thenReturn(userSavedPropertyDTO.getPropertyDTO());

        UserSavedPropertyDTO result = userSavedPropertyService.userSavedPropertyToUserSavedPropertyDTO(userSavedProperty);

        assertNotNull(result);
        verify(administratorService, times(1)).administratorToAdministratorDTO(userSavedProperty.getUser());
        verify(propertyService, times(1)).propertyToPropertyDTO(userSavedProperty.getProperty());
    }

    @Test
    void testCreateUserSavedProperty() {
        when(administratorService.administratorDTOToAdministrator(any())).thenReturn(userSavedProperty.getUser());
        when(propertyService.propertyDTOToProperty(any())).thenReturn(userSavedProperty.getProperty());
        when(userSavedPropertyRepository.save(any())).thenReturn(userSavedProperty);

        UserSavedPropertyDTO result = userSavedPropertyService.createUserSavedProperty(userSavedPropertyDTO);

        assertNotNull(result);
        verify(userSavedPropertyRepository, times(1)).save(any(UserSavedProperty.class));
    }

    @Test
    void testGetAllUserSavedProperties() {
        List<UserSavedProperty> userSavedProperties = List.of(userSavedProperty);
        when(userSavedPropertyRepository.findAll()).thenReturn(userSavedProperties);

        List<UserSavedPropertyDTO> result = userSavedPropertyService.getAllUserSavedProperties();

        assertEquals(1, result.size());
        verify(userSavedPropertyRepository, times(1)).findAll();
    }

    @Test
    void testIsPropertySavedByPropertyIdAndAlternativeUserId() {
        Long propertyId = 1L;
        Long userId = 1L;

        when(userSavedPropertyRepository.existsByPropertyIdAndUserId(propertyId, userId)).thenReturn(true);

        Boolean result = userSavedPropertyService.isPropertySavedByPropertyIdAndAlternativeUserId(propertyId, userId);

        assertTrue(result);
        verify(userSavedPropertyRepository, times(1)).existsByPropertyIdAndUserId(propertyId, userId);
    }

    @Test
    void testToggleUserSavedPropertyWhenNewEntry() {
        Long propertyId = 1L;
        Long userId = 1L;

        when(userSavedPropertyRepository.findByPropertyIdAndUserId(propertyId, userId)).thenReturn(null);
        when(administratorService.administratorDTOToAdministrator(any())).thenReturn(userSavedProperty.getUser());
        when(propertyService.getPropertyById(propertyId)).thenReturn(userSavedProperty.getProperty());
        when(userSavedPropertyRepository.save(any())).thenReturn(userSavedProperty);

        UserSavedPropertyDTO result = userSavedPropertyService.toggleUserSavedProperty(propertyId, userId);

        assertNotNull(result);
        verify(userSavedPropertyRepository, times(1)).save(any(UserSavedProperty.class));
    }

    @Test
    void testToggleUserSavedPropertyWhenUpdateExistingEntry() {
        Long propertyId = 1L;
        Long userId = 1L;
        userSavedProperty.setDeletedAt(null);

        when(userSavedPropertyRepository.findByPropertyIdAndUserId(propertyId, userId)).thenReturn(userSavedProperty);
        when(userSavedPropertyRepository.save(any())).thenReturn(userSavedProperty);

        UserSavedPropertyDTO result = userSavedPropertyService.toggleUserSavedProperty(propertyId, userId);

        assertNotNull(result);
        assertNotNull(userSavedProperty.getDeletedAt());
        verify(userSavedPropertyRepository, times(1)).save(userSavedProperty);
    }
}
