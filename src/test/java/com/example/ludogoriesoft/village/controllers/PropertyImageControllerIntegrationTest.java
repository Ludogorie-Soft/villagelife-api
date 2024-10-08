package com.example.ludogoriesoft.village.controllers;

import com.example.ludogorieSoft.village.controllers.PropertyImageController;
import com.example.ludogorieSoft.village.dtos.PropertyImageDTO;
import com.example.ludogorieSoft.village.exeptions.handler.ApiExceptionHandler;
import com.example.ludogorieSoft.village.services.PropertyImageService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = PropertyImageController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = PropertyImageController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = ApiExceptionHandler.class
                )
        }
)
class PropertyImageControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyImageService propertyImageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPropertyImagesByPropertyId() throws Exception {
        PropertyImageDTO propertyImageDTO1 = new PropertyImageDTO();
        propertyImageDTO1.setId(1L);
        propertyImageDTO1.setImageName("image1.jpg");

        PropertyImageDTO propertyImageDTO2 = new PropertyImageDTO();
        propertyImageDTO2.setId(2L);
        propertyImageDTO2.setImageName("image2.jpg");

        List<PropertyImageDTO> propertyImageDTOList = Arrays.asList(propertyImageDTO1, propertyImageDTO2);

        when(propertyImageService.getPropertyImagesByPropertyId(1L)).thenReturn(propertyImageDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/property-images/property/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].imageName").value("image1.jpg"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].imageName").value("image2.jpg"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }

}
