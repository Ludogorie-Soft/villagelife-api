package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.services.VillageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


class FilterControllerTest {

    @Mock
    private VillageService villageSearchService;

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

        when(villageSearchService.getAllSearchVillages(name, 0, 6)).thenReturn(expectedVillages);

        ResponseEntity<List<VillageDTO>> response = filterController.getVillageByName( 0, name);

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

        when(villageSearchService.getAllSearchVillagesByRegionName(region, 0, 6)).thenReturn(expectedVillages);

        ResponseEntity<List<VillageDTO>> response = filterController.getVillageByRegion(0, region);

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
        String sort = "nameAsc";

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        when(villageSearchService.getAllSearchVillagesByNameAndRegionName(region, keyword, 0, 6)).thenReturn(expectedVillages);

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

        when(villageSearchService.getAllSearchVillagesByNameAndRegionName(region, keyword, 0, 6)).thenReturn(expectedVillages);

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

        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        when(villageSearchService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, Children.BELOW_10)).thenReturn(expectedVillages);

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByCriteria(objectAroundVillageDTOS, livingConditionDTOS, children, sort);

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
    void searchVillagesByCriteriaShouldReturnVillageDTOsDesc() {
        List<String> objectAroundVillageDTOS = new ArrayList<>();
        objectAroundVillageDTOS.add("Object1");
        objectAroundVillageDTOS.add("Object2");

        List<String> livingConditionDTOS = new ArrayList<>();
        livingConditionDTOS.add("Condition1");
        livingConditionDTOS.add("Condition2");

        String children = "BELOW_10";
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

        when(villageSearchService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, Children.BELOW_10)).thenReturn(expectedVillages);

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByCriteria(objectAroundVillageDTOS, livingConditionDTOS, children, sort);

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

        when(villageSearchService.getSearchVillagesByLivingConditionAndChildren(livingConditionDTOS,Children.BELOW_10)).thenReturn(expectedVillages);

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByLivingConditionAndChildren(livingConditionDTOS, children);

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

        when(villageSearchService.getSearchVillagesByObjectAndChildren(objectAroundVillageDTOS, Children.BELOW_10)).thenReturn(expectedVillages);

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByObjectAndChildren(objectAroundVillageDTOS, children);

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

        when(villageSearchService.getSearchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS)).thenReturn(expectedVillages);

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS);

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

        when(villageSearchService.getSearchVillagesByChildrenCount(Children.OVER_50)).thenReturn(expectedVillages);

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByChildrenCount(children);

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

        when(villageSearchService.getSearchVillagesByObject(objectAroundVillageDTOS)).thenReturn(expectedVillages);

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByObject(objectAroundVillageDTOS);

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

        when(villageSearchService.getSearchVillagesByLivingCondition(livingConditionDTOS)).thenReturn(expectedVillages);

        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByLivingCondition(livingConditionDTOS);

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

        when(villageSearchService.getAllApprovedVillages(0, 6)).thenReturn(expectedVillages);

        ResponseEntity<List<VillageDTO>> response = filterController.getAllApprovedVillages(0);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<VillageDTO> actualVillages = response.getBody();
        assert actualVillages != null;
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());

        verify(villageSearchService, times(1)).getAllApprovedVillages(0, 6);
    }


}


