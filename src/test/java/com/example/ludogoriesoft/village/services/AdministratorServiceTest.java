package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.repositories.AdministratorRepository;
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
    private AdministratorRepository administratorRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        administratorRepository = mock(AdministratorRepository.class);
        modelMapper = mock(ModelMapper.class);
        VillageRepository villageRepository = mock(VillageRepository.class);
        administratorService = new AdministratorService(administratorRepository, modelMapper);
    }

    @Test
    void testGetAllAdministrators() {
        Administrator administrator1 = new Administrator();
        administrator1.setId(1L);
        Administrator administrator2 = new Administrator();
        administrator2.setId(2L);
        List<Administrator> administrators = new ArrayList<>();
        administrators.add(administrator1);
        administrators.add(administrator2);

        AdministratorDTO administratorDTO1 = new AdministratorDTO();
        administratorDTO1.setId(1L);
        AdministratorDTO administratorDTO2 = new AdministratorDTO();
        administratorDTO2.setId(2L);
        List<AdministratorDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(administratorDTO1);
        expectedDTOs.add(administratorDTO2);

        when(administratorRepository.findAll()).thenReturn(administrators);
        when(modelMapper.map(any(Administrator.class), eq(AdministratorDTO.class)))
                .thenReturn(administratorDTO1)
                .thenReturn(administratorDTO2);

        List<AdministratorDTO> resultDTOs = administratorService.getAllAdministrators();

        assertEquals(expectedDTOs.size(), resultDTOs.size());
        assertEquals(expectedDTOs.get(0).getId(), resultDTOs.get(0).getId());
        assertEquals(expectedDTOs.get(1).getId(), resultDTOs.get(1).getId());
    }

    @Test
    void testFindAdminByUsername() {
        String username = "admin";
        Administrator administrator = new Administrator();
        administrator.setUsername(username);

        AdministratorDTO expectedAdminDTO = new AdministratorDTO();
        expectedAdminDTO.setUsername(username);

        Mockito.when(administratorRepository.findByUsername(username))
                .thenReturn(administrator);
        Mockito.when(administratorService.administratorToAdministratorDTO(administrator)).thenReturn(expectedAdminDTO);

        AdministratorDTO actualAdminDTO = administratorService.findAdminByUsername(username);

        assertEquals(expectedAdminDTO.getUsername(), actualAdminDTO.getUsername());

        Mockito.verify(administratorRepository, Mockito.times(1)).findByUsername(username);
    }

    @Test
    void testGetAdministratorByIdWhenAdministratorExists() {
        Long id = 1L;
        Administrator administrator = new Administrator();
        administrator.setId(id);

        AdministratorDTO expectedDTO = new AdministratorDTO();
        expectedDTO.setId(id);

        when(administratorRepository.findById(id)).thenReturn(Optional.of(administrator));
        when(modelMapper.map(administrator, AdministratorDTO.class)).thenReturn(expectedDTO);

        AdministratorDTO resultDTO = administratorService.getAdministratorById(id);

        assertEquals(expectedDTO.getId(), resultDTO.getId());
    }

    @Test
    void testGetAdministratorByIdWhenAdministratorDoesNotExist() {
        Long id = 1L;
        when(administratorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> administratorService.getAdministratorById(id));
    }

    @Test
    void testDeleteAdministratorByIdWhenAdministratorExists() {
        Long id = 1L;
        when(administratorRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> administratorService.deleteAdministratorById(id));
    }

    @Test
    void testDeleteAdministratorByIdWhenAdministratorDoesNotExist() {
        Long id = 1L;
        when(administratorRepository.existsById(id)).thenReturn(false);

        assertThrows(ApiRequestException.class, () -> administratorService.deleteAdministratorById(id));
    }

    @Test
    void testUpdateAdministratorWhenAdministratorExists() {
        Long id = 1L;
        AdministratorRequest administratorRequest = new AdministratorRequest();

        Administrator administrator = new Administrator();
        administrator.setId(id);

        AdministratorDTO expectedDTO = new AdministratorDTO();
        expectedDTO.setId(id);

        Optional<Administrator> foundAdministrator = Optional.of(administrator);

        when(administratorRepository.findById(id)).thenReturn(foundAdministrator);
        when(administratorRepository.save(any(Administrator.class))).thenReturn(administrator);
        when(modelMapper.map(administrator, AdministratorDTO.class)).thenReturn(expectedDTO);

        AdministratorDTO resultDTO = administratorService.updateAdministrator(id, administratorRequest);

        verify(administratorRepository, times(1)).findById(id);
        verify(administratorRepository, times(1)).save(any(Administrator.class));
        verify(modelMapper, times(1)).map(administrator, AdministratorDTO.class);
    }

    @Test
    void testUpdateAdministratorWhenAdministratorDoesNotExist() {
        Long id = 1L;
        AdministratorRequest administratorRequest = new AdministratorRequest();
        when(administratorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> administratorService.updateAdministrator(id, administratorRequest));
    }

    @Test
    void testUpdateAdministratorWithoutChangingPassword() {
        Long id = 1L;
        AdministratorRequest administratorRequest = new AdministratorRequest();
        administratorRequest.setFullName("John Doe");
        administratorRequest.setEmail("john.doe@example.com");
        administratorRequest.setUsername("johndoe");

        Administrator administrator = new Administrator();
        administrator.setId(id);
        administrator.setPassword("oldPassword");

        AdministratorDTO expectedDTO = new AdministratorDTO();
        expectedDTO.setId(id);

        Optional<Administrator> foundAdministrator = Optional.of(administrator);

        when(administratorRepository.findById(id)).thenReturn(foundAdministrator);
        when(administratorRepository.save(any(Administrator.class))).thenReturn(administrator);
        when(modelMapper.map(administrator, AdministratorDTO.class)).thenReturn(expectedDTO);

        AdministratorDTO resultDTO = administratorService.updateAdministrator(id, administratorRequest);

        verify(administratorRepository, times(1)).findById(id);
        verify(administratorRepository, times(1)).save(any(Administrator.class));
        verify(modelMapper, times(1)).map(administrator, AdministratorDTO.class);

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

        Administrator administrator = new Administrator();
        administrator.setId(id);
        administrator.setPassword("oldPassword");

        AdministratorDTO expectedDTO = new AdministratorDTO();
        expectedDTO.setId(id);

        Optional<Administrator> foundAdministrator = Optional.of(administrator);

        when(administratorRepository.findById(id)).thenReturn(foundAdministrator);
        when(administratorRepository.save(any(Administrator.class))).thenReturn(administrator);
        when(modelMapper.map(administrator, AdministratorDTO.class)).thenReturn(expectedDTO);

        AdministratorDTO resultDTO = administratorService.updateAdministrator(id, administratorRequest);

        verify(administratorRepository, times(1)).findById(id);
        verify(administratorRepository, times(1)).save(any(Administrator.class));
        verify(modelMapper, times(1)).map(administrator, AdministratorDTO.class);

        assertNotEquals("oldPassword", foundAdministrator.get().getPassword());
    }
}
