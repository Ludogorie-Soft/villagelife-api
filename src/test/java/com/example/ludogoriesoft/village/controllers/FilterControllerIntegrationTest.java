package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.enums.PropertyTransferType;
import com.example.ludogorieSoft.village.enums.PropertyType;
import com.example.ludogorieSoft.village.exeptions.handler.ApiExceptionHandler;
import com.example.ludogorieSoft.village.services.PropertyService;
import com.example.ludogorieSoft.village.services.VillageService;
import com.example.ludogorieSoft.village.slack.SlackMessage;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private SlackMessage slackMessage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
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

        Page<VillageDTO> mockPage = new PageImpl<>(List.of(villageDTO1, villageDTO2));
        given(villageSearchService.getSearchVillages2(any(), any(), any(), any(), any(), any())).willReturn(mockPage);

        mockMvc.perform(get("/api/v1/filter/searchVillages")
                        .param("region", "TestRegion")
                        .param("name", "TestVillage")
                        .param("objectAroundVillageDTOS", "Object1", "Object2")
                        .param("livingConditionDTOS", "Condition1", "Condition2")
                        .param("children", children)
                        .param("page", "0")
                        .param("size", "6")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

    }

    @Test
    void testSearchPropertiesByCriteria_withValidParams() throws Exception {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(1L);
        villageDTO.setName("Village Name");

        PropertyDTO property1 = new PropertyDTO();
        property1.setId(1L);
        property1.setVillageDTO(villageDTO);
        property1.setPropertyType(PropertyType.HOUSE);
        property1.setPropertyTransferType(PropertyTransferType.SALE);
        property1.setPrice(BigDecimal.valueOf(50000));
        property1.setBuildUpArea(120.5);
        property1.setRoomsCount((short) 3);
        property1.setHeating(Arrays.asList("Electric", "Wood"));
        property1.setCreatedAt(LocalDateTime.now());

        PropertyDTO property2 = new PropertyDTO();
        property2.setId(2L);
        property2.setVillageDTO(villageDTO);
        property2.setPropertyType(PropertyType.APARTMENT);
        property2.setPropertyTransferType(PropertyTransferType.RENT);
        property2.setPrice(BigDecimal.valueOf(75000));
        property2.setBuildUpArea(85.0);
        property2.setRoomsCount((short) 2);
        property2.setHeating(Collections.singletonList("Gas"));
        property2.setCreatedAt(LocalDateTime.now());

        List<PropertyDTO> properties = Arrays.asList(property1, property2);
        Page<PropertyDTO> mockPage = new PageImpl<>(properties, PageRequest.of(0, 2), 2);

        given(propertyService.getSearchProperties(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).willReturn(mockPage);

        mockMvc.perform(get("/api/v1/filter/searchProperties")
                        .param("propertyTypes", "House", "Apartment")
                        .param("propertyTransferType", "Sale")
                        .param("minBuiltUpArea", "50")
                        .param("maxBuiltUpArea", "150")
                        .param("minRoomsCount", "2")
                        .param("maxRoomsCount", "4")
                        .param("minPrice", "40000")
                        .param("maxPrice", "100000")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "price,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].villageDTO.name").value("Village Name"))
                .andExpect(jsonPath("$.content[0].propertyType").value("HOUSE"))
                .andExpect(jsonPath("$.content[0].price").value(50000))
                .andExpect(jsonPath("$.content[0].buildUpArea").value(120.5))
                .andExpect(jsonPath("$.content[0].roomsCount").value(3))
                .andExpect(jsonPath("$.content[0].heating", hasItem("Electric")))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].villageDTO.name").value("Village Name"))
                .andExpect(jsonPath("$.content[1].propertyType").value("APARTMENT"))
                .andExpect(jsonPath("$.content[1].price").value(75000))
                .andExpect(jsonPath("$.content[1].buildUpArea").value(85.0))
                .andExpect(jsonPath("$.content[1].roomsCount").value(2))
                .andExpect(jsonPath("$.content[1].heating", hasItem("Gas")));
    }

    @Test
    void testSearchPropertiesByCriteria_withNoResults() throws Exception {
        Page<PropertyDTO> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 2), 0);

        given(propertyService.getSearchProperties(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).willReturn(emptyPage);

        mockMvc.perform(get("/api/v1/filter/searchProperties")
                        .param("propertyTypes", "House")
                        .param("minPrice", "1000000") // High price to ensure no results
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void testSearchPropertiesByCriteria_withOnlyRequiredParams() throws Exception {
        PropertyDTO property = new PropertyDTO();
        property.setId(1L);
        property.setPropertyType(PropertyType.HOUSE);
        property.setPrice(BigDecimal.valueOf(50000));
        property.setRoomsCount((short) 3);
        property.setBuildUpArea(120.5);
        property.setCreatedAt(LocalDateTime.now());

        Page<PropertyDTO> mockPage = new PageImpl<>(List.of(property), PageRequest.of(0, 1), 1);

        given(propertyService.getSearchProperties(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).willReturn(mockPage);

        mockMvc.perform(get("/api/v1/filter/searchProperties")
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].propertyType").value("HOUSE"))
                .andExpect(jsonPath("$.content[0].price").value(50000))
                .andExpect(jsonPath("$.content[0].roomsCount").value(3))
                .andExpect(jsonPath("$.content[0].buildUpArea").value(120.5));
    }
//    TODO
//    @Test
//    void testSearchPropertiesByCriteria_withInvalidParams() throws Exception {
//        mockMvc.perform(get("/api/v1/filter/searchProperties")
//                        .param("minPrice", "notANumber") // Invalid parameter
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
}
