package com.example.ludogorieSoft.village.services_tests;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.AdministratorRequest;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.repositories.AdministratorRepository;
import com.example.ludogorieSoft.village.services.AdministratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdministratorServiceTest {

    private AdministratorService administratorService;
    private AdministratorRepository administratorRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        administratorRepository = mock(AdministratorRepository.class);
        modelMapper = mock(ModelMapper.class);
        administratorService = new AdministratorService(administratorRepository, modelMapper);
    }

    @Test
    public void testGetAllAdministrators() {
        // Arrange
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

        // Act
        List<AdministratorDTO> resultDTOs = administratorService.getAllAdministrators();

        // Assert
        assertEquals(expectedDTOs.size(), resultDTOs.size());
        assertEquals(expectedDTOs.get(0).getId(), resultDTOs.get(0).getId());
        assertEquals(expectedDTOs.get(1).getId(), resultDTOs.get(1).getId());
    }

    @Test
    public void testCreateAdministrator_WhenUsernameDoesNotExist() {
        // Arrange
        AdministratorRequest administratorRequest = new AdministratorRequest();
        administratorRequest.setUsername("admin");
        Administrator administrator = new Administrator();
        administrator.setUsername("admin");

        AdministratorDTO expectedDTO = new AdministratorDTO();
        expectedDTO.setUsername("admin");

        when(administratorRepository.existsByUsername(administratorRequest.getUsername())).thenReturn(false);
        when(modelMapper.map(administratorRequest, Administrator.class)).thenReturn(administrator);
        when(administratorRepository.save(administrator)).thenReturn(administrator);
        when(administratorRepository.findByUsername(administrator.getUsername())).thenReturn(administrator);
        when(modelMapper.map(administrator, AdministratorDTO.class)).thenReturn(expectedDTO);

        // Act
        AdministratorDTO resultDTO = administratorService.createAdministrator(administratorRequest);

        // Assert
        assertEquals(expectedDTO.getUsername(), resultDTO.getUsername());
    }

    @Test
    public void testCreateAdministrator_WhenUsernameExists() {
        // Arrange
        AdministratorRequest administratorRequest = new AdministratorRequest();
        administratorRequest.setUsername("admin");

        when(administratorRepository.existsByUsername(administratorRequest.getUsername())).thenReturn(true);

        // Act & Assert
        assertThrows(ApiRequestException.class, () -> administratorService.createAdministrator(administratorRequest));
    }

    @Test
    public void testGetAdministratorById_WhenAdministratorExists() {
        // Arrange
        Long id = 1L;
        Administrator administrator = new Administrator();
        administrator.setId(id);

        AdministratorDTO expectedDTO = new AdministratorDTO();
        expectedDTO.setId(id);

        when(administratorRepository.findById(id)).thenReturn(Optional.of(administrator));
        when(modelMapper.map(administrator, AdministratorDTO.class)).thenReturn(expectedDTO);

        // Act
        AdministratorDTO resultDTO = administratorService.getAdministratorById(id);

        // Assert
        assertEquals(expectedDTO.getId(), resultDTO.getId());
    }

    @Test
    public void testGetAdministratorById_WhenAdministratorDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(administratorRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ApiRequestException.class, () -> administratorService.getAdministratorById(id));
    }

    @Test
    public void testDeleteAdministratorById_WhenAdministratorExists() {
        // Arrange
        Long id = 1L;
        when(administratorRepository.existsById(id)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> administratorService.deleteAdministratorById(id));
    }

    @Test
    public void testDeleteAdministratorById_WhenAdministratorDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(administratorRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(ApiRequestException.class, () -> administratorService.deleteAdministratorById(id));
    }

    @Test
    public void testUpdateAdministrator_WhenAdministratorExists() {
        // Arrange
        Long id = 1L;
        AdministratorRequest administratorRequest = new AdministratorRequest();
        administratorRequest.setFullName("John Doe");
        administratorRequest.setEmail("john@example.com");
        administratorRequest.setUsername("john");
        administratorRequest.setPassword("password");
        administratorRequest.setMobile("123456789");

        Administrator administrator = new Administrator();
        administrator.setId(id);

        AdministratorDTO expectedDTO = new AdministratorDTO();
        expectedDTO.setId(id);
        expectedDTO.setFullName(administratorRequest.getFullName());
        expectedDTO.setEmail(administratorRequest.getEmail());
        expectedDTO.setUsername(administratorRequest.getUsername());
        expectedDTO.setMobile(administratorRequest.getMobile());

        Optional<Administrator> foundAdministrator = Optional.of(administrator);

        when(administratorRepository.findById(id)).thenReturn(foundAdministrator);
        when(administratorRepository.save(foundAdministrator.get())).thenReturn(foundAdministrator.get());
        when(modelMapper.map(foundAdministrator.get(), AdministratorDTO.class)).thenReturn(expectedDTO);

        // Act
        AdministratorDTO resultDTO = administratorService.updateAdministrator(id, administratorRequest);

        // Assert
        assertEquals(expectedDTO.getId(), resultDTO.getId());
        assertEquals(expectedDTO.getFullName(), resultDTO.getFullName());
        assertEquals(expectedDTO.getEmail(), resultDTO.getEmail());
        assertEquals(expectedDTO.getUsername(), resultDTO.getUsername());
        assertEquals(expectedDTO.getMobile(), resultDTO.getMobile());
    }

    @Test
    public void testUpdateAdministrator_WhenAdministratorDoesNotExist() {
        // Arrange
        Long id = 1L;
        AdministratorRequest administratorRequest = new AdministratorRequest();
        when(administratorRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ApiRequestException.class, () -> administratorService.updateAdministrator(id, administratorRequest));
    }
}
