package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.enums.*;
import com.example.ludogorieSoft.village.model.*;

import com.example.ludogorieSoft.village.repositories.RegionRepository;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import com.example.ludogorieSoft.village.services.RegionService;
import com.example.ludogorieSoft.village.services.VillageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
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
    void testGetAllSearchVillagesByRegionName_NoVillagesFound() {
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
    void testGetAllSearchVillagesByNameAndRegionName_NoVillagesFound() {
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
    void testGetSearchVillagesByLivingConditionAndChildren_NoVillagesFound() {
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
        village1.setPopulation(new Population(1L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.FROM_21_TO_50, Foreigners.I_DONT_KNOW)); // �������������� �� ����������� � ������� �������� �� ����
        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        village2.setPopulation(new Population(2L, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.FROM_21_TO_50, Foreigners.I_DONT_KNOW)); // �������������� �� ����������� � ������� �������� �� ����
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





}
