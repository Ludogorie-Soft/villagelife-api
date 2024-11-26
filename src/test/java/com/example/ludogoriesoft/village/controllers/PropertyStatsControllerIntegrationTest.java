package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PropertyStatsDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.exeptions.handler.ApiExceptionHandler;
import com.example.ludogorieSoft.village.services.PropertyStatsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = PropertyStatsController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = PropertyStatsController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = ApiExceptionHandler.class
                )
        }
)
class PropertyStatsControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyStatsService propertyStatsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIncrementPropertyViews() throws Exception {
        Long propertyId = 1L;
        PropertyStatsDTO propertyStatsDTO = new PropertyStatsDTO();
        propertyStatsDTO.setViews(101L);

        when(propertyStatsService.incrementViewsByPropertyId(propertyId)).thenReturn(propertyStatsDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/property-stats/{propertyId}/increment-views", propertyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.views").value(101))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }

    @Test
    void testIncrementPropertyViewsWhenPropertyNotFound() throws Exception {
        Long propertyId = 999L;
        String expectedErrorMessage = "Property not found";

        when(propertyStatsService.incrementViewsByPropertyId(propertyId)).thenThrow(new ApiRequestException(expectedErrorMessage));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/property-stats/{propertyId}/increment-views", propertyId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedErrorMessage));
    }

}
