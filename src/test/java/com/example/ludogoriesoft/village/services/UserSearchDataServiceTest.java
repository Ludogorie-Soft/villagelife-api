package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.dtos.UserSearchDataDTO;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.UserSearchData;
import com.example.ludogorieSoft.village.repositories.RegionRepository;
import com.example.ludogorieSoft.village.repositories.UserSearchDataRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserSearchDataServiceTest {
    @InjectMocks
    UserSearchDataService userSearchDataService;
    @Mock
    UserSearchDataRepository userSearchDataRepository;
    @Mock
    ModelMapper modelMapper;
    @Mock
    AuthService authService;
    @Mock
    VillageRepository villageRepository;
    @Mock
    RegionRepository regionRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserSearchDataDTOToUserSearchData() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setId(1L);
        userSearchDataDTO.setSearchName("search");
        UserSearchData userSearchData = new UserSearchData();
        userSearchData.setId(1L);
        userSearchData.setSearchName("search");
        when(modelMapper.map(userSearchDataDTO, UserSearchData.class)).thenReturn(userSearchData);
        UserSearchData result = userSearchDataService.userSearchDataDTOToUserSearchData(userSearchDataDTO);
        Assertions.assertEquals(result.getId(), userSearchDataDTO.getId());
        verify(modelMapper, times(1)).map(userSearchDataDTO, UserSearchData.class);
    }

    @Test
    void testUserSearchDataToUserSearchDataDTO() {
        UserSearchData userSearchData = new UserSearchData();
        userSearchData.setId(1L);
        userSearchData.setSearchName("search");
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setId(1L);
        userSearchDataDTO.setSearchName("search");
        when(modelMapper.map(userSearchData, UserSearchDataDTO.class)).thenReturn(userSearchDataDTO);
        UserSearchDataDTO resultDTO = userSearchDataService.userSearchDataToUserSearchDataDTO(userSearchData);
        Assertions.assertEquals(resultDTO.getId(), userSearchData.getId());
        verify(modelMapper, times(1)).map(userSearchData, UserSearchDataDTO.class);
    }

    @Test
    void testCreateUserSearchData() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setSearchName("search");
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setId(1L);
        userSearchDataDTO.setAlternativeUserDTO(alternativeUserDTO);

        when(authService.getAdministratorInfo()).thenReturn(alternativeUserDTO);
        when(userSearchDataRepository.save(any(UserSearchData.class))).thenReturn(new UserSearchData());
        when(modelMapper.map(userSearchDataDTO, UserSearchData.class)).thenReturn(new UserSearchData());
        when(modelMapper.map(any(UserSearchData.class), eq(UserSearchDataDTO.class))).thenReturn(userSearchDataDTO);

        UserSearchDataDTO result = userSearchDataService.createUserSearchData(userSearchDataDTO);

        assertNotNull(result);
        assertEquals(alternativeUserDTO.getId(), result.getAlternativeUserDTO().getId());
        verify(userSearchDataRepository, times(1)).save(any(UserSearchData.class));
    }

    @Test
    void testGetAllUserSearchDataDTOsForUser_withValidRole() {
        Long userId = 1L;
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setId(userId);
        alternativeUserDTO.setRole(Role.ADMIN);

        when(authService.getAdministratorInfo()).thenReturn(alternativeUserDTO);
        when(userSearchDataRepository.findAllByAlternativeUserId(userId)).thenReturn(new ArrayList<>());

        List<UserSearchDataDTO> result = userSearchDataService.getAllUserSearchDataDTOsForUser(userId);
        assertNotNull(result);
        verify(userSearchDataRepository, times(1)).findAllByAlternativeUserId(userId);
    }

    @Test
    void testGetAllUserSearchDataDTOsForUser_equalIdAndUserId() {
        Long id = 1L;
        Long userId = 1L;
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setId(userId);
        alternativeUserDTO.setRole(Role.ADMIN);

        when(authService.getAdministratorInfo()).thenReturn(alternativeUserDTO);
        when(userSearchDataRepository.findAllByAlternativeUserId(userId)).thenReturn(new ArrayList<>());

        List<UserSearchDataDTO> result = userSearchDataService.getAllUserSearchDataDTOsForUser(id);
        assertNotNull(result);
        verify(userSearchDataRepository, times(1)).findAllByAlternativeUserId(id);
    }

    @Test
    void testGetAllUserSearchDataDTOsForUser_nullId() {
        Long id = null;
        Long userId = 1L;
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setId(userId);
        alternativeUserDTO.setRole(Role.USER);
        when(authService.getAdministratorInfo()).thenReturn(alternativeUserDTO);
        when(userSearchDataRepository.findAllByAlternativeUserId(userId)).thenReturn(new ArrayList<>());
        List<UserSearchDataDTO> result = userSearchDataService.getAllUserSearchDataDTOsForUser(id);
        assertNotNull(result);
        verify(userSearchDataRepository, times(1)).findAllByAlternativeUserId(userId);
    }

    @Test
    void testGetAllUserSearchDataDTOsForUser_notNullIdAndNotAdmin() {
        Long id = 2L;
        Long userId = 1L;
        AlternativeUserDTO alternativeUserDTO = new AlternativeUserDTO();
        alternativeUserDTO.setId(userId);
        alternativeUserDTO.setRole(Role.USER);
        when(authService.getAdministratorInfo()).thenReturn(alternativeUserDTO);

        when(userSearchDataRepository.findAllByAlternativeUserId(userId)).thenReturn(new ArrayList<>());
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSearchDataService.getAllUserSearchDataDTOsForUser(id));
        assertEquals("You can not access others search info!", exception.getMessage());
        verify(userSearchDataRepository, never()).findAllByAlternativeUserId(userId);
    }

    @Test
    void testCheckUserSearchDataValidations_invalidSearchName() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setSearchName("");

        Exception exception = assertThrows(ApiRequestException.class,
                () -> userSearchDataService.createUserSearchData(userSearchDataDTO));
        assertEquals("Search name cannot be null or blank!", exception.getMessage());
    }

    @Test
    void testCreateUserSearchData_invalidSearchName_blank() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setSearchName("");

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSearchDataService.createUserSearchData(userSearchDataDTO));

        assertEquals("Search name cannot be null or blank!", exception.getMessage());
    }

    @Test
    void testCreateUserSearchData_invalidSearchName_tooLong() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setSearchName("This is a search name that exceeds fifty characters!");

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSearchDataService.createUserSearchData(userSearchDataDTO));

        assertEquals("Search name can not be more than 50 signs long!", exception.getMessage());
    }

    @Test
    void testCreateUserSearchData_invalidBuiltUpArea() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setSearchName("search");
        userSearchDataDTO.setMinBuiltUpArea(100.0);
        userSearchDataDTO.setMaxBuiltUpArea(50.0);
        AlternativeUserDTO mockAlternativeUser = new AlternativeUserDTO();
        mockAlternativeUser.setId(1L);
        when(authService.getAdministratorInfo()).thenReturn(mockAlternativeUser);
        userSearchDataDTO.setAlternativeUserDTO(mockAlternativeUser);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSearchDataService.createUserSearchData(userSearchDataDTO));

        assertEquals("Minimal built up area can not be more than maximal built up area!", exception.getMessage());
    }

    @Test
    void testCreateUserSearchData_invalidYardArea() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setSearchName("search");
        userSearchDataDTO.setMinYardArea(100.0);
        userSearchDataDTO.setMaxYardArea(50.0);
        AlternativeUserDTO mockAlternativeUser = new AlternativeUserDTO();
        mockAlternativeUser.setId(1L);
        when(authService.getAdministratorInfo()).thenReturn(mockAlternativeUser);
        userSearchDataDTO.setAlternativeUserDTO(mockAlternativeUser);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSearchDataService.createUserSearchData(userSearchDataDTO));

        assertEquals("Minimal yard area can not be more than maximal yard area!", exception.getMessage());
    }

    @Test
    void testCreateUserSearchData_invalidRoomsCount() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setSearchName("search");
        userSearchDataDTO.setMinRoomsCount((short)5);
        userSearchDataDTO.setMaxRoomsCount((short)3);
        AlternativeUserDTO mockAlternativeUser = new AlternativeUserDTO();
        mockAlternativeUser.setId(1L);
        when(authService.getAdministratorInfo()).thenReturn(mockAlternativeUser);
        userSearchDataDTO.setAlternativeUserDTO(mockAlternativeUser);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSearchDataService.createUserSearchData(userSearchDataDTO));

        assertEquals("Minimal room count can not be more than maximal room count!", exception.getMessage());
    }

    @Test
    void testCreateUserSearchData_invalidBathroomsCount() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setSearchName("search");
        userSearchDataDTO.setMinBathroomsCount((short)3);
        userSearchDataDTO.setMaxBathroomsCount((short)1);
        AlternativeUserDTO mockAlternativeUser = new AlternativeUserDTO();
        mockAlternativeUser.setId(1L);
        when(authService.getAdministratorInfo()).thenReturn(mockAlternativeUser);
        userSearchDataDTO.setAlternativeUserDTO(mockAlternativeUser);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSearchDataService.createUserSearchData(userSearchDataDTO));

        assertEquals("Minimal bathroom count can not be more than maximal bathroom count!", exception.getMessage());
    }

    @Test
    void testCreateUserSearchData_invalidConstructionYear() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setSearchName("search");
        userSearchDataDTO.setMinConstructionYear((short)2000);
        userSearchDataDTO.setMaxConstructionYear((short)1990);
        AlternativeUserDTO mockAlternativeUser = new AlternativeUserDTO();
        mockAlternativeUser.setId(1L);
        when(authService.getAdministratorInfo()).thenReturn(mockAlternativeUser);
        userSearchDataDTO.setAlternativeUserDTO(mockAlternativeUser);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSearchDataService.createUserSearchData(userSearchDataDTO));

        assertEquals("Minimal construction year can not be more than maximal construction year!", exception.getMessage());
    }

    @Test
    void testCreateUserSearchData_invalidPrice() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setSearchName("search");
        userSearchDataDTO.setMinPrice(BigDecimal.valueOf(100000));
        userSearchDataDTO.setMaxPrice(BigDecimal.valueOf(50000));
        AlternativeUserDTO mockAlternativeUser = new AlternativeUserDTO();
        mockAlternativeUser.setId(1L);
        when(authService.getAdministratorInfo()).thenReturn(mockAlternativeUser);
        userSearchDataDTO.setAlternativeUserDTO(mockAlternativeUser);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSearchDataService.createUserSearchData(userSearchDataDTO));

        assertEquals("Minimal price can not be more than maximal price!", exception.getMessage());
    }

    // Test for invalid Region (non-existent)
    @Test
    void testCreateUserSearchData_invalidRegion() {
        UserSearchDataDTO userSearchDataDTO = new UserSearchDataDTO();
        userSearchDataDTO.setSearchName("search");
        userSearchDataDTO.setRegionName("NonExistingRegion");
        AlternativeUserDTO mockAlternativeUser = new AlternativeUserDTO();
        mockAlternativeUser.setId(1L);
        when(authService.getAdministratorInfo()).thenReturn(mockAlternativeUser);
        userSearchDataDTO.setAlternativeUserDTO(mockAlternativeUser);

        when(regionRepository.findByRegionName(any())).thenReturn(null);

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userSearchDataService.createUserSearchData(userSearchDataDTO));

        assertEquals("Region not found!", exception.getMessage());
    }
}
