package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.repositories.AdministratorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AdministratorServiceTest {
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @InjectMocks
    private AdministratorService administratorService;
    @Mock
    private AdministratorRepository administratorRepository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testGetAllAdministrators() {
        List<Administrator> administrators = new ArrayList<>();
        administrators.add(new Administrator(1L, "John Doe", "john@example.com", "john", "password1", "1234567890", LocalDateTime.now()));
        administrators.add(new Administrator(2L, "Jane Smith", "jane@example.com", "jane", "password2", "0987654321", LocalDateTime.now()));

        List<AdministratorDTO> administratorDTOs = new ArrayList<>();
        administratorDTOs.add(new AdministratorDTO(1L, "John Doe", "john@example.com", "john", "1234567890", LocalDateTime.now()));
        administratorDTOs.add(new AdministratorDTO(2L, "Jane Smith", "jane@example.com", "jane", "0987654321", LocalDateTime.now()));

        when(administratorRepository.findAll()).thenReturn(administrators);
        when(modelMapper.map(administrators.get(0), AdministratorDTO.class)).thenReturn(administratorDTOs.get(0));
        when(modelMapper.map(administrators.get(1), AdministratorDTO.class)).thenReturn(administratorDTOs.get(1));

        List<AdministratorDTO> result = administratorService.getAllAdministrators();

        verify(administratorRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(administrators.get(0), AdministratorDTO.class);
        verify(modelMapper, times(1)).map(administrators.get(1), AdministratorDTO.class);
        Assertions.assertEquals(administratorDTOs, result);
    }
    @Test
    public void testGetAllAdministratorsWithNoAdministrators() {
        List<Administrator> administrators = new ArrayList<>();
        List<AdministratorDTO> administratorDTOs = new ArrayList<>();

        when(administratorRepository.findAll()).thenReturn(administrators);

        List<AdministratorDTO> result = administratorService.getAllAdministrators();

        verify(administratorRepository, times(1)).findAll();
        Assertions.assertEquals(administratorDTOs, result);
    }

    @Test
    public void testGetAdministratorByIdWithExistingAdministratorIdThenReturnsAdministratorDTO() {
        Long administratorId = 123L;
        Administrator existingAdministrator = new Administrator(administratorId, "Test Administrator", "test@example.com", "testadmin", "password", "1234567890", LocalDateTime .now());

        AdministratorDTO expectedAdministratorDTO = new AdministratorDTO(administratorId, "Test Administrator", "test@example.com", "testadmin", "1234567890", LocalDateTime .now());
        expectedAdministratorDTO.setId(administratorId);

        when(administratorRepository.findById(administratorId)).thenReturn(Optional.of(existingAdministrator));
        when(modelMapper.map(existingAdministrator, AdministratorDTO.class)).thenReturn(expectedAdministratorDTO);

        AdministratorDTO result = administratorService.getAdministratorById(administratorId);

        verify(administratorRepository, times(1)).findById(administratorId);
        verify(modelMapper, times(1)).map(existingAdministrator, AdministratorDTO.class);
        Assertions.assertEquals(expectedAdministratorDTO, result);
    }
    @Test
    public void testGetAdministratorByIdWithNonExistingAdministratorIdThenThrowsApiRequestException() {
        Long administratorId = 123L;

        when(administratorRepository.findById(administratorId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> administratorService.getAdministratorById(administratorId));

        verify(administratorRepository, times(1)).findById(administratorId);
        verify(modelMapper, never()).map(any(), eq(AdministratorDTO.class));
    }
    @Test
    public void testDeleteAdministratorByIdWithExistingAdministratorId() {
        Long administratorId = 123L;

        when(administratorRepository.existsById(administratorId)).thenReturn(true);

        administratorService.deleteAdministratorById(administratorId);

        verify(administratorRepository, times(1)).existsById(administratorId);
        verify(administratorRepository, times(1)).deleteById(administratorId);
    }
    @Test
    public void testDeleteAdministratorByIdWithNonExistingAdministratorIdThenThrowsApiRequestException() {
        Long administratorId = 123L;

        when(administratorRepository.existsById(administratorId)).thenReturn(false);

        assertThrows(ApiRequestException.class, () -> administratorService.deleteAdministratorById(administratorId));

        verify(administratorRepository, times(1)).existsById(administratorId);
        verify(administratorRepository, never()).deleteById(administratorId);
    }

    //@Test
    //public void testCreateAdministrator() {
    //    Administrator administrator = new Administrator(123L, "Test Administrator", "test@example.com", "testadmin", "password", "1234567890", LocalDateTime.now());
//
    //    AdministratorDTO expectedAdministratorDTO = new AdministratorDTO(123L, "Test Administrator", "test@example.com", "testadmin", "password", "1234567890", LocalDateTime.now());
//
    //    when(administratorRepository.save(administrator)).thenReturn(administrator);
    //    when(modelMapper.map(administrator, AdministratorDTO.class)).thenReturn(expectedAdministratorDTO);
//
    //    AdministratorDTO result = administratorService.createAdministrator(administrator);
//
    //    verify(administratorRepository, times(1)).save(administrator);
    //    verify(modelMapper, times(1)).map(administrator, AdministratorDTO.class);
    //    Assertions.assertEquals(expectedAdministratorDTO, result);
    //}
    //@Test
    //public void testUpdateAdministratorWithExistingAdministratorId() {
    //    Long administratorId = 123L;
    //    Administrator existingAdministrator = new Administrator(administratorId, "Existing Administrator", "existing@example.com", "existingadmin", "existingpassword", "1234567890", LocalDateTime.now());
    //    Administrator updatedAdministrator = new Administrator(administratorId, "Updated Administrator", "updated@example.com", "updatedadmin", "updatedpassword", "9876543210", LocalDateTime.now());
//
    //    AdministratorDTO expectedAdministratorDTO = new AdministratorDTO(administratorId, "Updated Administrator", "updated@example.com", "updatedadmin", "updatedpassword", "9876543210", LocalDateTime.now());
//
    //    when(administratorRepository.findById(administratorId)).thenReturn(Optional.of(existingAdministrator));
    //    when(administratorRepository.save(existingAdministrator)).thenReturn(existingAdministrator);
    //    when(modelMapper.map(existingAdministrator, AdministratorDTO.class)).thenReturn(expectedAdministratorDTO);
//
    //    AdministratorDTO result = administratorService.updateAdministrator(administratorId, updatedAdministrator);
//
    //    verify(administratorRepository, times(1)).findById(administratorId);
    //    verify(administratorRepository, times(1)).save(existingAdministrator);
    //    verify(modelMapper, times(1)).map(existingAdministrator, AdministratorDTO.class);
    //    Assertions.assertEquals(expectedAdministratorDTO, result);
    //}
//
    //@Test
    //public void testUpdateAdministratorWithNonExistingAdministratorIdThenThrowsApiRequestException() {
    //    Long administratorId = 123L;
//
    //    Administrator updatedAdministrator = new Administrator(administratorId, "Updated Administrator", "updated@example.com", "updatedadmin", "updatedpassword", "9876543210", LocalDateTime.now());
//
    //    when(administratorRepository.findById(administratorId)).thenReturn(Optional.empty());
//
    //    assertThrows(ApiRequestException.class, () -> administratorService.updateAdministrator(administratorId, updatedAdministrator));
//
    //    verify(administratorRepository, times(1)).findById(administratorId);
    //    verify(administratorRepository, never()).save(any());
    //    verify(modelMapper, never()).map(any(), eq(AdministratorDTO.class));
    //}
}
