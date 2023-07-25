package com.example.ludogorieSoft.village.services;


import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.enums.Foreigners;
import com.example.ludogorieSoft.village.enums.NumberOfPopulation;
import com.example.ludogorieSoft.village.enums.Residents;
import com.example.ludogorieSoft.village.model.*;
import com.example.ludogorieSoft.village.repositories.RegionRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VillageServiceTests {


    @Test
    void testGetAllSearchVillages() {
        Village village1 = new Village(1L, "Village1", new Region(1L, "Region1"), 1000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null);
        Village village2 = new Village(2L, "Village2", new Region(2L, "Region2"), 2000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null);
        List<Village> mockVillages = List.of(village1, village2);

        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.findByNameContainingIgnoreCaseOrderByRegionNameAsc("Village")).thenReturn(mockVillages);

        RegionRepository mockRegionRepository = mock(RegionRepository.class);

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), new RegionService(mockRegionRepository, new ModelMapper()));

        List<VillageDTO> result = villageService.getAllSearchVillages("Village");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Village1", result.get(0).getName());
        assertEquals("Village2", result.get(1).getName());
    }


    @Test
    void testGetAllSearchVillagesByRegionName() {
        VillageRepository mockRepository = mock(VillageRepository.class);

        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));
        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        List<Village> mockVillages = Arrays.asList(village1, village2);

        when(mockRepository.findByRegionName("Region1")).thenReturn(mockVillages);

        RegionRepository mockRegionRepository = mock(RegionRepository.class);

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), new RegionService(mockRegionRepository, new ModelMapper()));

        List<VillageDTO> result = villageService.getAllSearchVillagesByRegionName("Region1");

        verify(mockRepository).findByRegionName("Region1");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Village1", result.get(0).getName());
        assertEquals("Village2", result.get(1).getName());
    }

    @Test
    void testGetAllSearchVillagesByNameAndRegionName() {
        VillageRepository mockRepository = mock(VillageRepository.class);

        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));
        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        List<Village> mockVillages = Arrays.asList(village1, village2);

        when(mockRepository.findByNameContainingIgnoreCaseAndRegionName("Region1", "Village1"))
                .thenReturn(mockVillages);

        RegionRepository mockRegionRepository = mock(RegionRepository.class);
        ModelMapper modelMapper = new ModelMapper();

        RegionService regionService = new RegionService(mockRegionRepository, modelMapper);

        VillageService villageService = new VillageService(mockRepository, modelMapper, regionService);

        List<VillageDTO> result = villageService.getAllSearchVillagesByNameAndRegionName("Region1", "Village1");

        verify(mockRepository).findByNameContainingIgnoreCaseAndRegionName("Region1", "Village1");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Village1", result.get(0).getName());
    }


    @Test
    void testGetAllSearchVillagesByRegionNameNoVillagesFound() {
        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.findByRegionName("NonExistentRegion")).thenReturn(Collections.emptyList());

        RegionRepository mockRegionRepository = mock(RegionRepository.class);

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), new RegionService(mockRegionRepository, new ModelMapper()));

        List<VillageDTO> result = villageService.getAllSearchVillagesByRegionName("NonExistentRegion");

        verify(mockRepository).findByRegionName("NonExistentRegion");

        assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllSearchVillagesByNameAndRegionNameNoVillagesFound() {
        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.findByNameContainingIgnoreCaseAndRegionName("Region1", "NonExistentVillage"))
                .thenReturn(Collections.emptyList());

        RegionRepository mockRegionRepository = mock(RegionRepository.class);
        ModelMapper modelMapper = new ModelMapper();

        RegionService regionService = new RegionService(mockRegionRepository, modelMapper);

        VillageService villageService = new VillageService(mockRepository, modelMapper, regionService);

        List<VillageDTO> result = villageService.getAllSearchVillagesByNameAndRegionName("Region1", "NonExistentVillage");

        verify(mockRepository).findByNameContainingIgnoreCaseAndRegionName("Region1", "NonExistentVillage");

        assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    void testGetSearchVillagesWhenNoVillagesFoundWithParamsNoVillagesFound() {
        VillageRepository mockRepository = mock(VillageRepository.class);

        when(mockRepository.searchVillages(anyList(), anyList(), any(Children.class))).thenReturn(Collections.emptyList());

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), new RegionService(mock(RegionRepository.class), new ModelMapper()));

        List<String> objectAroundVillageDTOS = Arrays.asList("object1", "object2");
        List<String> livingConditionDTOS = Arrays.asList("condition1", "condition2");
        Children children = Children.FROM_21_TO_50;

        List<VillageDTO> result = villageService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, children);

        verify(mockRepository).searchVillages(objectAroundVillageDTOS, livingConditionDTOS, children);

        assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetSearchVillagesWhenNoVillagesFoundWithEmptyParams() {
        VillageRepository mockRepository = mock(VillageRepository.class);

        when(mockRepository.searchVillages(anyList(), anyList(), any(Children.class))).thenReturn(Collections.emptyList());

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), new RegionService(null, null));

        List<VillageDTO> result = villageService.getSearchVillages(new ArrayList<>(), new ArrayList<>(), Children.OVER_50);

        verify(mockRepository).searchVillages(anyList(), anyList(), any(Children.class));

        assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    void testGetSearchVillagesByLivingConditionAndChildrenNoVillagesFound() {
        VillageRepository mockRepository = mock(VillageRepository.class);

        when(mockRepository.searchVillagesByLivingConditionAndChildren(anyList(), any(Children.class))).thenReturn(Collections.emptyList());

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), new RegionService(mock(RegionRepository.class), new ModelMapper()));

        List<String> livingConditionDTOS = Arrays.asList("condition1", "condition2");
        Children children = Children.BELOW_10;
        List<VillageDTO> result = villageService.getSearchVillagesByLivingConditionAndChildren(livingConditionDTOS, children);

        verify(mockRepository).searchVillagesByLivingConditionAndChildren(livingConditionDTOS, children.getEnumValue());

        assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    void testGetSearchVillagesByChildrenCount() {
        VillageRepository mockRepository = mock(VillageRepository.class);

        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));
        village1.setPopulation(new Population(1L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.FROM_21_TO_50, Foreigners.I_DONT_KNOW));
        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        village2.setPopulation(new Population(2L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.FROM_21_TO_50, Foreigners.I_DONT_KNOW));
        List<Village> mockVillages = Arrays.asList(village1, village2);


        when(mockRepository.searchVillagesByChildrenCount(
                ArgumentMatchers.any(Children.class)))
                .thenReturn(mockVillages);

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), new RegionService(mock(RegionRepository.class), new ModelMapper()));

        Children children = Children.FROM_21_TO_50;

        List<VillageDTO> result = villageService.getSearchVillagesByChildrenCount(children);

        Mockito.verify(mockRepository).searchVillagesByChildrenCount(
                children.getEnumValue()
        );

        assertEquals(2, result.size());
        assertEquals("Village1", result.get(0).getName());
        assertEquals("Village2", result.get(1).getName());
    }


    @Test
    void testGetAllVillages() {
        VillageRepository mockRepository = mock(VillageRepository.class);

        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));
        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        List<Village> mockVillages = Arrays.asList(village1, village2);

        when(mockRepository.findAll()).thenReturn(mockVillages);

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), new RegionService(mock(RegionRepository.class), new ModelMapper()));

        List<VillageDTO> result = villageService.getAllVillages();

        Mockito.verify(mockRepository).findAll();

        assertEquals(2, result.size());
        assertEquals("Village1", result.get(0).getName());
        assertEquals("Village2", result.get(1).getName());
    }


    @Test
    void testGetVillageByIdWhenFound() {
        Village village = new Village(1L, "Village1", new Region(1L, "Region1"), 1000, new Population(), LocalDateTime.now(), true, new Administrator(), null, null, null);

        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(village));

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), new RegionService(null, null));

        VillageDTO result = villageService.getVillageById(1L);

        verify(mockRepository).findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Village1", result.getName());
        assertEquals("Region1", result.getRegion());
    }


    @Test
    void testConvertToLivingConditionDTOList() {
        List<VillageLivingConditions> villageLivingConditions = new ArrayList<>();
        VillageLivingConditions vl1 = new VillageLivingConditions();
        vl1.setId(1L);
        vl1.setLivingCondition(new LivingCondition(1L, "Condition1"));
        villageLivingConditions.add(vl1);
        VillageLivingConditions vl2 = new VillageLivingConditions();
        vl2.setId(2L);
        vl2.setLivingCondition(new LivingCondition(2L, "Condition2"));
        villageLivingConditions.add(vl2);

        VillageRepository mockRepository = mock(VillageRepository.class);

        RegionRepository mockRegionRepository = mock(RegionRepository.class);

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), new RegionService(mockRegionRepository, new ModelMapper()));
        List<LivingConditionDTO> livingConditionDTOs = villageService.convertToLivingConditionDTOList(villageLivingConditions);

        assertEquals(2, livingConditionDTOs.size());

        LivingConditionDTO dto1 = livingConditionDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Condition1", dto1.getLivingConditionName());

        LivingConditionDTO dto2 = livingConditionDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Condition2", dto2.getLivingConditionName());
    }


    @Test
    void testConvertToPopulationDTO() {
        Children children = Children.OVER_50;

        VillageService villageService = new VillageService(null, null, null);
        PopulationDTO populationDTO = villageService.convertToPopulationDTO(children);

        assertEquals(Children.OVER_50, populationDTO.getChildren());
    }


    @Test
    void testVillageToVillageDTOWithoutObject() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        VillageLivingConditions vl1 = new VillageLivingConditions();
        vl1.setId(1L);
        vl1.setLivingCondition(new LivingCondition(1L, "Condition1"));
        village1.setVillageLivingConditions(List.of(vl1));

        village1.setPopulation(new Population(1L, NumberOfPopulation.FROM_11_TO_50_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.BELOW_10, Foreigners.I_DONT_KNOW));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));

        VillageLivingConditions vl2 = new VillageLivingConditions();
        vl2.setId(2L);
        vl2.setLivingCondition(new LivingCondition(2L, "Condition2"));
        village2.setVillageLivingConditions(List.of(vl2));

        village2.setPopulation(new Population(2L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.UP_TO_2_PERCENT, Children.FROM_21_TO_50, Foreigners.YES));
        villages.add(village2);

        VillageService villageService = new VillageService(null, null, null);
        List<VillageDTO> villageDTOs = villageService.villageToVillageDTOWithoutObject(villages);

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getLivingConditions().size());
        assertEquals(Children.BELOW_10, dto1.getPopulationDTO().getChildren());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getLivingConditions().size());
        assertEquals(Children.FROM_21_TO_50, dto2.getPopulationDTO().getChildren());
    }

    @Test
    void testGetSearchVillagesByLivingConditionAndChildren() {
        List<String> livingConditions = Arrays.asList("Condition1", "Condition2");
        Children children = Children.BELOW_10;

        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        VillageLivingConditions vl1 = new VillageLivingConditions();
        vl1.setId(1L);
        vl1.setLivingCondition(new LivingCondition(1L, "Condition1"));
        village1.setVillageLivingConditions(List.of(vl1));

        village1.setPopulation(new Population(1L, NumberOfPopulation.FROM_11_TO_50_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.BELOW_10, Foreigners.I_DONT_KNOW));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));

        VillageLivingConditions vl2 = new VillageLivingConditions();
        vl2.setId(2L);
        vl2.setLivingCondition(new LivingCondition(2L, "Condition2"));
        village2.setVillageLivingConditions(List.of(vl2));

        village2.setPopulation(new Population(2L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.UP_TO_2_PERCENT, Children.FROM_21_TO_50, Foreigners.YES));
        villages.add(village2);

        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.searchVillagesByLivingConditionAndChildren(livingConditions, children.getEnumValue())).thenReturn(villages);

        VillageService villageService = new VillageService(mockRepository, null, null);
        List<VillageDTO> villageDTOs = villageService.getSearchVillagesByLivingConditionAndChildren(livingConditions, children);

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getLivingConditions().size());
        assertEquals(Children.BELOW_10, dto1.getPopulationDTO().getChildren());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getLivingConditions().size());
        assertEquals(Children.FROM_21_TO_50, dto2.getPopulationDTO().getChildren());
    }


    @Test
    void testConvertToObjectAroundVillageDTOList() {
        List<ObjectVillage> objectVillages = new ArrayList<>();
        ObjectVillage ov1 = new ObjectVillage();
        ov1.setObject(new ObjectAroundVillage(1L, "Type1"));
        objectVillages.add(ov1);
        ObjectVillage ov2 = new ObjectVillage();
        ov2.setObject(new ObjectAroundVillage(2L, "Type2"));
        objectVillages.add(ov2);

        VillageService villageService = new VillageService(null, null, null);
        List<ObjectAroundVillageDTO> objectAroundVillageDTOs = villageService.convertToObjectAroundVillageDTOList(objectVillages);

        assertEquals(2, objectAroundVillageDTOs.size());

        ObjectAroundVillageDTO dto1 = objectAroundVillageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Type1", dto1.getType());

        ObjectAroundVillageDTO dto2 = objectAroundVillageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Type2", dto2.getType());
    }


    @Test
    void testVillageToVillageDTOWithoutLivingCondition() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        ObjectAroundVillage object1 = new ObjectAroundVillage(1L, "Type1");
        ObjectVillage objectVillage1 = new ObjectVillage();
        objectVillage1.setObject(object1);
        village1.setObjectVillages(List.of(objectVillage1));

        village1.setPopulation(new Population(1L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.UP_TO_2_PERCENT, Children.OVER_50, Foreigners.YES));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));

        ObjectAroundVillage object2 = new ObjectAroundVillage(2L, "Type2");
        ObjectVillage objectVillage2 = new ObjectVillage();
        objectVillage2.setObject(object2);
        village2.setObjectVillages(List.of(objectVillage2));

        village2.setPopulation(new Population(2L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.UP_TO_2_PERCENT, Children.FROM_11_TO_20, Foreigners.YES));
        villages.add(village2);

        VillageService villageService = new VillageService(null, null, null);
        List<VillageDTO> villageDTOs = villageService.villageToVillageDTOWithoutLivingCondition(villages);

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());
        assertEquals(1L, dto1.getObject().get(0).getId());
        assertEquals("Type1", dto1.getObject().get(0).getType());
        assertEquals(Children.OVER_50, dto1.getPopulationDTO().getChildren());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getObject().size());
        assertEquals(2L, dto2.getObject().get(0).getId());
        assertEquals("Type2", dto2.getObject().get(0).getType());
        assertEquals(Children.FROM_11_TO_20, dto2.getPopulationDTO().getChildren());
    }


    @Test
    void testGetSearchVillagesByObjectAndChildren() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));
        ObjectAroundVillage object1 = new ObjectAroundVillage(1L, "Type1");
        ObjectVillage objectVillage1 = new ObjectVillage();
        objectVillage1.setObject(object1);
        village1.setObjectVillages(List.of(objectVillage1));
        village1.setPopulation(new Population(1L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.UP_TO_2_PERCENT, Children.OVER_50, Foreigners.YES));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        ObjectAroundVillage object2 = new ObjectAroundVillage(2L, "Type2");
        ObjectVillage objectVillage2 = new ObjectVillage();
        objectVillage2.setObject(object2);
        village2.setObjectVillages(List.of(objectVillage2));
        village2.setPopulation(new Population(2L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.UP_TO_2_PERCENT, Children.FROM_11_TO_20, Foreigners.YES));
        villages.add(village2);

        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.searchVillagesByObjectAndChildren(anyList(), any(Children.class))).thenReturn(villages);

        VillageService villageService = new VillageService(mockRepository, null, null);
        List<VillageDTO> villageDTOs = villageService.getSearchVillagesByObjectAndChildren(Collections.singletonList("Object1"), Children.OVER_50);

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());
        assertEquals(Children.OVER_50, dto1.getPopulationDTO().getChildren());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getObject().size());
        assertEquals(Children.FROM_11_TO_20, dto2.getPopulationDTO().getChildren());
    }


    @Test
    void testVillageToVillageDTOWithoutChildren() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        ObjectAroundVillage object1 = new ObjectAroundVillage(1L, "Type1");
        ObjectVillage objectVillage1 = new ObjectVillage();
        objectVillage1.setObject(object1);
        village1.setObjectVillages(List.of(objectVillage1));

        LivingCondition livingCondition1 = new LivingCondition(1L, "LivingCondition1");
        VillageLivingConditions villageLivingConditions1 = new VillageLivingConditions();
        villageLivingConditions1.setLivingCondition(livingCondition1);
        village1.setVillageLivingConditions(List.of(villageLivingConditions1));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        ObjectAroundVillage object2 = new ObjectAroundVillage(2L, "Type2");
        ObjectVillage objectVillage2 = new ObjectVillage();
        objectVillage2.setObject(object2);
        village2.setObjectVillages(List.of(objectVillage2));

        LivingCondition livingCondition2 = new LivingCondition(2L, "LivingCondition2");
        VillageLivingConditions villageLivingConditions2 = new VillageLivingConditions();
        villageLivingConditions2.setLivingCondition(livingCondition2);
        village2.setVillageLivingConditions(List.of(villageLivingConditions2));
        villages.add(village2);

        VillageService villageService = new VillageService(null, null, null);
        List<VillageDTO> villageDTOs = villageService.villageToVillageDTOWithoutChildren(villages);

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());
        assertEquals(1, dto1.getLivingConditions().size());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getObject().size());
        assertEquals(1, dto2.getLivingConditions().size());
    }


    @Test
    void testGetSearchVillagesByObjectAndLivingCondition() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        ObjectAroundVillage object1 = new ObjectAroundVillage(1L, "Type1");
        ObjectVillage objectVillage1 = new ObjectVillage();
        objectVillage1.setObject(object1);
        village1.setObjectVillages(List.of(objectVillage1));

        LivingCondition livingCondition1 = new LivingCondition(1L, "LivingCondition1");
        VillageLivingConditions villageLivingConditions1 = new VillageLivingConditions();
        villageLivingConditions1.setLivingCondition(livingCondition1);
        village1.setVillageLivingConditions(List.of(villageLivingConditions1));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        ObjectAroundVillage object2 = new ObjectAroundVillage(2L, "Type2");
        ObjectVillage objectVillage2 = new ObjectVillage();
        objectVillage2.setObject(object2);
        village2.setObjectVillages(List.of(objectVillage2));

        LivingCondition livingCondition2 = new LivingCondition(2L, "LivingCondition2");
        VillageLivingConditions villageLivingConditions2 = new VillageLivingConditions();
        villageLivingConditions2.setLivingCondition(livingCondition2);
        village2.setVillageLivingConditions(List.of(villageLivingConditions2));
        villages.add(village2);

        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.searchVillagesByObjectAndLivingCondition(anyList(), anyList())).thenReturn(villages);

        VillageService villageService = new VillageService(mockRepository, null, null);
        List<VillageDTO> villageDTOs = villageService.getSearchVillagesByObjectAndLivingCondition(Collections.singletonList("Object1"), Collections.singletonList("LivingCondition1"));

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());
        assertEquals(1, dto1.getLivingConditions().size());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getObject().size());
        assertEquals(1, dto2.getLivingConditions().size());
    }

    @Test
    void testVillageToVillageDTOChildren() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));
        village1.setPopulation(new Population(1L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.UP_TO_2_PERCENT, Children.OVER_50, Foreigners.YES));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        village2.setPopulation(new Population(2L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.UP_TO_2_PERCENT, Children.FROM_11_TO_20, Foreigners.YES));
        villages.add(village2);

        VillageService villageService = new VillageService(null, null, null);
        List<VillageDTO> villageDTOs = villageService.villageToVillageDTOChildren(villages);

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(Children.OVER_50, dto1.getPopulationDTO().getChildren());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(Children.FROM_11_TO_20, dto2.getPopulationDTO().getChildren());
    }


    @Test
    void testVillageToVillageDTOObject() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        ObjectAroundVillage object1 = new ObjectAroundVillage(1L, "Type1");
        ObjectVillage objectVillage1 = new ObjectVillage();
        objectVillage1.setObject(object1);
        village1.setObjectVillages(List.of(objectVillage1));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        ObjectAroundVillage object2 = new ObjectAroundVillage(2L, "Type2");
        ObjectVillage objectVillage2 = new ObjectVillage();
        objectVillage2.setObject(object2);
        village2.setObjectVillages(List.of(objectVillage2));
        villages.add(village2);

        VillageService villageService = new VillageService(null, null, null);
        List<VillageDTO> villageDTOs = villageService.villageToVillageDTOObject(villages);

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getObject().size());
    }


    @Test
    void testGetSearchVillagesByObject() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        ObjectAroundVillage object1 = new ObjectAroundVillage(1L, "Type1");
        ObjectVillage objectVillage1 = new ObjectVillage();
        objectVillage1.setObject(object1);
        village1.setObjectVillages(List.of(objectVillage1));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        ObjectAroundVillage object2 = new ObjectAroundVillage(2L, "Type2");
        ObjectVillage objectVillage2 = new ObjectVillage();
        objectVillage2.setObject(object2);
        village2.setObjectVillages(List.of(objectVillage2));
        villages.add(village2);

        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.searchVillagesByObject(anyList())).thenReturn(villages);

        VillageService villageService = new VillageService(mockRepository, null, null);
        List<VillageDTO> villageDTOs = villageService.getSearchVillagesByObject(Collections.singletonList("Object1"));

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getObject().size());
    }

    @Test
    void testVillageToVillageDTOLivingCondition() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        LivingCondition livingCondition1 = new LivingCondition(1L, "LivingCondition1");
        VillageLivingConditions villageLivingConditions1 = new VillageLivingConditions();
        villageLivingConditions1.setLivingCondition(livingCondition1);
        village1.setVillageLivingConditions(List.of(villageLivingConditions1));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        LivingCondition livingCondition2 = new LivingCondition(2L, "LivingCondition2");
        VillageLivingConditions villageLivingConditions2 = new VillageLivingConditions();
        villageLivingConditions2.setLivingCondition(livingCondition2);
        village2.setVillageLivingConditions(List.of(villageLivingConditions2));
        villages.add(village2);

        VillageService villageService = new VillageService(null, null, null);
        List<VillageDTO> villageDTOs = villageService.villageToVillageDTOLivingCondition(villages);

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getLivingConditions().size());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getLivingConditions().size());
    }


    @Test
    void testConvertToDTO() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        ObjectAroundVillage object1 = new ObjectAroundVillage(1L, "Type1");
        ObjectVillage objectVillage1 = new ObjectVillage();
        objectVillage1.setObject(object1);
        village1.setObjectVillages(List.of(objectVillage1));

        LivingCondition livingCondition1 = new LivingCondition(1L, "LivingCondition1");
        VillageLivingConditions villageLivingConditions1 = new VillageLivingConditions();
        villageLivingConditions1.setLivingCondition(livingCondition1);
        village1.setVillageLivingConditions(List.of(villageLivingConditions1));

        Children children1 = Children.FROM_11_TO_20;
        Population population1 = new Population();
        population1.setChildren(children1);
        village1.setPopulation(population1);

        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        ObjectAroundVillage object2 = new ObjectAroundVillage(2L, "Type2");
        ObjectVillage objectVillage2 = new ObjectVillage();
        objectVillage2.setObject(object2);
        village2.setObjectVillages(List.of(objectVillage2));

        LivingCondition livingCondition2 = new LivingCondition(2L, "LivingCondition2");
        VillageLivingConditions villageLivingConditions2 = new VillageLivingConditions();
        villageLivingConditions2.setLivingCondition(livingCondition2);
        village2.setVillageLivingConditions(List.of(villageLivingConditions2));

        Children children2 = Children.OVER_50;
        Population population2 = new Population();
        population2.setChildren(children2);
        village2.setPopulation(population2);

        villages.add(village2);

        VillageService villageService = new VillageService(null, null, null);
        List<VillageDTO> villageDTOs = villageService.convertToDTO(villages);

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());
        assertEquals(1, dto1.getLivingConditions().size());
        assertEquals(Children.FROM_11_TO_20.getEnumValue(), dto1.getPopulationDTO().getChildren());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getObject().size());
        assertEquals(1, dto2.getLivingConditions().size());
        assertEquals(Children.OVER_50.getEnumValue(), dto2.getPopulationDTO().getChildren());
    }


    @Test
    void testGetSearchVillagesByLivingCondition() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        LivingCondition livingCondition1 = new LivingCondition(1L, "LivingCondition1");
        VillageLivingConditions villageLivingConditions1 = new VillageLivingConditions();
        villageLivingConditions1.setLivingCondition(livingCondition1);
        village1.setVillageLivingConditions(List.of(villageLivingConditions1));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        village2.setVillageLivingConditions(List.of(villageLivingConditions1));
        villages.add(village2);

        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.searchVillagesByLivingCondition(anyList())).thenReturn(villages);

        VillageService villageService = new VillageService(mockRepository, null, null);
        List<VillageDTO> villageDTOs = villageService.getSearchVillagesByLivingCondition(Collections.singletonList("LivingCondition1"));

        assertEquals(2, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getLivingConditions().size());

        VillageDTO dto2 = villageDTOs.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getLivingConditions().size());
    }


    @Test
    void testGetSearchVillages() {
        List<Village> villages = new ArrayList<>();
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        ObjectAroundVillage object1 = new ObjectAroundVillage(1L, "Type1");
        ObjectVillage objectVillage1 = new ObjectVillage();
        objectVillage1.setObject(object1);
        village1.setObjectVillages(List.of(objectVillage1));

        LivingCondition livingCondition1 = new LivingCondition(1L, "LivingCondition1");
        VillageLivingConditions villageLivingConditions1 = new VillageLivingConditions();
        villageLivingConditions1.setLivingCondition(livingCondition1);
        village1.setVillageLivingConditions(List.of(villageLivingConditions1));

        Children children1 = Children.FROM_21_TO_50;
        Population population1 = new Population();
        population1.setChildren(children1);
        village1.setPopulation(population1);

        villages.add(village1);

        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.searchVillages(anyList(), anyList(), any())).thenReturn(villages);

        VillageService villageService = new VillageService(mockRepository, null, null);
        List<VillageDTO> villageDTOs = villageService.getSearchVillages(
                Collections.singletonList("Type1"),
                Collections.singletonList("LivingCondition1"),
                Children.FROM_21_TO_50
        );

        assertEquals(1, villageDTOs.size());

        VillageDTO dto1 = villageDTOs.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());
        assertEquals(1, dto1.getLivingConditions().size());
        assertEquals(Children.FROM_21_TO_50, dto1.getPopulationDTO().getChildren());
    }

    @Test
    void testGetAllApprovedVillages() {
        VillageRepository mockRepository = mock(VillageRepository.class);

        Village village1 = new Village();
        village1.setName("Село 1");
        village1.setStatus(true);

        Village village2 = new Village();
        village2.setName("Село 2");
        village2.setStatus(false);

        List<Village> villages = new ArrayList<>();
        villages.add(village1);
        villages.add(village2);

        when(mockRepository.findAllApprovedVillages()).thenReturn(villages);

        ModelMapper modelMapper = new ModelMapper();
        RegionRepository mockRegionRepository = mock(RegionRepository.class);

        VillageService villageService = new VillageService(mockRepository, modelMapper, new RegionService(mockRegionRepository, modelMapper));

        List<VillageDTO> approvedVillages = villageService.getAllApprovedVillages();


        assertEquals("Село 1", approvedVillages.get(0).getName());
    }







    @Mock
    private VillageRepository villageRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private VillageService villageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetVillagesByStatus() {
        boolean status = true;

        List<Village> villagesWithStatus = new ArrayList<>();
        villagesWithStatus.add(new Village(1L, "Village1", new Region(), 100, new Population(),
                LocalDateTime.now(), true, new Administrator(), LocalDateTime.now(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        when(villageRepository.findByStatus(status)).thenReturn(villagesWithStatus);

        List<VillageDTO> expectedVillageDTOs = new ArrayList<>();
        expectedVillageDTOs.add(new VillageDTO(1L, "Village1", "region", 100, new PopulationDTO(),
                new Date(), true, new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>()));

        when(modelMapper.map(any(Village.class), eq(VillageDTO.class))).thenReturn(expectedVillageDTOs.get(0));

        List<VillageDTO> result = villageService.getVillagesByStatus(status);

        assertEquals(expectedVillageDTOs, result);
        assertEquals(expectedVillageDTOs.size(), result.size());

        verify(villageRepository, times(1)).findByStatus(status);
        verify(modelMapper, times(1)).map(any(Village.class), eq(VillageDTO.class));
    }
}

