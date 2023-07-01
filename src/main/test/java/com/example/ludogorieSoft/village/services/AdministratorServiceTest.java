package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.request.AdministratorRequest;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.model.Population;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.AdministratorRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AdministratorServiceTest {

    private AdministratorService administratorService;

    private VillageRepository villageRepository;
    private AdministratorRepository administratorRepository;



    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        administratorRepository = mock(AdministratorRepository.class);
        modelMapper = mock(ModelMapper.class);
        villageRepository = mock(VillageRepository.class);
        administratorService = new AdministratorService(administratorRepository, modelMapper, villageRepository );
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
    void testCreateAdministratorWhenUsernameDoesNotExist() {
        AdministratorRequest administratorRequest = new AdministratorRequest();
        administratorRequest.setUsername("admin");
        Administrator administrator = new Administrator();
        administrator.setUsername("admin");
        administrator.setPassword("123123");


        AdministratorDTO expectedDTO = new AdministratorDTO();
        expectedDTO.setUsername("admin");

        when(administratorRepository.existsByUsername(administratorRequest.getUsername())).thenReturn(false);
        when(modelMapper.map(administratorRequest, Administrator.class)).thenReturn(administrator);
        when(administratorRepository.save(administrator)).thenReturn(administrator);
        when(administratorRepository.findByUsername(administrator.getUsername())).thenReturn(administrator);
        when(modelMapper.map(administrator, AdministratorDTO.class)).thenReturn(expectedDTO);

        AdministratorDTO resultDTO = administratorService.createAdministrator(administratorRequest);

        assertEquals(expectedDTO.getUsername(), resultDTO.getUsername());
    }

    @Test
    void testGetAllVillagesWithPopulation() {
        Village village1 = new Village();
        village1.setId(1L);

        Population population1 =  new Population();
        population1.setId(1L);

        Administrator administrator1 = new Administrator();
        administrator1.setId(1L);

        Village village2 = new Village();
        village2.setId(2L);

        Population population2 =  new Population();
        population2.setId(3L);

        Administrator administrator2 = new Administrator();
        administrator2.setId(3L);



        List<Object[]> mockResults = new ArrayList<>();
        Object[] result1 = {village1,population1, administrator1};
        Object[] result2 = {village2, population2, administrator2};
        mockResults.add(result1);
        mockResults.add(result2);
        when(villageRepository.findAllVillagesWithPopulation()).thenReturn(mockResults);

        List<VillageResponse> expectedResponses = new ArrayList<>();
        VillageResponse response1 = new VillageResponse();
        response1.setId(1L);
        response1.setPopulation(population1);
        response1.setAdmin(administrator1);
        expectedResponses.add(response1);

        VillageResponse response2 = new VillageResponse();
        response2.setId(2L);
        response2.setPopulation(population2);
        response2.setAdmin(administrator2);
        expectedResponses.add(response2);

        List<VillageResponse> actualResponses = administratorService.getAllVillagesWithPopulation();

        assertEquals(expectedResponses.size(), actualResponses.size());
        for (int i = 0; i < expectedResponses.size(); i++) {
            VillageResponse expectedResponse = expectedResponses.get(i);
            VillageResponse actualResponse = actualResponses.get(i);
            assertEquals(expectedResponse.getId(), actualResponse.getId());
            assertEquals(expectedResponse.getName(), actualResponse.getName());
            assertEquals(expectedResponse.getPopulation(), actualResponse.getPopulation());
            assertEquals(expectedResponse.getDateUpload(), actualResponse.getDateUpload());
            assertEquals(expectedResponse.getAdmin(), actualResponse.getAdmin());
            assertEquals(expectedResponse.getDateApproved(), actualResponse.getDateApproved());
        }
        verify(villageRepository,times(1)).findAllVillagesWithPopulation();
    }


    @Test
    void testCreateAdministratorWhenUsernameExists() {
        AdministratorRequest administratorRequest = new AdministratorRequest();
        administratorRequest.setUsername("admin");

        when(administratorRepository.existsByUsername(administratorRequest.getUsername())).thenReturn(true);

        assertThrows(ApiRequestException.class, () -> administratorService.createAdministrator(administratorRequest));
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

        AdministratorDTO resultDTO = administratorService.updateAdministrator(id, administratorRequest);

        assertEquals(expectedDTO.getId(), resultDTO.getId());
        assertEquals(expectedDTO.getFullName(), resultDTO.getFullName());
        assertEquals(expectedDTO.getEmail(), resultDTO.getEmail());
        assertEquals(expectedDTO.getUsername(), resultDTO.getUsername());
        assertEquals(expectedDTO.getMobile(), resultDTO.getMobile());
    }

    @Test
    void testUpdateAdministratorWhenAdministratorDoesNotExist() {
        Long id = 1L;
        AdministratorRequest administratorRequest = new AdministratorRequest();
        when(administratorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> administratorService.updateAdministrator(id, administratorRequest));
    }
}
