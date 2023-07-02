package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;


class VillageServiceTest {
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @InjectMocks
    private VillageService villageService;
    @Mock
    private VillageRepository villageRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RegionService regionService;

    @Test
    void getAllSearchVillages_ShouldReturnVillageDTOsMatchingName() {
        // Define the test input
        String name = "Village";

        // Create a list of test villages
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village 1");
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village 2");
        villages.add(village2);

        Village village3 = new Village();
        village3.setId(3L);
        village3.setName("Other Village");
        villages.add(village3);

        // Mock the village repository
        VillageRepository villageRepository = mock(VillageRepository.class);
        when(villageRepository.findByName(name)).thenReturn(villages);


        // Call the method under test
        List<VillageDTO> result = villageService.getAllSearchVillages(name);

        // Check the result
        assertEquals(2, result.size());
        assertEquals(village1.getId(), result.get(0).getId());
        assertEquals(village1.getName(), result.get(0).getName());
        assertEquals(village2.getId(), result.get(1).getId());
        assertEquals(village2.getName(), result.get(1).getName());

        // Verify that the village repository was called
        verify(villageRepository, times(1)).findByName(name);
    }



    @Test
    void convertToObjectAroundVillageDTOList_ShouldConvertObjectVillagesToObjectAroundVillageDTOList() {
        // Create a list of test objects
        ObjectAroundVillage objectAroundVillage = new ObjectAroundVillage(1L, "TYPE");
        List<ObjectVillage> objectVillages = new ArrayList<>();
        ObjectVillage ov1 = new ObjectVillage();
        ov1.setId(1L);
        ov1.setObject(objectAroundVillage);
        objectVillages.add(ov1);

        ObjectVillage ov2 = new ObjectVillage();
        ov2.setId(2L);
        ov2.setObject(objectAroundVillage);
        objectVillages.add(ov2);

        // Create an instance of the tested class
        VillageService villageService = new VillageService(villageRepository,modelMapper,regionService);

        // Call the method under test
        List<ObjectAroundVillageDTO> result = villageService.convertToObjectAroundVillageDTOList(objectVillages);

        // Check the result
        assertEquals(objectVillages.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            ObjectVillage ov = objectVillages.get(i);
            ObjectAroundVillageDTO dto = result.get(i);
            assertEquals(ov.getObject().getId(), dto.getId());
            assertEquals(ov.getObject().getType(), dto.getType());
        }
    }



    @Test
    void convertToLivingConditionDTOList_ShouldConvertVillageLivingConditionsToLivingConditionDTOList() {
        // Create a list of test objects
        List<VillageLivingConditions> villageLivingConditionsList = new ArrayList<>();
        VillageLivingConditions vl1 = new VillageLivingConditions();
        vl1.setId(1L);
        LivingCondition lc1 = new LivingCondition();
        lc1.setId(1L);
        lc1.setLivingConditionName("Condition 1");
        vl1.setLivingCondition(lc1);
        villageLivingConditionsList.add(vl1);

        VillageLivingConditions vl2 = new VillageLivingConditions();
        vl2.setId(2L);
        LivingCondition lc2 = new LivingCondition();
        lc2.setId(2L);
        lc2.setLivingConditionName("Condition 2");
        vl2.setLivingCondition(lc2);
        villageLivingConditionsList.add(vl2);


        // Call the method under test
        List<LivingConditionDTO> result = villageService.convertToLivingConditionDTOList(villageLivingConditionsList);

        // Check the result
        assertEquals(villageLivingConditionsList.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            VillageLivingConditions vl = villageLivingConditionsList.get(i);
            LivingConditionDTO dto = result.get(i);
            assertEquals(vl.getLivingCondition().getId(), dto.getId());
            assertEquals(vl.getLivingCondition().getLivingConditionName(), dto.getLivingConditionName());
        }
    }


    @Test
    void convertToPopulationDTO_ShouldConvertChildrenToPopulationDTO() {
        // Create a test object
        Children children = Children.BELOW_10;


        // Call the method under test
        PopulationDTO result = villageService.convertToPopulationDTO(children);

        // Check the result
        assertEquals(children, result.getChildren());
    }





    @Test
    void testVillageToVillageDTO() {

        VillageDTO expectedVillageDTO = new VillageDTO();
        expectedVillageDTO.setId(1L);

        Village village = new Village();
        village.setId(1L);

        when(modelMapper.map(village, VillageDTO.class)).thenReturn(expectedVillageDTO);

        VillageDTO villageDTO = villageService.villageToVillageDTO(village);

        Assertions.assertEquals(village.getId(), villageDTO.getId());
    }

    @Test
    void testGetAllVillages() {

        List<Village> villages = new ArrayList<>();
        villages.add(new Village());
        villages.add(new Village());
        villages.add(new Village());

        when(villageRepository.findAll()).thenReturn(villages);

        List<VillageDTO> villageDTOs = villageService.getAllVillages();

        Assertions.assertEquals(villages.size(), villageDTOs.size());

        verify(villageRepository, times(1)).findAll();
    }

    @Test
    void testGetVillageByIdWhenExist() {

        Long villageId = 1L;

        VillageDTO expectedVillageDTO = new VillageDTO();
        expectedVillageDTO.setId(1L);

        Village village = new Village();
        village.setId(1L);

        when(modelMapper.map(village, VillageDTO.class)).thenReturn(expectedVillageDTO);

        when(villageRepository.findById(villageId)).thenReturn(Optional.of(village));

        VillageDTO villageDTO = villageService.getVillageById(villageId);

        Assertions.assertEquals(village.getId(), villageDTO.getId());
        verify(villageRepository, times(1)).findById(villageId);


    }

