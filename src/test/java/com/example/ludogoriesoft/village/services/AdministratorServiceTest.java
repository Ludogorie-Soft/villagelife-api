package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import com.example.ludogorieSoft.village.repositories.AlternativeUserRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class AdministratorServiceTest {
    private AdministratorService administratorService;
    private AlternativeUserRepository alternativeUserRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        alternativeUserRepository = mock(AlternativeUserRepository.class);
        modelMapper = mock(ModelMapper.class);
        VillageRepository villageRepository = mock(VillageRepository.class);
        administratorService = new AdministratorService(alternativeUserRepository, modelMapper);
    }

    @Test
    void testGetAllAdministrators() {
        AlternativeUser alternativeUser1 = new AlternativeUser();
        alternativeUser1.setId(1L);
        AlternativeUser alternativeUser2 = new AlternativeUser();
        alternativeUser2.setId(2L);
        List<AlternativeUser> alternativeUsers = new ArrayList<>();
        alternativeUsers.add(alternativeUser1);
        alternativeUsers.add(alternativeUser2);

        AlternativeUserDTO alternativeUserDTO1 = new AlternativeUserDTO();
        alternativeUserDTO1.setId(1L);
        AlternativeUserDTO alternativeUserDTO2 = new AlternativeUserDTO();
        alternativeUserDTO2.setId(2L);
        List<AlternativeUserDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(alternativeUserDTO1);
        expectedDTOs.add(alternativeUserDTO2);

        when(alternativeUserRepository.findAll()).thenReturn(alternativeUsers);
        when(modelMapper.map(any(AlternativeUser.class), eq(AlternativeUserDTO.class)))
                .thenReturn(alternativeUserDTO1)
                .thenReturn(alternativeUserDTO2);

        List<AlternativeUserDTO> resultDTOs = administratorService.getAllAdministrators();

        assertEquals(expectedDTOs.size(), resultDTOs.size());
        assertEquals(expectedDTOs.get(0).getId(), resultDTOs.get(0).getId());
        assertEquals(expectedDTOs.get(1).getId(), resultDTOs.get(1).getId());
    }

    @Test
    void testFindAdminByUsername() {
        String username = "admin";
        AlternativeUser alternativeUser = new AlternativeUser();
        alternativeUser.setUsername(username);

        AlternativeUserDTO expectedAdminDTO = new AlternativeUserDTO();
        expectedAdminDTO.setUsername(username);

        Mockito.when(alternativeUserRepository.findByUsername(username))
                .thenReturn(alternativeUser);
        Mockito.when(administratorService.administratorToAdministratorDTO(alternativeUser)).thenReturn(expectedAdminDTO);

        AlternativeUserDTO actualAdminDTO = administratorService.findAdminByUsername(username);

        assertEquals(expectedAdminDTO.getUsername(), actualAdminDTO.getUsername());

        Mockito.verify(alternativeUserRepository, Mockito.times(1)).findByUsername(username);
    }

    @Test
    void testGetAdministratorByIdWhenAdministratorExists() {
        Long id = 1L;
        AlternativeUser alternativeUser = new AlternativeUser();
        alternativeUser.setId(id);

        AlternativeUserDTO expectedDTO = new AlternativeUserDTO();
        expectedDTO.setId(id);

        when(alternativeUserRepository.findById(id)).thenReturn(Optional.of(alternativeUser));
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(expectedDTO);

        AlternativeUserDTO resultDTO = administratorService.getAdministratorById(id);

        assertEquals(expectedDTO.getId(), resultDTO.getId());
    }

    @Test
    void testGetAdministratorByIdWhenAdministratorDoesNotExist() {
        Long id = 1L;
        when(alternativeUserRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> administratorService.getAdministratorById(id));
    }

    @Test
    void testDeleteAdministratorByIdWhenAdministratorExists() {
        Long id = 1L;
        when(alternativeUserRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> administratorService.deleteAdministratorById(id));
    }

    @Test
    void testDeleteAdministratorByIdWhenAdministratorDoesNotExist() {
        Long id = 1L;
        when(alternativeUserRepository.existsById(id)).thenReturn(false);

        assertThrows(ApiRequestException.class, () -> administratorService.deleteAdministratorById(id));
    }

    @Test
    void testUpdateAdministratorWhenAdministratorExists() {
        Long id = 1L;
        AdministratorRequest administratorRequest = new AdministratorRequest();

        AlternativeUser alternativeUser = new AlternativeUser();
        alternativeUser.setId(id);

        AlternativeUserDTO expectedDTO = new AlternativeUserDTO();
        expectedDTO.setId(id);

        Optional<AlternativeUser> foundAdministrator = Optional.of(alternativeUser);

        when(alternativeUserRepository.findById(id)).thenReturn(foundAdministrator);
        when(alternativeUserRepository.save(any(AlternativeUser.class))).thenReturn(alternativeUser);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(expectedDTO);

        AlternativeUserDTO resultDTO = administratorService.updateAdministrator(id, administratorRequest);

        verify(alternativeUserRepository, times(1)).findById(id);
        verify(alternativeUserRepository, times(1)).save(any(AlternativeUser.class));
        verify(modelMapper, times(1)).map(alternativeUser, AlternativeUserDTO.class);
    }

    @Test
    void testUpdateAdministratorWhenAdministratorDoesNotExist() {
        Long id = 1L;
        AdministratorRequest administratorRequest = new AdministratorRequest();
        when(alternativeUserRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> administratorService.updateAdministrator(id, administratorRequest));
    }

    @Test
    void testUpdateAdministratorWithoutChangingPassword() {
        Long id = 1L;
        AdministratorRequest administratorRequest = new AdministratorRequest();
        administratorRequest.setFullName("John Doe");
        administratorRequest.setEmail("john.doe@example.com");
        administratorRequest.setUsername("johndoe");

        AlternativeUser alternativeUser = new AlternativeUser();
        alternativeUser.setId(id);
        alternativeUser.setPassword("oldPassword");

        AlternativeUserDTO expectedDTO = new AlternativeUserDTO();
        expectedDTO.setId(id);

        Optional<AlternativeUser> foundAdministrator = Optional.of(alternativeUser);

        when(alternativeUserRepository.findById(id)).thenReturn(foundAdministrator);
        when(alternativeUserRepository.save(any(AlternativeUser.class))).thenReturn(alternativeUser);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(expectedDTO);

        AlternativeUserDTO resultDTO = administratorService.updateAdministrator(id, administratorRequest);

        verify(alternativeUserRepository, times(1)).findById(id);
        verify(alternativeUserRepository, times(1)).save(any(AlternativeUser.class));
        verify(modelMapper, times(1)).map(alternativeUser, AlternativeUserDTO.class);

        assertEquals("oldPassword", foundAdministrator.get().getPassword());
    }

    @Test
    void testUpdateAdministratorWithNewPassword() {
        Long id = 1L;
        AdministratorRequest administratorRequest = new AdministratorRequest();
        administratorRequest.setFullName("John Doe");
        administratorRequest.setEmail("john.doe@example.com");
        administratorRequest.setUsername("johndoe");
        administratorRequest.setPassword("newPassword");

        AlternativeUser alternativeUser = new AlternativeUser();
        alternativeUser.setId(id);
        alternativeUser.setPassword("oldPassword");

        AlternativeUserDTO expectedDTO = new AlternativeUserDTO();
        expectedDTO.setId(id);

        Optional<AlternativeUser> foundAdministrator = Optional.of(alternativeUser);

        when(alternativeUserRepository.findById(id)).thenReturn(foundAdministrator);
        when(alternativeUserRepository.save(any(AlternativeUser.class))).thenReturn(alternativeUser);
        when(modelMapper.map(alternativeUser, AlternativeUserDTO.class)).thenReturn(expectedDTO);

        AlternativeUserDTO resultDTO = administratorService.updateAdministrator(id, administratorRequest);

        verify(alternativeUserRepository, times(1)).findById(id);
        verify(alternativeUserRepository, times(1)).save(any(AlternativeUser.class));
        verify(modelMapper, times(1)).map(alternativeUser, AlternativeUserDTO.class);

        assertNotEquals("oldPassword", foundAdministrator.get().getPassword());
    }
}
