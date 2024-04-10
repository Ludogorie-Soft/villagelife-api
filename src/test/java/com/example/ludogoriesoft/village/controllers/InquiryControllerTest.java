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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
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

    @Test
    void testGetAllInquiries() throws Exception {
        InquiryDTO inquiryDTO1 = new InquiryDTO();
        inquiryDTO1.setId(1L);
        inquiryDTO1.setUserMessage("Message 1");

        InquiryDTO inquiryDTO2 = new InquiryDTO();
        inquiryDTO2.setId(2L);
        inquiryDTO2.setUserMessage("Message 2");

        List<InquiryDTO> inquiriesDTOList = Arrays.asList(inquiryDTO1, inquiryDTO2);

        when(inquiryService.getAllInquiries()).thenReturn(inquiriesDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inquiries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userMessage").value("Message 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].userMessage").value("Message 2"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
    }
}