    @Test
    void testGetVillageByIdWhenNotExist() {

        Long villageId = 1L;

        when(villageRepository.findById(villageId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageService.getVillageById(villageId));
        verify(villageRepository, times(1)).findById(villageId);
    }

    @Test
    void createVillageValidVillageDTOReturnsCreatedVillageDTO() {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("Test Village");
        Region region = new Region(1L, "testRegion");
        RegionDTO regionDTO = new RegionDTO(region.getId(), region.getRegionName());
        PopulationDTO populationDTO = new PopulationDTO();
        populationDTO.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);
        villageDTO.setPopulationDTO(populationDTO);
        villageDTO.setRegion(region.getRegionName());

        Village village = new Village();
        village.setName("Test Village");
        Population population = new Population();
        population.setNumberOfPopulation(NumberOfPopulation.UP_TO_10_PEOPLE);
        village.setPopulation(population);
        village.setRegion(region);

        Village savedVillage = new Village();
        savedVillage.setId(1L);
        when(modelMapper.map(villageDTO.getPopulationDTO(), Population.class)).thenReturn(population);
        when(villageRepository.save(any(Village.class))).thenReturn(savedVillage);
        when(modelMapper.map(village, VillageDTO.class)).thenReturn(villageDTO);
        when(regionService.findRegionByName(region.getRegionName())).thenReturn(regionDTO);
        when(regionService.checkRegion(region.getId())).thenReturn(region);
        VillageDTO result = villageService.createVillage(villageDTO);

        verify(modelMapper, times(1)).map(villageDTO.getPopulationDTO(), Population.class);
        verify(villageRepository, times(1)).save(any(Village.class));
        verify(modelMapper, times(1)).map(village, VillageDTO.class);
        Assertions.assertEquals(savedVillage.getId(), result.getId());
        Assertions.assertEquals(villageDTO.getName(), result.getName());
        Assertions.assertEquals(villageDTO.getPopulationDTO(), result.getPopulationDTO());
    }

    @Test
    void testUpdateVillageWithExistingVillageId() {
        Long villageId = 123L;
        Village existingVillage = new Village();
        Region region = new Region(1L, "testRegion");
        RegionDTO regionDTO = new RegionDTO(region.getId(), region.getRegionName());
        existingVillage.setId(villageId);
        existingVillage.setName("Existing Village");


        VillageDTO updatedVillage = new VillageDTO();
        updatedVillage.setName("Updated Village");
        updatedVillage.setRegion(region.getRegionName());

        VillageDTO expectedVillageDTO = new VillageDTO();
        expectedVillageDTO.setId(villageId);
        expectedVillageDTO.setName("Updated Village");
        updatedVillage.setRegion(region.getRegionName());

        when(villageRepository.findById(villageId)).thenReturn(Optional.of(existingVillage));
        when(modelMapper.map(existingVillage, VillageDTO.class)).thenReturn(expectedVillageDTO);
        when(regionService.findRegionByName(region.getRegionName())).thenReturn(regionDTO);
        when(regionService.checkRegion(region.getId())).thenReturn(region);

        VillageDTO result = villageService.updateVillage(villageId, updatedVillage);

        verify(villageRepository, times(1)).findById(villageId);
        verify(villageRepository, times(1)).save(existingVillage);
        Assertions.assertEquals(expectedVillageDTO, result);
    }

    @Test
    void testUpdateVillageExceptionCase() {

        Long villageId = 1L;

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("Updated Village");

        when(villageRepository.findById(villageId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageService.updateVillage(villageId, villageDTO));
        verify(villageRepository, times(1)).findById(villageId);
        verify(villageRepository, never()).save(any(Village.class));
    }

    @Test
    void testDeleteVillagePositiveCase() {

        Long villageId = 1L;

        when(villageRepository.existsById(villageId)).thenReturn(true);

        villageService.deleteVillage(villageId);

        verify(villageRepository, times(1)).deleteById(villageId);
    }

    @Test
    void testDeleteVillageExceptionCase() {

        Long villageId = 1L;

        when(villageRepository.existsById(villageId)).thenReturn(false);

        assertThrows(ApiRequestException.class, () -> villageService.deleteVillage(villageId));
    }

    @Test
    void checkVillageShouldReturnExistingVillage() {
        Long villageId = 1L;
        Village existingVillage = new Village();
        existingVillage.setId(villageId);
        Optional<Village> optionalVillage = Optional.of(existingVillage);

        when(villageRepository.findById(villageId)).thenReturn(optionalVillage);

        Village result = villageService.checkVillage(villageId);

        verify(villageRepository, times(1)).findById(villageId);
        Assertions.assertEquals(existingVillage, result);
    }

    @Test
    void checkVillageShouldThrowExceptionWhenVillageNotFound() {
        Long villageId = 1L;
        Optional<Village> optionalVillage = Optional.empty();

        when(villageRepository.findById(villageId)).thenReturn(optionalVillage);

        Assertions.assertThrows(ApiRequestException.class, () -> villageService.checkVillage(villageId));

        verify(villageRepository, times(1)).findById(villageId);
    }
}
