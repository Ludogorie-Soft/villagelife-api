package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.exeptions.handler.ApiExceptionHandler;
import com.example.ludogorieSoft.village.services.VillageService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = FilterController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = FilterController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = ApiExceptionHandler.class)
        }
)
class FilterControllerIntegrationTest {
    private static final int pageNumber = 0;
    private static final int elementsCount = 6;
    private static final String sort = "asc";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VillageService villageSearchService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllApprovedVillages() throws Exception {

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setDateUpload(LocalDateTime.now());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setDateUpload(LocalDateTime.now());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = List.of(villageDTO1, villageDTO2);
        when(villageSearchService.getAllApprovedVillages(pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(villageDTOList, PageRequest.of(pageNumber, elementsCount), villageDTOList.size()));

        mockMvc.perform(get("/api/v1/filter/{page}", pageNumber)
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(villageDTOList.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"));
    }

    @Test
    void testGetVillageByName() throws Exception {
        String name = "SomeVillageName";

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setDateUpload(LocalDateTime.now());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setDateUpload(LocalDateTime.now());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);
        when(villageSearchService.getAllSearchVillages(name, pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(villageDTOList, PageRequest.of(pageNumber, elementsCount), villageDTOList.size()));

        mockMvc.perform(get("/api/v1/filter/byName/{page}", pageNumber)
                        .param("name", name)
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(villageDTOList.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"));
    }

    @Test
    void testGetVillageByRegion() throws Exception {
        String region = "SomeRegionName";

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setDateUpload(LocalDateTime.now());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setDateUpload(LocalDateTime.now());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);
        when(villageSearchService.getAllSearchVillagesByRegionName(region, pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(villageDTOList, PageRequest.of(pageNumber, elementsCount), villageDTOList.size()));

        mockMvc.perform(get("/api/v1/filter/byRegion/{page}", pageNumber)
                        .param("region", region)
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(villageDTOList.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"));
    }

    @Test
    void testGetVillageByNameAndRegion() throws Exception {
        String region = "Example Region";
        String keyword = "Example Keyword";

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setDateUpload(LocalDateTime.now());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setDateUpload(LocalDateTime.now());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);
        when(villageSearchService.getAllSearchVillagesByNameAndRegionName(region, keyword, pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(villageDTOList, PageRequest.of(pageNumber, elementsCount), villageDTOList.size()));

        mockMvc.perform(get("/api/v1/filter/searchAll/{page}", pageNumber)
                        .param("region", region)
                        .param("keyword", keyword)
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(villageDTOList.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"));
    }

    @Test
    void testSearchVillagesByCriteria() throws Exception {
        List<String> objectAroundVillageDTOS = Arrays.asList("Object1", "Object2");
        List<String> livingConditionDTOS = Arrays.asList("Condition1", "Condition2");
        String children = "FROM_21_TO_50";

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setDateUpload(LocalDateTime.now());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setDateUpload(LocalDateTime.now());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);
        when(villageSearchService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, Children.valueOf(children), pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(villageDTOList, PageRequest.of(pageNumber, elementsCount), villageDTOList.size()));

        mockMvc.perform(get("/api/v1/filter/searchVillages/{page}", pageNumber)
                        .param("objectAroundVillageDTOS", "Object1", "Object2")
                        .param("livingConditionDTOS", "Condition1", "Condition2")
                        .param("children", children)
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(villageDTOList.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"));
    }

    @Test
    void testSearchVillagesByLivingConditionAndChildren() throws Exception {
        List<String> livingConditionDTOS = Arrays.asList("Condition1", "Condition2");
        String children = Children.FROM_21_TO_50.name();

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setDateUpload(LocalDateTime.now());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setDateUpload(LocalDateTime.now());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);
        when(villageSearchService.getSearchVillagesByLivingConditionAndChildren(livingConditionDTOS, Children.valueOf(children), pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(villageDTOList, PageRequest.of(pageNumber, elementsCount), villageDTOList.size()));

        mockMvc.perform(get("/api/v1/filter/searchVillagesByLivingConditionAndChildren/{page}", pageNumber)
                        .param("livingConditionDTOS", "Condition1", "Condition2")
                        .param("children", children)
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(villageDTOList.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"));
    }


    @Test
    void testSearchVillagesByObjectAndChildren() throws Exception {
        List<String> objectAroundVillageDTOS = Arrays.asList("Object1", "Object2");
        String children = Children.FROM_21_TO_50.name();

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setDateUpload(LocalDateTime.now());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setDateUpload(LocalDateTime.now());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);

        when(villageSearchService.getSearchVillagesByObjectAndChildren(objectAroundVillageDTOS, Children.valueOf(children), pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(villageDTOList, PageRequest.of(pageNumber, elementsCount), villageDTOList.size()));

        mockMvc.perform(get("/api/v1/filter/searchVillagesByObjectAndChildren/{page}", pageNumber)
                        .param("objectAroundVillageDTOS", "Object1", "Object2")
                        .param("children", children)
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(villageDTOList.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"));
    }

    @Test
    void testSearchVillagesByObjectAndLivingCondition() throws Exception {
        List<String> objectAroundVillageDTOS = Arrays.asList("Object1", "Object2");
        List<String> livingConditionDTOS = Arrays.asList("Condition1", "Condition2");

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setDateUpload(LocalDateTime.now());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setDateUpload(LocalDateTime.now());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);

        when(villageSearchService.getSearchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS, pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(villageDTOList, PageRequest.of(pageNumber, elementsCount), villageDTOList.size()));

        mockMvc.perform(get("/api/v1/filter/searchVillagesByObjectAndLivingCondition/{page}", pageNumber)
                        .param("objectAroundVillageDTOS", "Object1", "Object2")
                        .param("livingConditionDTOS", "Condition1", "Condition2")
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(villageDTOList.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"));
    }

    @Test
    void testSearchVillagesByChildrenCount() throws Exception {
        String children = "FROM_21_TO_50";

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setDateUpload(LocalDateTime.now());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setDateUpload(LocalDateTime.now());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);
        when(villageSearchService.getSearchVillagesByChildrenCount(Children.valueOf(children), pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(villageDTOList, PageRequest.of(pageNumber, elementsCount), villageDTOList.size()));

        mockMvc.perform(get("/api/v1/filter/searchVillagesByChildrenCount/{page}", pageNumber)
                        .param("children", children)
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(villageDTOList.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"));
    }

    @Test
    void testSearchVillagesByObject() throws Exception {
        List<String> objectAroundVillageDTOS = Arrays.asList("Object1", "Object2");

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setDateUpload(LocalDateTime.now());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setDateUpload(LocalDateTime.now());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);
        when(villageSearchService.getSearchVillagesByObject(objectAroundVillageDTOS, pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(villageDTOList, PageRequest.of(pageNumber, elementsCount), villageDTOList.size()));

        mockMvc.perform(get("/api/v1/filter/searchVillagesByObject/{page}", pageNumber)
                        .param("objectAroundVillageDTOS", "Object1", "Object2")
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(villageDTOList.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"));
    }

    @Test
    void testSearchVillagesByLivingCondition() throws Exception {
        List<String> livingConditionDTOS = Arrays.asList("Condition1", "Condition2");

        VillageDTO villageDTO1 = new VillageDTO();
        villageDTO1.setId(1L);
        villageDTO1.setName("Village Name 1");
        villageDTO1.setDateUpload(LocalDateTime.now());
        villageDTO1.setStatus(true);

        VillageDTO villageDTO2 = new VillageDTO();
        villageDTO2.setId(2L);
        villageDTO2.setName("Village Name 2");
        villageDTO2.setDateUpload(LocalDateTime.now());
        villageDTO2.setStatus(true);

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);
        when(villageSearchService.getSearchVillagesByLivingCondition(livingConditionDTOS, pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(villageDTOList, PageRequest.of(pageNumber, elementsCount), villageDTOList.size()));

        mockMvc.perform(get("/api/v1/filter/searchVillagesByLivingCondition/{page}", pageNumber)
                        .param("livingConditionDTOS", "Condition1", "Condition2")
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(villageDTOList.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"));
    }

    @Test
    void testGetAllApprovedVillagesWhenEmpty() throws Exception {
        when(villageSearchService.getAllApprovedVillages(pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(pageNumber, elementsCount), 0));

        mockMvc.perform(get("/api/v1/filter/{page}", pageNumber)
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetVillageByNameWhenNoMatch() throws Exception {
        String name = "Nonexistent Village";

        when(villageSearchService.getAllSearchVillages(name, pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(pageNumber, elementsCount), 0));

        mockMvc.perform(get("/api/v1/filter/byName/{page}", pageNumber)
                        .param("name", name)
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetVillageByRegionRegionNotFound() throws Exception {
        String region = "Nonexistent Region";
        when(villageSearchService.getAllSearchVillagesByRegionName(region, pageNumber, elementsCount, sort))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(pageNumber, elementsCount), 0));
        mockMvc.perform(get("/api/v1/filter/byRegion/{page}", pageNumber)
                        .param("region", region)
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }
/*
    @Test
    void testGetAllApprovedVillagesElementsCount() throws Exception {
        int page = 0;
        long expectedCount = 10L;

        when(villageSearchService.getAllApprovedVillages(page, 6, ""))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(page, 6), expectedCount));

        mockMvc.perform(get("/api/v1/filter/{page}/elementsCount", page)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedCount));
    }
    @Test
    void testGetVillageByNameElementsCount() throws Exception {
        int page = 0;
        String name = "YourVillageName";
        long expectedCount = 10L;

        when(villageSearchService.getAllSearchVillages(name, page, 6, ""))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(page, 6), expectedCount));

        mockMvc.perform(get("/api/v1/filter/byName/{page}/elementsCount", page)
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedCount));
    }

    @Test
    void testGetVillageByRegionElementsCount() throws Exception {
        int page = 0;
        String region = "YourRegionName";
        long expectedCount = 10L;

        when(villageSearchService.getAllSearchVillagesByRegionName(region, page, 6, ""))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(page, 6), expectedCount));

        mockMvc.perform(get("/api/v1/filter/byRegion/{page}/elementsCount", page)
                        .param("region", region)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedCount));
    }
    @Test
    void testGetVillageByNameAndRegionElementsCount() throws Exception {
        int page = 0;
        String region = "YourRegionName";
        String keyword = "YourKeyword";
        long expectedCount = 10L;

        when(villageSearchService.getAllSearchVillagesByNameAndRegionName(region, keyword, page, 6, ""))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(page, 6), expectedCount));

        mockMvc.perform(get("/api/v1/filter/searchAll/{page}/elementsCount", page)
                        .param("region", region)
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedCount));
    }*/
}
