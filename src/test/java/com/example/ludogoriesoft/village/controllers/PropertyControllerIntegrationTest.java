package com.example.ludogoriesoft.village.controllers;

import com.example.ludogorieSoft.village.controllers.PropertyController;
import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.exeptions.handler.ApiExceptionHandler;
import com.example.ludogorieSoft.village.services.PropertyService;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = PropertyController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = PropertyController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = ApiExceptionHandler.class
                )
        }
)
class PropertyControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProperties() throws Exception {
        PropertyDTO propertyDTO1 = new PropertyDTO();
        propertyDTO1.setId(1L);
        propertyDTO1.setPrice(new BigDecimal("100000"));

        PropertyDTO propertyDTO2 = new PropertyDTO();
        propertyDTO2.setId(2L);
        propertyDTO2.setPrice(new BigDecimal("200000"));

        Page<PropertyDTO> propertyPage = new PageImpl<>(Arrays.asList(propertyDTO1, propertyDTO2));

        when(propertyService.getAllPropertiesAndMainImage(0, 2)).thenReturn(propertyPage);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/properties/0/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].price").value(100000))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].price").value(200000))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }

    @Test
    void testGetAllPropertiesByVillageId() throws Exception {
        PropertyDTO propertyDTO1 = new PropertyDTO();
        propertyDTO1.setId(1L);
        propertyDTO1.setPrice(new BigDecimal("150000"));

        PropertyDTO propertyDTO2 = new PropertyDTO();
        propertyDTO2.setId(2L);
        propertyDTO2.setPrice(new BigDecimal("250000"));

        List<PropertyDTO> propertyDTOList = Arrays.asList(propertyDTO1, propertyDTO2);

        when(propertyService.getAllPropertiesByVillageIdAndMainImage(1L)).thenReturn(propertyDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/properties/village/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].price").value(150000))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].price").value(250000))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }

    @Test
    void testGetPropertyWithMainImageById() throws Exception {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setId(1L);
        propertyDTO.setPrice(new BigDecimal("180000"));

        when(propertyService.getPropertyWithMainImageById(1L)).thenReturn(propertyDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/properties/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.price").value(180000))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }
}
