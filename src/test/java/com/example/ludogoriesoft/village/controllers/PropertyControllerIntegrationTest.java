package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.exeptions.handler.ApiExceptionHandler;
import com.example.ludogorieSoft.village.services.PropertyService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.ArgumentMatchers.any;
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
    @Test
    void testCreateProperty() throws Exception {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setId(1L);
        propertyDTO.setPrice(new BigDecimal("180000"));
        propertyDTO.setPhoneNumber("1234567890");
        propertyDTO.setBuildUpArea(120.0);
        propertyDTO.setYardArea(50.0);
        propertyDTO.setRoomsCount((short)3);
        propertyDTO.setBathroomsCount((short)2);
        propertyDTO.setHeating(Arrays.asList("Gas", "Electric"));
        propertyDTO.setDescription("A beautiful house in the village.");
        propertyDTO.setAddress("123 Village St.");
        propertyDTO.setMainImageBytes(new byte[]{1, 2, 3});

        PropertyDTO createdPropertyDTO = new PropertyDTO();
        createdPropertyDTO.setId(1L);
        createdPropertyDTO.setPrice(new BigDecimal("180000"));
        createdPropertyDTO.setPhoneNumber("1234567890");
        createdPropertyDTO.setBuildUpArea(120.0);
        createdPropertyDTO.setYardArea(50.0);
        createdPropertyDTO.setRoomsCount((short)3);
        createdPropertyDTO.setBathroomsCount((short)2);
        createdPropertyDTO.setHeating(Arrays.asList("Gas", "Electric"));
        createdPropertyDTO.setDescription("A beautiful house in the village.");
        createdPropertyDTO.setAddress("123 Village St.");

        when(propertyService.createProperty(any(PropertyDTO.class))).thenReturn(createdPropertyDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(propertyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.price").value(180000))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.buildUpArea").value(120.0))
                .andExpect(jsonPath("$.yardArea").value(50.0))
                .andExpect(jsonPath("$.heating.length()").value(2))
                .andExpect(jsonPath("$.heating[0]").value("Gas"))
                .andExpect(jsonPath("$.heating[1]").value("Electric"))
                .andExpect(jsonPath("$.description").value("A beautiful house in the village."))
                .andExpect(jsonPath("$.address").value("123 Village St."))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }
    @Test
    void testCreatePropertyInvalidImage() throws Exception {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setPrice(new BigDecimal("200000"));
        propertyDTO.setDescription("A property with an invalid image.");
        propertyDTO.setAddress("456 Village St.");
        propertyDTO.setMainImageBytes(new byte[]{});

        PropertyDTO createdPropertyDTO = new PropertyDTO();
        createdPropertyDTO.setId(1L);
        createdPropertyDTO.setPrice(new BigDecimal("200000"));
        createdPropertyDTO.setDescription("A property with an invalid image.");
        createdPropertyDTO.setAddress("456 Village St.");

        when(propertyService.createProperty(any(PropertyDTO.class))).thenReturn(createdPropertyDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(propertyDTO)))
                .andExpect(status().isCreated())  // Expect 201 Created despite invalid image
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.price").value(200000))
                .andExpect(jsonPath("$.description").value("A property with an invalid image."))
                .andExpect(jsonPath("$.address").value("456 Village St."))
                .andReturn();
    }
    @Test
    void testCreatePropertyWithOptionalFields() throws Exception {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setPrice(new BigDecimal("300000"));
        propertyDTO.setDescription("A property with omitted optional fields.");
        propertyDTO.setAddress("101 Village St.");
        propertyDTO.setMainImageBytes(new byte[]{1, 2, 3});
        propertyDTO.setHeating(Arrays.asList("Gas"));

        PropertyDTO createdPropertyDTO = new PropertyDTO();
        createdPropertyDTO.setId(1L);
        createdPropertyDTO.setPrice(new BigDecimal("300000"));
        createdPropertyDTO.setDescription("A property with omitted optional fields.");
        createdPropertyDTO.setAddress("101 Village St.");

        when(propertyService.createProperty(any(PropertyDTO.class))).thenReturn(createdPropertyDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(propertyDTO)))
                .andExpect(status().isCreated())  // Expect 201 Created
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.price").value(300000))
                .andExpect(jsonPath("$.description").value("A property with omitted optional fields."))
                .andExpect(jsonPath("$.address").value("101 Village St."))
                .andReturn();
    }
    @Test
    void testCreatePropertyWithLargeDescription() throws Exception {
        String largeDescription = "A".repeat(500);
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setPrice(new BigDecimal("400000"));
        propertyDTO.setDescription(largeDescription);
        propertyDTO.setAddress("202 Village St.");
        propertyDTO.setMainImageBytes(new byte[]{1, 2, 3});

        PropertyDTO createdPropertyDTO = new PropertyDTO();
        createdPropertyDTO.setId(1L);
        createdPropertyDTO.setPrice(new BigDecimal("400000"));
        createdPropertyDTO.setDescription(largeDescription);
        createdPropertyDTO.setAddress("202 Village St.");

        when(propertyService.createProperty(any(PropertyDTO.class))).thenReturn(createdPropertyDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(propertyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.price").value(400000))
                .andExpect(jsonPath("$.description").value(largeDescription))
                .andExpect(jsonPath("$.address").value("202 Village St."))
                .andReturn();
    }
}
