package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.enums.Children;
import com.example.ludogorieSoft.village.exeptions.handler.ApiExceptionHandler;
import com.example.ludogorieSoft.village.services.VillageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
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

        List<VillageDTO> villageDTOList = Arrays.asList(villageDTO1, villageDTO2);

        when(villageSearchService.getAllVillages()).thenReturn(villageDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertNotNull(response);
    }


    @Test
    void testGetVillageByName() throws Exception {
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

        String name = "Example Name";

        when(villageSearchService.getAllSearchVillages(name)).thenReturn(villageDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/byName")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[0].dateUpload").exists())
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"))
                .andExpect(jsonPath("$[1].dateUpload").exists())
                .andExpect(jsonPath("$[1].status").value(true));
    }

    @Test
    void testGetVillageByRegion() throws Exception {
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

        String region = "Example Region";

        when(villageSearchService.getAllSearchVillagesByRegionName(region)).thenReturn(villageDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/byRegion")
                        .param("region", region)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[0].dateUpload").exists())
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"))
                .andExpect(jsonPath("$[1].dateUpload").exists())
                .andExpect(jsonPath("$[1].status").value(true));
    }

    @Test
    void testGetVillageByNameAndRegion() throws Exception {
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

        String region = "Example Region";
        String keyword = "Example Keyword";

        when(villageSearchService.getAllSearchVillagesByNameAndRegionName(region, keyword)).thenReturn(villageDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchAll")
                        .param("region", region)
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[0].dateUpload").exists())
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"))
                .andExpect(jsonPath("$[1].dateUpload").exists())
                .andExpect(jsonPath("$[1].status").value(true));
    }


    @Test
    void testSearchVillagesByCriteria() throws Exception {
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

        List<String> objectAroundVillageDTOS = Arrays.asList("object1", "object2");
        List<String> livingConditionDTOS = Arrays.asList("condition1", "condition2");
        String children = "FROM_21_TO_50";

        when(villageSearchService.getSearchVillages(objectAroundVillageDTOS, livingConditionDTOS, Children.FROM_21_TO_50))
                .thenReturn(villageDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchVillages")
                        .param("objectAroundVillageDTOS", "object1", "object2")
                        .param("livingConditionDTOS", "condition1", "condition2")
                        .param("children", children)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[0].dateUpload").exists())
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"))
                .andExpect(jsonPath("$[1].dateUpload").exists())
                .andExpect(jsonPath("$[1].status").value(true));
    }


    @Test
    void testSearchVillagesByLivingConditionAndChildren() throws Exception {
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
        List<String> livingConditionDTOS = Arrays.asList("condition1", "condition2");
        String children = Children.FROM_21_TO_50.name(); // Use the enum name here

        when(villageSearchService.getSearchVillagesByLivingConditionAndChildren(livingConditionDTOS, Children.FROM_21_TO_50))
                .thenReturn(villageDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchVillagesByLivingConditionAndChildren")
                        .param("livingConditionDTOS", "condition1", "condition2")
                        .param("children", children)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[0].dateUpload").exists())
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"))
                .andExpect(jsonPath("$[1].dateUpload").exists())
                .andExpect(jsonPath("$[1].status").value(true));
    }

    @Test
    void testSearchVillagesByObjectAndChildren() throws Exception {
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
        List<String> objectAroundVillageDTOS = Arrays.asList("object1", "object2");
        String children = Children.FROM_21_TO_50.name(); // Use the enum name here

        when(villageSearchService.getSearchVillagesByObjectAndChildren(objectAroundVillageDTOS, Children.FROM_21_TO_50))
                .thenReturn(villageDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchVillagesByObjectAndChildren")
                        .param("objectAroundVillageDTOS", "object1", "object2")
                        .param("children", children)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[0].dateUpload").exists())
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"))
                .andExpect(jsonPath("$[1].dateUpload").exists())
                .andExpect(jsonPath("$[1].status").value(true));
    }


    @Test
    void testSearchVillagesByObjectAndLivingCondition() throws Exception {
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
        List<String> objectAroundVillageDTOS = Arrays.asList("object1", "object2");
        List<String> livingConditionDTOS = Arrays.asList("condition1", "condition2");

        when(villageSearchService.getSearchVillagesByObjectAndLivingCondition(objectAroundVillageDTOS, livingConditionDTOS))
                .thenReturn(villageDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchVillagesByObjectAndLivingCondition")
                        .param("objectAroundVillageDTOS", "object1", "object2")
                        .param("livingConditionDTOS", "condition1", "condition2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[0].dateUpload").exists())
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"))
                .andExpect(jsonPath("$[1].dateUpload").exists())
                .andExpect(jsonPath("$[1].status").value(true));
    }

    @Test
    void testSearchVillagesByChildrenCount() throws Exception {
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
        String children = "FROM_21_TO_50";

        when(villageSearchService.getSearchVillagesByChildrenCount(Children.FROM_21_TO_50))
                .thenReturn(villageDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchVillagesByChildrenCount")
                        .param("children", children)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[0].dateUpload").exists())
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"))
                .andExpect(jsonPath("$[1].dateUpload").exists())
                .andExpect(jsonPath("$[1].status").value(true));
    }

    @Test
    void testSearchVillagesByObject() throws Exception {
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
        List<String> objectAroundVillageDTOS = Arrays.asList("object1", "object2");

        when(villageSearchService.getSearchVillagesByObject(objectAroundVillageDTOS))
                .thenReturn(villageDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchVillagesByObject")
                        .param("objectAroundVillageDTOS", "object1", "object2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[0].dateUpload").exists())
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"))
                .andExpect(jsonPath("$[1].dateUpload").exists())
                .andExpect(jsonPath("$[1].status").value(true));
    }

    @Test
    void testSearchVillagesByLivingCondition() throws Exception {
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
        List<String> livingConditionDTOS = Arrays.asList("condition1", "condition2");

        when(villageSearchService.getSearchVillagesByLivingCondition(livingConditionDTOS))
                .thenReturn(villageDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchVillagesByLivingCondition")
                        .param("livingConditionDTOS", "condition1", "condition2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Village Name 1"))
                .andExpect(jsonPath("$[0].dateUpload").exists())
                .andExpect(jsonPath("$[0].status").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Village Name 2"))
                .andExpect(jsonPath("$[1].dateUpload").exists())
                .andExpect(jsonPath("$[1].status").value(true));
    }


    @Test
    void testGetAllApprovedVillagesNoVillages() throws Exception {
        when(villageSearchService.getAllVillages()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetVillageByNameVillageNotFound() throws Exception {
        String name = "Nonexistent Village";
        when(villageSearchService.getAllSearchVillages(name)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/byName")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetVillageByRegionRegionNotFound() throws Exception {
        String region = "Nonexistent Region";
        when(villageSearchService.getAllSearchVillagesByRegionName(region)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/byRegion")
                        .param("region", region)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @ParameterizedTest
    @CsvSource({
            "'Nonexistent Region','Nonexistent Village'",
            "'Existing Region','Nonexistent Village'",
            "'Nonexistent Region','Existing Village'"
    })
    void testGetVillageByNameAndRegion(String region, String keyword) throws Exception {
        when(villageSearchService.getAllSearchVillagesByNameAndRegionName(region, keyword)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchAll")
                        .param("region", region)
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testSearchVillagesByObjectAndLivingConditionNoData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchVillagesByObjectAndLivingCondition")
                        .param("objectAroundVillageDTOS", "")
                        .param("livingConditionDTOS", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


    @Test
    void testSearchVillagesByObjectNoObjects() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchVillagesByObject")
                        .param("objectAroundVillageDTOS", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


    @Test
    void testSearchVillagesByLivingConditionNoLivingConditions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filter/searchVillagesByLivingCondition")
                        .param("livingConditionDTOS", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }


}
