package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.enums.Foreigners;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.enums.Residents;
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
    @Mock
    private AuthService authService;


    @Test
    void convertToObjectAroundVillageDTOList_ShouldConvertObjectVillagesToObjectAroundVillageDTOList() {
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

        VillageService villageService = new VillageService(villageRepository, modelMapper, regionService,authService);

        List<ObjectAroundVillageDTO> result = villageService.convertToObjectAroundVillageDTOList(objectVillages);

        Assertions.assertEquals(objectVillages.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            ObjectVillage ov = objectVillages.get(i);
            ObjectAroundVillageDTO dto = result.get(i);
            Assertions.assertEquals(ov.getObject().getId(), dto.getId());
            Assertions.assertEquals(ov.getObject().getType(), dto.getType());
        }
    }


    @Test
    void convertToLivingConditionDTOList_ShouldConvertVillageLivingConditionsToLivingConditionDTOList() {
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

        List<LivingConditionDTO> result = villageService.convertToLivingConditionDTOList(villageLivingConditionsList);

        Assertions.assertEquals(villageLivingConditionsList.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            VillageLivingConditions vl = villageLivingConditionsList.get(i);
            LivingConditionDTO dto = result.get(i);
            Assertions.assertEquals(vl.getLivingCondition().getId(), dto.getId());
            Assertions.assertEquals(vl.getLivingCondition().getLivingConditionName(), dto.getLivingConditionName());
        }
    }


    @Test
    void convertToPopulationDTO_ShouldConvertChildrenToPopulationDTO() {
        Children children = Children.BELOW_10;
        PopulationDTO result = villageService.convertToPopulationDTO(children);
        Assertions.assertEquals(children, result.getChildren());
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
    void testCreateVillageWhenVillageExists() {//first
        String villageName = "Sample Village";
        String regionName = "Sample Region";
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName(villageName);
        villageDTO.setRegion(regionName);

        Village existingVillage = new Village();
        existingVillage.setId(1L);
        existingVillage.setName(villageName);
        existingVillage.setStatus(true);
        existingVillage.setRegion(new Region(1L, regionName));

        VillageDTO savedVillageDTO = new VillageDTO();
        savedVillageDTO.setId(1L);
        savedVillageDTO.setName(villageName);
        savedVillageDTO.setRegion(regionName);
        savedVillageDTO.setPopulationDTO(new PopulationDTO(1L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.BELOW_10, Foreigners.I_DONT_KNOW));

        when(villageRepository.findSingleVillageByNameAndRegionName(villageName, regionName)).thenReturn(existingVillage);
        when(modelMapper.map(villageDTO.getPopulationDTO(), Population.class)).thenReturn(new Population(1L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.BELOW_10, Foreigners.I_DONT_KNOW));
        when(villageRepository.save(existingVillage)).thenReturn(existingVillage);
        when(modelMapper.map(existingVillage, VillageDTO.class)).thenReturn(savedVillageDTO);

        VillageDTO resultDTO = villageService.createVillage(villageDTO);

        verify(villageRepository, times(1)).findSingleVillageByNameAndRegionName(villageName, regionName);
        verify(modelMapper, times(1)).map(villageDTO.getPopulationDTO(), Population.class);
        verify(villageRepository, times(1)).save(any(Village.class));
        verify(modelMapper, times(1)).map(existingVillage, VillageDTO.class);
        verify(regionService, times(0)).findRegionByName(regionName);

        Assertions.assertEquals(savedVillageDTO.getId(), resultDTO.getId());
        Assertions.assertEquals(savedVillageDTO.getName(), resultDTO.getName());
        Assertions.assertEquals(savedVillageDTO.getPopulationDTO(), resultDTO.getPopulationDTO());
        Assertions.assertNotNull(resultDTO);
        Assertions.assertFalse(resultDTO.isStatus());
    }

    @Test
    void testCreateVillageWhenVillageNotExists() {
        String villageName = "Sample Village";
        String regionName = "Sample Region";
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName(villageName);
        villageDTO.setRegion(regionName);
        villageDTO.setPopulationDTO(new PopulationDTO(1L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.BELOW_10, Foreigners.I_DONT_KNOW));

        VillageDTO savedVillageDTO = new VillageDTO();
        savedVillageDTO.setId(1L);
        savedVillageDTO.setName(villageName);
        savedVillageDTO.setRegion(regionName);
        savedVillageDTO.setPopulationDTO(new PopulationDTO(1L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.BELOW_10, Foreigners.I_DONT_KNOW));

        when(villageRepository.findSingleVillageByNameAndRegionName(villageName, regionName)).thenReturn(null);
        when(regionService.findRegionByName(regionName)).thenReturn(new RegionDTO(1L, regionName));
        when(regionService.checkRegion(1L)).thenReturn(new Region(1L, regionName));
        when(modelMapper.map(villageDTO.getPopulationDTO(), Population.class)).thenReturn(new Population(1L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.BELOW_10, Foreigners.I_DONT_KNOW));
        when(villageRepository.save(any(Village.class))).thenAnswer(invocation -> {
            Village villageToSave = invocation.getArgument(0);
            villageToSave.setId(1L);
            return villageToSave;
        });
        when(modelMapper.map(any(Village.class), eq(VillageDTO.class))).thenReturn(savedVillageDTO);

        VillageDTO resultDTO = villageService.createVillage(villageDTO);

        verify(villageRepository, times(1)).findSingleVillageByNameAndRegionName(villageName, regionName);
        verify(modelMapper, times(1)).map(villageDTO.getPopulationDTO(), Population.class);
        verify(villageRepository, times(1)).save(any(Village.class));
        verify(modelMapper, times(1)).map(any(Village.class), eq(VillageDTO.class));
        verify(regionService, times(1)).findRegionByName(regionName);
        verify(regionService, times(1)).checkRegion(1L);

        Assertions.assertEquals(savedVillageDTO.getId(), resultDTO.getId());
        Assertions.assertEquals(savedVillageDTO.getName(), resultDTO.getName());
        Assertions.assertEquals(savedVillageDTO.getPopulationDTO(), resultDTO.getPopulationDTO());
        Assertions.assertNotNull(resultDTO);
        Assertions.assertFalse(resultDTO.isStatus());
    }
    @Test
    void testUpdateVillageWithExistingVillageId() {//me
        Long villageId = 123L;
        Village existingVillage = new Village();
        Region region = new Region(1L, "testRegion");
        RegionDTO regionDTO = new RegionDTO(region.getId(), region.getRegionName());
        existingVillage.setId(villageId);
        existingVillage.setName("Existing Village");
        existingVillage.setStatus(false);

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

        VillageDTO result = villageService.updateVillageStatus(villageId, updatedVillage);

        verify(villageRepository, times(1)).findById(villageId);
        verify(villageRepository, times(1)).save(existingVillage);
        Assertions.assertEquals(expectedVillageDTO, result);
    }


    @Test
    void testUpdateVillageWithApprovedVillage() {//me
        Long id = 1L;
        Region region = new Region(1L, "testRegion");
        RegionDTO regionDTO = new RegionDTO(region.getId(), region.getRegionName());

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("Test Village");
        villageDTO.setRegion(regionDTO.getRegionName());
        villageDTO.setStatus(true);

        Village village = new Village();
        village.setName("Test Village");
        village.setPopulationCount(1000);
        village.setStatus(true); // Set the status as approved
        when(villageRepository.findById(id)).thenReturn(Optional.of(village));
        when(regionService.findRegionByName(region.getRegionName())).thenReturn(regionDTO);
        when(regionService.checkRegion(region.getId())).thenReturn(region);

        AdministratorDTO administratorDTO = new AdministratorDTO();
        administratorDTO.setId(1L);
        administratorDTO.setUsername("admin");
        when(authService.getAdministratorInfo()).thenReturn(administratorDTO);

        assertThrows(ApiRequestException.class, () -> villageService.updateVillageStatus(id, villageDTO));
    }
    @Test
    void testUpdateVillageExceptionCase() {

        Long villageId = 1L;

        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("Updated Village");

        when(villageRepository.findById(villageId)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class, () -> villageService.updateVillageStatus(villageId, villageDTO));
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


    @Test
    void testCreateVillageWhitNullValues() {
        Village village = new Village();
        village.setName("null");
        village.setPopulationCount(0);
        village.setStatus(false);

        Village savedVillage = new Village();
        savedVillage.setId(1L);

        when(villageRepository.save(any(Village.class))).thenAnswer(invocation -> {
            Village createdVillage = invocation.getArgument(0);
            createdVillage.setId(1L);
            return createdVillage;
        });

        Long createdVillageId = villageService.createVillageWhitNullValues();

        Assertions.assertEquals(1L, createdVillageId);
    }


}
