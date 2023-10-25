package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.dtos.response.VillageResponse;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.VillageRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class VillageServiceTest {
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

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

        VillageService villageService = new VillageService(villageRepository, modelMapper, regionService, authService);

        List<ObjectAroundVillageDTO> result = villageService.convertToObjectAroundVillageDTOList(objectVillages);

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
        Children children = Children.BELOW_10;
        PopulationDTO result = villageService.convertToPopulationDTO(children);
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

        assertEquals(village.getId(), villageDTO.getId());
    }

    @Test
    void testGetAllVillages() {

        List<Village> villages = new ArrayList<>();
        villages.add(new Village());
        villages.add(new Village());
        villages.add(new Village());

        when(villageRepository.findAll()).thenReturn(villages);

        List<VillageDTO> villageDTOs = villageService.getAllVillages();

        assertEquals(villages.size(), villageDTOs.size());

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

        assertEquals(village.getId(), villageDTO.getId());
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
        assertEquals(expectedVillageDTO, result);
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
        assertEquals(existingVillage, result);
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
        village.setStatus(false);

        Village savedVillage = new Village();
        savedVillage.setId(1L);

        when(villageRepository.save(any(Village.class))).thenAnswer(invocation -> {
            Village createdVillage = invocation.getArgument(0);
            createdVillage.setId(1L);
            return createdVillage;
        });

        Long createdVillageId = villageService.createVillageWhitNullValues();

        assertEquals(1L, createdVillageId);
    }


    @Test
    void testVillageToVillageResponse() {
        Village village = new Village();
        village.setId(1L);
        village.setName("Village1");

        VillageResponse villageResponse = new VillageResponse();
        villageResponse.setId(village.getId());
        villageResponse.setName(village.getName());

        when(modelMapper.map(village, VillageResponse.class)).thenReturn(villageResponse);

        VillageResponse result = villageService.villageToVillageResponse(village);

        verify(modelMapper).map(village, VillageResponse.class);

        assertEquals(villageResponse.getId(), result.getId());
        assertEquals(villageResponse.getName(), result.getName());
    }

    @Test
    void testCreateNewVillage() {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("NewVillage");
        villageDTO.setRegion("NewRegion");
        villageDTO.setDateUpload(LocalDateTime.now());
        villageDTO.setStatus(false);

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setId(1L);
        regionDTO.setRegionName("NewRegion");
        when(regionService.findRegionByName("NewRegion")).thenReturn(regionDTO);
        when(regionService.findRegionByName(anyString())).thenReturn(regionDTO);

        Village village = new Village();
        village.setName("NewVillage");
        village.setRegion(new Region(1L, "NewRegion"));
        village.setStatus(false);
        Village savedVillage = new Village();
        savedVillage.setId(1L);
        when(villageRepository.findSingleVillageByNameAndRegionName("NewVillage", "NewRegion")).thenReturn(null);
        when(villageRepository.save(any(Village.class))).thenReturn(savedVillage);
        when(modelMapper.map(savedVillage, VillageDTO.class)).thenReturn(villageDTO);

        VillageDTO result = villageService.createVillage(villageDTO);

        assertEquals("NewVillage", result.getName());
        assertEquals("NewRegion", result.getRegion());
        assertFalse(result.getStatus());
        assertTrue(result.getDateUpload().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(result.getDateUpload().isAfter(LocalDateTime.now().minusSeconds(1)));
    }


    @Test
    void testUpdateExistingVillageStatus() {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setName("ExistingVillage");
        villageDTO.setRegion("ExistingRegion");
        villageDTO.setStatus(true);
        villageDTO.setDateUpload(LocalDateTime.now());

        Village existingVillage = new Village();
        existingVillage.setName("ExistingVillage");
        existingVillage.setRegion(new Region(1L, "ExistingRegion"));
        existingVillage.setStatus(true);
        when(villageRepository.findSingleVillageByNameAndRegionName("ExistingVillage", "ExistingRegion")).thenReturn(existingVillage);
        when(villageRepository.save(any(Village.class))).thenReturn(existingVillage);
        when(modelMapper.map(existingVillage, VillageDTO.class)).thenReturn(villageDTO);

        VillageDTO result = villageService.createVillage(villageDTO);

        assertEquals("ExistingVillage", result.getName());
        assertEquals("ExistingRegion", result.getRegion());
        assertTrue(result.getStatus());
        assertTrue(result.getDateUpload().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(result.getDateUpload().isAfter(LocalDateTime.now().minusSeconds(1)));

    }

    @Test
    void testIncreaseApprovedResponsesCountWhenValidVillageId() {
        Long villageId = 123L;
        Village testVillage = new Village();
        testVillage.setId(villageId);
        testVillage.setApprovedResponsesCount(2);
        Mockito.when(villageRepository.findById(villageId))
                .thenReturn(Optional.of(testVillage));
        villageService.increaseApprovedResponsesCount(villageId);
        Mockito.verify(villageRepository, Mockito.times(1)).save(testVillage);
        assert(testVillage.getApprovedResponsesCount() == 3);
    }
    @Test
    void testIncreaseApprovedResponsesCountWhenInvalidVillageId() {
        Long invalidVillageId = 456L;
        villageService.increaseApprovedResponsesCount(invalidVillageId);
        Mockito.verify(villageRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testGetVillageByNameWhenNotFound() {
        String villageName = "NonExistentVillage";
        when(villageRepository.findByName(villageName)).thenReturn(Collections.emptyList());
        assertThrows(ApiRequestException.class, () -> villageService.getVillageByName(villageName));
    }

    @Test
    void testGetVillageByNameWhenMultipleFound() {
        String villageName = "DuplicateVillage";
        Village village1 = new Village();
        Village village2 = new Village();
        village1.setName(villageName);
        village2.setName(villageName);
        when(villageRepository.findByName(villageName)).thenReturn(List.of(village1, village2));
        assertThrows(ApiRequestException.class, () -> villageService.getVillageByName(villageName));
    }

}
