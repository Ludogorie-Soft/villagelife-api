package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.SubscriptionDTO;
import com.example.ludogorieSoft.village.exeptions.handler.ApiExceptionHandler;
import com.example.ludogorieSoft.village.services.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = SubscriptionController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = SubscriptionController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = ApiExceptionHandler.class
                )
        }
)
class SubscriptionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSubscription() throws Exception {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setId(1L);
        subscriptionDTO.setEmail("test@example.com");

        when(subscriptionService.createSubscription(any(SubscriptionDTO.class))).thenReturn(subscriptionDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\": \"test@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }

    @Test
    void testGetAllSubscriptions() throws Exception {
        SubscriptionDTO subscriptionDTO1 = new SubscriptionDTO();
        subscriptionDTO1.setId(1L);
        subscriptionDTO1.setEmail("email1@example.com");
        subscriptionDTO1.setDeletedAt(null);

        SubscriptionDTO subscriptionDTO2 = new SubscriptionDTO();
        subscriptionDTO2.setId(2L);
        subscriptionDTO2.setEmail("email2@example.com");
        subscriptionDTO2.setDeletedAt(LocalDateTime.now());

        List<SubscriptionDTO> subscriptionDTOList = Arrays.asList(subscriptionDTO1, subscriptionDTO2);

        when(subscriptionService.getAllSubscriptions()).thenReturn(subscriptionDTOList);

        MvcResult mvcResult =  mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("email1@example.com"))
                .andExpect(jsonPath("$[0].deletedAt", nullValue()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].email").value("email2@example.com"))
                .andExpect(jsonPath("$[1].deletedAt").value(notNullValue()))
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(response);

    }

    @Test
    void testEmailExistsEndpointWithEmailExistsAndIsActive() throws Exception {
        String email = "existing@example.com";

        when(subscriptionService.emailExists(email)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/subscriptions/check-email")
                        .param("email", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void testEmailExistsEndpointWithEmailDoesNotExistOrIsInactive() throws Exception {
        String email = "nonexistent@example.com";

        when(subscriptionService.emailExists(email)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/subscriptions/check-email")
                        .param("email", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }


}
