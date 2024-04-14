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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
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

    @Test
    void testGetAllMessages() throws Exception {
        MessageDTO messageDTO1 = new MessageDTO();
        messageDTO1.setId(1L);
        messageDTO1.setUserMessage("Message 1");

        MessageDTO messageDTO2 = new MessageDTO();
        messageDTO2.setId(2L);
        messageDTO2.setUserMessage("Message 2");

        List<MessageDTO> messagesDTOList = Arrays.asList(messageDTO1, messageDTO2);

        when(messageService.getAllMessages()).thenReturn(messagesDTOList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/messages")
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
