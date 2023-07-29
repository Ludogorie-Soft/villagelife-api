package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.InquiryDTO;
import com.example.ludogorieSoft.village.services.InquiryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = InquiryController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = InquiryController.class
                )
        }
)
class InquiryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InquiryService inquiryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInquiry() throws Exception {
        InquiryDTO inquiryDTO = new InquiryDTO();
        inquiryDTO.setId(1L);
        inquiryDTO.setUserName("John");
        inquiryDTO.setEmail("john@example.com");
        inquiryDTO.setUserMessage("Hello, World!");

        when(inquiryService.createInquiry(any(InquiryDTO.class))).thenReturn(inquiryDTO);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/inquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inquiryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userName").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.userMessage").value("Hello, World!"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }

}
