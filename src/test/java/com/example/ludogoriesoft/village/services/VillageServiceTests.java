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

import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VillageServiceTests {
    private static final int pageNumber = 0;
    private static final int elementsCount = 6;
    private static final String sort = "asc";
    @Mock
    private VillageRepository villageRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RegionService regionService;
    @Mock
    private AuthService authService;
    @InjectMocks
    private VillageService villageService;

    @BeforeEach
    void setUp() {
        villageRepository = Mockito.mock(VillageRepository.class);
        modelMapper = Mockito.mock(ModelMapper.class);
        regionService = Mockito.mock(RegionService.class);
        authService = Mockito.mock(AuthService.class);
        villageService = new VillageService(
                villageRepository,
                modelMapper,
                regionService,
                authService
        );
    }

    @Test
    void testGetAllSearchVillages() {
        Village village = new Village();
        village.setName("Village1");

        Village village2 = new Village();
        village.setName("Village2");

        Page<Village> villagePage = new PageImpl<>(List.of(village, village2));

        when(villageRepository.findByNameContainingIgnoreCaseOrderByRegionNameAsc(
                ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(villagePage);

        Page<VillageDTO> resultPage = villageService.getAllSearchVillages("Village", pageNumber, elementsCount, sort);

        assertEquals(2, resultPage.getTotalElements());
        assertEquals(2, resultPage.getContent().size());
    }

    @Test
    void testGetAllSearchVillagesByRegionName() {

        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));
        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));

        List<Village> mockVillages = List.of(village1, village2);

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setName("Village1");
        villageDTO1.setRegion("Region1");

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setName("Village2");
        villageDTO2.setRegion("Region2");

        when(modelMapper.map(village1, VillageDTO.class)).thenReturn(villageDTO1);
        when(modelMapper.map(village2, VillageDTO.class)).thenReturn(villageDTO2);

        Pageable page = PageRequest.of(pageNumber, elementsCount);
        when(villageRepository.findByRegionName("Region1", page)).thenReturn(new PageImpl<>(mockVillages, page, mockVillages.size()));

        Page<VillageDTO> result = villageService.getAllSearchVillagesByRegionName("Region1", pageNumber, elementsCount, sort);

        verify(villageRepository).findByRegionName("Region1", page);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Village1", result.getContent().get(0).getName());
        assertEquals("Village2", result.getContent().get(1).getName());
    }

    @Test
    void testGetAllSearchVillagesByNameAndRegionName() {
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));

        List<Village> mockVillages = List.of(village1, village2);

        Pageable page = PageRequest.of(pageNumber, elementsCount);
        when(villageRepository.findByNameContainingIgnoreCaseAndRegionName("Region1", "Village1", page))
                .thenReturn(new PageImpl<>(mockVillages, page, mockVillages.size()));

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setName("Village1");

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setName("Village2");

        when(modelMapper.map(village1, VillageDTO.class)).thenReturn(villageDTO1);
        when(modelMapper.map(village2, VillageDTO.class)).thenReturn(villageDTO2);

        Page<VillageDTO> result = villageService.getAllSearchVillagesByNameAndRegionName("Region1", "Village1", pageNumber, elementsCount);

        verify(villageRepository).findByNameContainingIgnoreCaseAndRegionName("Region1", "Village1", PageRequest.of(pageNumber, elementsCount));

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Village1", result.getContent().get(0).getName());
        assertEquals("Village2", result.getContent().get(1).getName());
    }

    @Test
    void testGetAllSearchVillagesByRegionNameNoVillagesFound() {
        when(villageRepository.findByRegionName("NonExistentRegion", PageRequest.of(pageNumber, elementsCount))).thenReturn(new PageImpl<>(List.of(), PageRequest.of(pageNumber, elementsCount), 0));

        Page<VillageDTO> result = villageService.getAllSearchVillagesByRegionName("NonExistentRegion", pageNumber, elementsCount, sort);

        verify(villageRepository).findByRegionName("NonExistentRegion", PageRequest.of(pageNumber, elementsCount));

        assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllSearchVillagesByNameAndRegionNameNoVillagesFound() {
        when(villageRepository.findByNameContainingIgnoreCaseAndRegionName("Region1", "NonExistentVillage", PageRequest.of(pageNumber, elementsCount)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(pageNumber, elementsCount), 0));

        Page<VillageDTO> result = villageService.getAllSearchVillagesByNameAndRegionName("Region1", "NonExistentVillage", pageNumber, elementsCount);

        verify(villageRepository).findByNameContainingIgnoreCaseAndRegionName("Region1", "NonExistentVillage", PageRequest.of(pageNumber, elementsCount));

        assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetSearchVillagesWhenNoVillagesFoundWithParamsNoVillagesFound() {
        Page<Village> mockedPage = new PageImpl<>(Collections.emptyList());
        when(villageRepository.searchVillages(anyString(), anyString(), anyList(), anyList(), any(Children.class), any())).thenReturn(mockedPage);

        List<String> objectAroundVillageDTOS = List.of("object1", "object2");
        List<String> livingConditionDTOS = List.of("condition1", "condition2");
        Children children = Children.FROM_21_TO_50;

        Page<VillageDTO> result = villageService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, children, pageNumber, elementsCount, sort);

        verify(villageRepository).searchVillages(eq("region"), eq("name"), eq(objectAroundVillageDTOS), eq(livingConditionDTOS), eq(children), any());

        assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetSearchVillagesWhenNoVillagesFoundWithEmptyParams() {
        Page<Village> mockedPage = new PageImpl<>(Collections.emptyList());
        when(villageRepository.searchVillages(anyString(), anyString(), anyList(), anyList(), any(Children.class), any())).thenReturn(mockedPage);

        Page<VillageDTO> result = villageService.getSearchVillages(List.of(), List.of(), Children.OVER_50, pageNumber, elementsCount, sort);

        verify(villageRepository).searchVillages(anyString(), anyString(), anyList(), anyList(), any(Children.class), any());

        assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetSearchVillagesByLivingConditionAndChildrenNoVillagesFound() {
        Page<Village> mockedPage = new PageImpl<>(Collections.emptyList());
        when(villageRepository.searchVillagesByLivingConditionAndChildren(anyList(), any(Children.class), any())).thenReturn(mockedPage);

        List<String> livingConditionDTOS = List.of("condition1", "condition2");
        Children children = Children.BELOW_10;
        Page<VillageDTO> result = villageService.getSearchVillagesByLivingConditionAndChildren(livingConditionDTOS, children, pageNumber, elementsCount, sort);

        verify(villageRepository).searchVillagesByLivingConditionAndChildren(eq(livingConditionDTOS), eq(children.getEnumValue()), any());

        assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetSearchVillagesByChildrenCount() {
        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));
        village1.setPopulations(List.of(
                new Population(1L, village1, 111, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.FROM_21_TO_50, Foreigners.I_DONT_KNOW,
                        true, LocalDateTime.now(), LocalDateTime.now())
        ));

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village1");
        villageDTO1.setRegion("Region1");
        PopulationDTO populationDTO1 = new PopulationDTO();
        populationDTO1.setChildren(Children.FROM_21_TO_50);
        villageDTO1.setPopulations(Collections.singletonList(populationDTO1));
        Pageable page = PageRequest.of(0, 6);
        when(villageRepository.searchVillagesByChildrenCount(
                ArgumentMatchers.any(Children.class), ArgumentMatchers.eq(page)
        )).thenReturn(new PageImpl<>(List.of(village1), page, 1));
        when(villageService.villageToVillageDTO(village1)).thenReturn(villageDTO1);

        Children children = Children.FROM_21_TO_50;

        Page<VillageDTO> result = villageService.getSearchVillagesByChildrenCount(children, pageNumber, elementsCount, sort);

        Mockito.verify(villageRepository).searchVillagesByChildrenCount(
                children.getEnumValue(), page
        );

        assertEquals(1, result.getTotalElements());

        VillageDTO expectedDTO = result.getContent().get(0);
        assertEquals(villageDTO1.getId(), expectedDTO.getId());
        assertEquals(villageDTO1.getName(), expectedDTO.getName());
        assertEquals(villageDTO1.getRegion(), expectedDTO.getRegion());
    }

    @Test
    void testGetAllVillages() {

        Village village1 = new Village();
        village1.setId(1L);
        village1.setName("Village1");
        village1.setRegion(new Region(1L, "Region1"));
        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        List<Village> mockVillages = List.of(village1, village2);

        when(villageRepository.findAll()).thenReturn(mockVillages);

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setName("Village1");

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setName("Village2");

        when(modelMapper.map(village1, VillageDTO.class)).thenReturn(villageDTO1);
        when(modelMapper.map(village2, VillageDTO.class)).thenReturn(villageDTO2);

        List<VillageDTO> result = villageService.getAllVillages();

        Mockito.verify(villageRepository).findAll();

        assertEquals(2, result.size());
        assertEquals("Village1", result.get(0).getName());
        assertEquals("Village2", result.get(1).getName());
    }

    @Test
    void testGetVillageByIdWhenFound() {
        Village village = new Village();
        village.setId(1L);
        village.setName("Village1");
        village.setRegion(new Region(1L, "Region1"));

        when(villageRepository.findById(1L)).thenReturn(Optional.of(village));

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village1");
        villageDTO1.setRegion("Region1");

        when(modelMapper.map(village, VillageDTO.class)).thenReturn(villageDTO1);

        VillageDTO result = villageService.getVillageById(1L);

        verify(villageRepository).findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Village1", result.getName());
        assertEquals("Region1", result.getRegion());
    }

    @Test
    void testGetSearchVillagesByLivingConditionAndChildren() {
        List<String> livingConditions = List.of("Condition1", "Condition2");
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

        village1.setPopulations(List.of(
                new Population(1L, village1, 111, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.FROM_21_TO_50, Foreigners.I_DONT_KNOW,
                        true, LocalDateTime.now(), LocalDateTime.now())
        ));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));

        VillageLivingConditions vl2 = new VillageLivingConditions();
        vl2.setId(2L);
        vl2.setLivingCondition(new LivingCondition(2L, "Condition2"));
        village2.setVillageLivingConditions(List.of(vl2));

        village2.setPopulations(List.of(
                new Population(1L, village1, 111, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.FROM_21_TO_50, Foreigners.I_DONT_KNOW,
                        true, LocalDateTime.now(), LocalDateTime.now())
        ));
        villages.add(village2);
        Pageable page = PageRequest.of(pageNumber, elementsCount);

        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.searchVillagesByLivingConditionAndChildren(livingConditions, children.getEnumValue(), page)).thenReturn(new PageImpl<>(villages, page, villages.size()));

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), null, null);
        Page<VillageDTO> villageDTOs = villageService.getSearchVillagesByLivingConditionAndChildren(livingConditions, children, pageNumber, elementsCount, sort);

        assertEquals(2, villageDTOs.getTotalElements());

        VillageDTO dto1 = villageDTOs.getContent().get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getLivingConditions().size());
        assertEquals(Children.FROM_21_TO_50, dto1.getPopulations().get(0).getChildren());

        VillageDTO dto2 = villageDTOs.getContent().get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getLivingConditions().size());
        assertEquals(Children.FROM_21_TO_50, dto2.getPopulations().get(0).getChildren());
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
        village1.setPopulations(List.of(
                new Population(1L, village1, 111, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.FROM_21_TO_50, Foreigners.I_DONT_KNOW,
                        true, LocalDateTime.now(), LocalDateTime.now())
        ));
        villages.add(village1);

        Village village2 = new Village();
        village2.setId(2L);
        village2.setName("Village2");
        village2.setRegion(new Region(2L, "Region2"));
        ObjectAroundVillage object2 = new ObjectAroundVillage(2L, "Type2");
        ObjectVillage objectVillage2 = new ObjectVillage();
        objectVillage2.setObject(object2);
        village2.setObjectVillages(List.of(objectVillage2));
        village2.setPopulations(List.of(
                new Population(1L, village2, 111, NumberOfPopulation.UP_TO_10_PEOPLE, Residents.FROM_21_TO_30_PERCENT, Children.FROM_11_TO_20, Foreigners.I_DONT_KNOW,
                        true, LocalDateTime.now(), LocalDateTime.now())
        ));
        villages.add(village2);

        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.searchVillagesByObjectAndChildren(anyList(), any(Children.class), any())).thenReturn(new PageImpl<>(villages, PageRequest.of(pageNumber, elementsCount), villages.size()));

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), null, null);
        Page<VillageDTO> villageDTOs = villageService.getSearchVillagesByObjectAndChildren(Collections.singletonList("Object1"), Children.OVER_50, pageNumber, elementsCount, sort);

        assertEquals(2, villageDTOs.getTotalElements());

        VillageDTO dto1 = villageDTOs.getContent().get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());
        assertEquals(Children.FROM_21_TO_50, dto1.getPopulations().get(0).getChildren());

        VillageDTO dto2 = villageDTOs.getContent().get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getObject().size());
        assertEquals(Children.FROM_11_TO_20, dto2.getPopulations().get(0).getChildren());
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
        when(mockRepository.searchVillagesByObjectAndLivingCondition(anyList(), anyList(), any())).thenReturn(new PageImpl<>(villages));

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), null, null);

        Page<VillageDTO> villageDTOs = villageService.getSearchVillagesByObjectAndLivingCondition(Collections.singletonList("Object1"), Collections.singletonList("LivingCondition1"), pageNumber, elementsCount, sort);

        assertEquals(2, villageDTOs.getTotalElements());

        VillageDTO dto1 = villageDTOs.getContent().get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());
        assertEquals(1, dto1.getLivingConditions().size());

        VillageDTO dto2 = villageDTOs.getContent().get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getObject().size());
        assertEquals(1, dto2.getLivingConditions().size());
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
        when(mockRepository.searchVillagesByObject(anyList(), any())).thenReturn(new PageImpl<>(villages, PageRequest.of(pageNumber, elementsCount), villages.size()));

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), null, null);
        Page<VillageDTO> villageDTOs = villageService.getSearchVillagesByObject(Collections.singletonList("Object1"), pageNumber, elementsCount, sort);

        assertEquals(2, villageDTOs.getTotalElements());

        VillageDTO dto1 = villageDTOs.getContent().get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());

        VillageDTO dto2 = villageDTOs.getContent().get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Village2", dto2.getName());
        assertEquals("Region2", dto2.getRegion());
        assertEquals(1, dto2.getObject().size());
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
        when(mockRepository.searchVillagesByLivingCondition(anyList(), any())).thenReturn(new PageImpl<>(villages, PageRequest.of(pageNumber, elementsCount), villages.size()));

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), null, null);
        Page<VillageDTO> villageDTOs = villageService.getSearchVillagesByLivingCondition(Collections.singletonList("LivingCondition1"), pageNumber, elementsCount, sort);

        assertEquals(2, villageDTOs.getTotalElements());

        VillageDTO dto1 = villageDTOs.getContent().get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getLivingConditions().size());

        VillageDTO dto2 = villageDTOs.getContent().get(1);
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

        village1.setPopulations(new ArrayList<>());
        village1.getPopulations().add(population1);

        villages.add(village1);

        VillageRepository mockRepository = mock(VillageRepository.class);
        when(mockRepository.searchVillages(anyString(), anyString(), anyList(), anyList(), any(), any())).thenReturn(new PageImpl<>(villages, PageRequest.of(pageNumber, elementsCount), villages.size()));

        VillageService villageService = new VillageService(mockRepository, new ModelMapper(), null, null);
        Page<VillageDTO> villageDTOs = villageService.getSearchVillages(
                Collections.singletonList("Type1"),
                Collections.singletonList("LivingCondition1"),
                Children.FROM_21_TO_50, pageNumber, elementsCount, sort
        );

        assertEquals(1, villageDTOs.getTotalElements());

        VillageDTO dto1 = villageDTOs.getContent().get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("Village1", dto1.getName());
        assertEquals("Region1", dto1.getRegion());
        assertEquals(1, dto1.getObject().size());
        assertEquals(1, dto1.getLivingConditions().size());
        assertEquals(Children.FROM_21_TO_50, dto1.getPopulations().get(0).getChildren());
    }

    @Test
    void testCreateVillageWhenVillageExists() {
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

        when(villageRepository.findSingleVillageByNameAndRegionName(villageName, regionName)).thenReturn(existingVillage);
        when(villageRepository.save(existingVillage)).thenReturn(existingVillage);
        when(modelMapper.map(existingVillage, VillageDTO.class)).thenReturn(savedVillageDTO);

        VillageDTO resultDTO = villageService.createVillage(villageDTO);

        verify(villageRepository, times(1)).findSingleVillageByNameAndRegionName(villageName, regionName);
        verify(villageRepository, times(1)).save(any(Village.class));
        verify(modelMapper, times(1)).map(existingVillage, VillageDTO.class);
        verify(regionService, times(0)).findRegionByName(regionName);

        Assertions.assertEquals(savedVillageDTO.getId(), resultDTO.getId());
        Assertions.assertEquals(savedVillageDTO.getName(), resultDTO.getName());
        Assertions.assertNotNull(resultDTO);
    }

    @Test
    void testGetAllApprovedVillages() {

        Village village1 = new Village();
        village1.setName("Village1");
        village1.setStatus(true);

        Village village2 = new Village();
        village2.setName("Village2");
        village2.setStatus(false);

        List<Village> villages = new ArrayList<>();
        villages.add(village1);
        villages.add(village2);

        Page<Village> mockedPage = new PageImpl<>(villages, PageRequest.of(0, 6), villages.size());
        when(villageRepository.findAllApprovedVillages(PageRequest.of(0, 6))).thenReturn(mockedPage);

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setName("Village1");
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setName("Village2");
        villageDTO2.setStatus(false);

        when(modelMapper.map(village1, VillageDTO.class)).thenReturn(villageDTO1);
        when(modelMapper.map(village2, VillageDTO.class)).thenReturn(villageDTO2);

        Page<VillageDTO> approvedVillages = villageService.getAllApprovedVillages(pageNumber, elementsCount, sort);

        assertEquals("Village1", approvedVillages.getContent().get(0).getName());
    }

    @Test
    void testUpdateVillageStatusThrowsException() {
        VillageDTO villageDTO = new VillageDTO();

        when(villageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ApiRequestException.class,
                () -> villageService.updateVillageStatus(1L, villageDTO));

        verify(villageRepository).findById(1L);

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
        assertEquals(expectedVillageDTO, result);
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
}

