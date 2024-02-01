package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.VillageRepository;
import com.example.ludogorieSoft.village.services.VillageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = FilterController.class, useDefaultFilters = false, includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = FilterController.class)})
class FilterControllerTest {
    private static final int pageNumber = 0;
    private static final int elementsCount = 6;
    private static final String sort = "asc";
    @MockBean
    private VillageService villageService;
    @InjectMocks
    private FilterController filterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getVillageByNameShouldReturnVillageDTOs() {
        String name = "Village";

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);
        when(villageService.getAllSearchVillages(name, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, PageRequest.of(0, elementsCount), expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> response = filterController.getVillageByName(0, name, sort);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void getVillageByRegionShouldReturnVillageDTOs() {
        String region = "Region";

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        when(villageService.getAllSearchVillagesByRegionName(region, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, PageRequest.of(0, elementsCount), expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> response = filterController.getVillageByRegion(pageNumber, region, sort);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void getVillageByNameAndRegionShouldReturnVillageDTOs() {
        String region = "Region";
        String keyword = "Keyword";

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        when(villageService.getAllSearchVillagesByNameAndRegionName(region, keyword, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, PageRequest.of(0, elementsCount), expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> responseEntity = filterController.getVillageByNameAndRegion(0, region, keyword, sort);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<VillageDTO> actualVillages = responseEntity.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void getVillageByNameAndRegionShouldReturnVillageDTOsDesc() {
        String region = "Region";
        String keyword = "Keyword";
        String sort = "nameDesc";

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        when(villageService.getAllSearchVillagesByNameAndRegionName(region, keyword, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, PageRequest.of(0, elementsCount), expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> responseEntity = filterController.getVillageByNameAndRegion(0, region, keyword, sort);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<VillageDTO> actualVillages = responseEntity.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByCriteriaShouldReturnVillageDTOs() {
        List<String> objectAroundVillageDTOS = new ArrayList<>();
        objectAroundVillageDTOS.add("Object1");
        objectAroundVillageDTOS.add("Object2");

        List<String> livingConditionDTOS = new ArrayList<>();
        livingConditionDTOS.add("Condition1");
        livingConditionDTOS.add("Condition2");

        String children = "BELOW_10";
        String sort = "nameAsc";

        List<VillageDTO> expectedVillagesList = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillagesList.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillagesList.add(village2);

        when(villageService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, Children.BELOW_10, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillagesList, PageRequest.of(0, elementsCount), expectedVillagesList.size()));

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByCriteria(0, objectAroundVillageDTOS, livingConditionDTOS, children, sort);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillagesList, actualVillages);
        assertEquals(expectedVillagesList.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillagesList.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillagesList.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillagesList.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByCriteriaShouldReturnVillageDTOsDesc() {
        List<String> objectAroundVillageDTOS = new ArrayList<>();
        objectAroundVillageDTOS.add("Object1");
        objectAroundVillageDTOS.add("Object2");

        List<String> livingConditionDTOS = new ArrayList<>();
        livingConditionDTOS.add("Condition1");
        livingConditionDTOS.add("Condition2");

        String children = "BELOW_10";


        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        when(villageService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, Children.BELOW_10, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, PageRequest.of(0, elementsCount), expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByCriteria(0, objectAroundVillageDTOS, livingConditionDTOS, children, sort);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByLivingConditionAndChildrenShouldReturnVillageDTOs() {
        List<String> livingConditionDTOS = new ArrayList<>();
        livingConditionDTOS.add("Condition1");
        livingConditionDTOS.add("Condition2");

        String children = "BELOW_10";

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        Pageable page = PageRequest.of(0, 6);
        when(villageService.getSearchVillagesByLivingConditionAndChildren(livingConditionDTOS, Children.BELOW_10, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, page, expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByLivingConditionAndChildren(0, livingConditionDTOS, children, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByObjectAndChildrenShouldReturnVillageDTOs() {
        List<String> objectAroundVillageDTOS = new ArrayList<>();
        objectAroundVillageDTOS.add("Object1");
        objectAroundVillageDTOS.add("Object2");

        String children = "BELOW_10";

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        Pageable page = PageRequest.of(0, 6);
        when(villageService.getSearchVillagesByObjectAndChildren(objectAroundVillageDTOS, Children.BELOW_10, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, page, expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByObjectAndChildren(0, objectAroundVillageDTOS, children, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByObjectAndLivingConditionShouldReturnVillageDTOs() {
        List<String> objectAroundVillageDTOS = new ArrayList<>();
        objectAroundVillageDTOS.add("Object1");
        objectAroundVillageDTOS.add("Object2");

        List<String> livingConditionDTOS = new ArrayList<>();
        livingConditionDTOS.add("LivingCondition1");
        livingConditionDTOS.add("LivingCondition2");

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        Pageable page = PageRequest.of(0, 6);
        when(villageService.getSearchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, page, expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByObjectAndLivingCondition(0, objectAroundVillageDTOS, livingConditionDTOS, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByChildrenCountShouldReturnVillageDTOs() {
        String children = "OVER_50";

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        Pageable page = PageRequest.of(0, 6);
        when(villageService.getSearchVillagesByChildrenCount(Children.OVER_50, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, page, expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByChildrenCount(0, children, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByObjectShouldReturnVillageDTOs() {
        List<String> objectAroundVillageDTOS = new ArrayList<>();
        objectAroundVillageDTOS.add("Object1");
        objectAroundVillageDTOS.add("Object2");

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        Pageable page = PageRequest.of(0, 6);
        when(villageService.getSearchVillagesByObject(objectAroundVillageDTOS, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, page, expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByObject(0, objectAroundVillageDTOS, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByLivingConditionShouldReturnVillageDTOs() {
        List<String> livingConditionDTOS = new ArrayList<>();
        livingConditionDTOS.add("Condition1");
        livingConditionDTOS.add("Condition2");

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        Pageable page = PageRequest.of(0, 6);
        when(villageService.getSearchVillagesByLivingCondition(livingConditionDTOS, pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, page, expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByLivingCondition(0, livingConditionDTOS, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void getAllApprovedVillagesShouldReturnListOfApprovedVillages() {
        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        Pageable page = PageRequest.of(0, elementsCount);
        when(villageService.getAllApprovedVillages(pageNumber, elementsCount, sort)).thenReturn(new PageImpl<>(expectedVillages, page, expectedVillages.size()));

        ResponseEntity<List<VillageDTO>> response = filterController.getAllApprovedVillages(0, sort);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());

        verify(villageService, times(1)).getAllApprovedVillages(0, 6, "asc");
    }
}


