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

public class FilterControllerTest {

    @Mock
    private VillageService villageSearchService;

    @InjectMocks
    private FilterController filterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getVillageByName_ShouldReturnVillageDTOs() {
        // Define the test input
        String name = "Village";

        // Create a list of test villages
        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        // Mock the villageSearchService
        when(villageSearchService.getAllSearchVillages(name)).thenReturn(expectedVillages);

        // Call the method under test
        ResponseEntity<List<VillageDTO>> response = filterController.getVillageByName(name);

        // Check the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check the response body
        List<VillageDTO> actualVillages = response.getBody();
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void getVillageByRegion_ShouldReturnVillageDTOs() {
        // Define the test input
        String region = "Region";

        // Create a list of test villages
        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        // Mock the villageSearchService
        when(villageSearchService.getAllSearchVillagesByRegionName(region)).thenReturn(expectedVillages);

        // Call the method under test
        ResponseEntity<List<VillageDTO>> response = filterController.getVillageByRegion(region);

        // Check the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check the response body
        List<VillageDTO> actualVillages = response.getBody();
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void getVillageByNameAndRegion_ShouldReturnVillageDTOs() {
        // Define the test input
        String region = "Region";
        String keyword = "Keyword";

        // Create a list of test villages
        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        // Mock the villageSearchService
        when(villageSearchService.getAllSearchVillagesByNameAndRegionName(region, keyword)).thenReturn(expectedVillages);

        // Call the method under test
        ResponseEntity<List<VillageDTO>> response = filterController.getVillageByNameAndRegion(region, keyword);

        // Check the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check the response body
        List<VillageDTO> actualVillages = response.getBody();
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByCriteria_ShouldReturnVillageDTOs() {
        // Define the test input
        List<String> objectAroundVillageDTOS = new ArrayList<>();
        objectAroundVillageDTOS.add("Object1");
        objectAroundVillageDTOS.add("Object2");

        List<String> livingConditionDTOS = new ArrayList<>();
        livingConditionDTOS.add("Condition1");
        livingConditionDTOS.add("Condition2");

        String children = "BELOW_10";

        // Create a list of test villages
        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        // Mock the villageSearchService
        when(villageSearchService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, Children.BELOW_10)).thenReturn(expectedVillages);

        // Call the method under test
        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByCriteria(objectAroundVillageDTOS, livingConditionDTOS, children);

        // Check the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check the response body
        List<VillageDTO> actualVillages = response.getBody();
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByLivingConditionAndChildren_ShouldReturnVillageDTOs() {
        // Define the test input
        List<String> livingConditionDTOS = new ArrayList<>();
        livingConditionDTOS.add("Condition1");
        livingConditionDTOS.add("Condition2");

        String children = "BELOW_10";

        // Create a list of test villages
        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        // Mock the villageSearchService
        when(villageSearchService.getSearchVillagesByLivingConditionAndChildren(livingConditionDTOS,Children.BELOW_10)).thenReturn(expectedVillages);

        // Call the method under test
        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByLivingConditionAndChildren(livingConditionDTOS, children);

        // Check the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check the response body
        List<VillageDTO> actualVillages = response.getBody();
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByObjectAndChildren_ShouldReturnVillageDTOs() {
        // Define the test input
        List<String> objectAroundVillageDTOS = new ArrayList<>();
        objectAroundVillageDTOS.add("Object1");
        objectAroundVillageDTOS.add("Object2");

        String children = "BELOW_10";

        // Create a list of test villages
        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        // Mock the villageSearchService
        when(villageSearchService.getSearchVillagesByObjectAndChildren(objectAroundVillageDTOS, Children.BELOW_10)).thenReturn(expectedVillages);

        // Call the method under test
        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByObjectAndChildren(objectAroundVillageDTOS, children);

        // Check the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check the response body
        List<VillageDTO> actualVillages = response.getBody();
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByObjectAndLivingCondition_ShouldReturnVillageDTOs() {
        // Define the test input
        List<String> objectAroundVillageDTOS = new ArrayList<>();
        objectAroundVillageDTOS.add("Object1");
        objectAroundVillageDTOS.add("Object2");

        List<String> livingConditionDTOS = new ArrayList<>();
        livingConditionDTOS.add("LivingCondition1");
        livingConditionDTOS.add("LivingCondition2");

        // Create a list of test villages
        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        // Mock the villageSearchService
        when(villageSearchService.getSearchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS)).thenReturn(expectedVillages);

        // Call the method under test
        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS);

        // Check the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check the response body
        List<VillageDTO> actualVillages = response.getBody();
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByChildrenCount_ShouldReturnVillageDTOs() {
        // Define the test input
        String children = "OVER_50";

        // Create a list of test villages
        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        // Mock the villageSearchService
        when(villageSearchService.getSearchVillagesByChildrenCount(Children.OVER_50)).thenReturn(expectedVillages);

        // Call the method under test
        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByChildrenCount(children);

        // Check the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check the response body
        List<VillageDTO> actualVillages = response.getBody();
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByObject_ShouldReturnVillageDTOs() {
        // Define the test input
        List<String> objectAroundVillageDTOS = new ArrayList<>();
        objectAroundVillageDTOS.add("Object1");
        objectAroundVillageDTOS.add("Object2");

        // Create a list of test villages
        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        // Mock the villageSearchService
        when(villageSearchService.getSearchVillagesByObject(objectAroundVillageDTOS)).thenReturn(expectedVillages);

        // Call the method under test
        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByObject(objectAroundVillageDTOS);

        // Check the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check the response body
        List<VillageDTO> actualVillages = response.getBody();
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }

    @Test
    void searchVillagesByLivingCondition_ShouldReturnVillageDTOs() {
        // Define the test input
        List<String> livingConditionDTOS = new ArrayList<>();
        livingConditionDTOS.add("Condition1");
        livingConditionDTOS.add("Condition2");

        // Create a list of test villages
        List<VillageDTO> expectedVillages = new ArrayList<>();
        VillageDTO village1 = new VillageDTO();
        village1.setId(1L);
        village1.setName("Village 1");
        expectedVillages.add(village1);

        VillageDTO village2 = new VillageDTO();
        village2.setId(2L);
        village2.setName("Village 2");
        expectedVillages.add(village2);

        // Mock the villageSearchService
        when(villageSearchService.getSearchVillagesByLivingCondition(livingConditionDTOS)).thenReturn(expectedVillages);

        // Call the method under test
        ResponseEntity<List<VillageDTO>> response = filterController.searchVillagesByLivingCondition(livingConditionDTOS);

        // Check the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check the response body
        List<VillageDTO> actualVillages = response.getBody();
        assertEquals(expectedVillages.size(), actualVillages.size());
        assertEquals(expectedVillages.get(0).getId(), actualVillages.get(0).getId());
        assertEquals(expectedVillages.get(0).getName(), actualVillages.get(0).getName());
        assertEquals(expectedVillages.get(1).getId(), actualVillages.get(1).getId());
        assertEquals(expectedVillages.get(1).getName(), actualVillages.get(1).getName());
    }
}


