package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.MessageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.services.MessageService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = MessageController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = MessageController.class
                )
        }
)
class MessageControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMessage() throws Exception {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setUserName("John");
        messageDTO.setEmail("john@example.com");
        messageDTO.setUserMessage("Hello, World!");

        when(messageService.createMessage(any(MessageDTO.class))).thenReturn(messageDTO);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"userName\": \"John\", \"email\": \"john@example.com\", \"userMessage\": \"Hello, World!\"}"))
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
    void testCreateMessageExceptionThrown() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setUserName("John");
        messageDTO.setEmail("john@example.com");
        messageDTO.setUserMessage("Hello, World!");

        MessageService messageService = mock(MessageService.class);
        when(messageService.createMessage(messageDTO)).thenThrow(new RuntimeException("Error creating message"));
        MessageController messageController = new MessageController(messageService);

        try {
            messageController.createMessage(messageDTO);
            Assertions.fail("Expected an ApiRequestException to be thrown");
        } catch (ApiRequestException e) {
            Assertions.assertEquals("Error creating message", e.getMessage());
        }
        verify(messageService).createMessage(messageDTO);
    }

}
