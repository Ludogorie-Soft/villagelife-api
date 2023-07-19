package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.MessageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Message;
import com.example.ludogorieSoft.village.repositories.MessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    EmailSenderService emailSenderService;
    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMessageWhenSuccessful() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setUserName("John Doe");
        messageDTO.setEmail("johndoe@example.com");
        messageDTO.setUserMessage("Hello, this is a test message.");
        Message message = new Message(null, messageDTO.getUserName(), messageDTO.getEmail(), messageDTO.getUserMessage());

        doNothing().when(emailSenderService).sendEmail(anyString(), anyString(), anyString());
        when(messageRepository.save(message)).thenReturn(message);

        MessageDTO result = messageService.createMessage(messageDTO);

        Assertions.assertEquals(messageDTO, result);
        verify(messageRepository).save(any(Message.class));
        verify(emailSenderService).sendEmail(
                "johndoe@example.com",
                "\u0418\u043c\u0435 \u043d\u0430 \u043f\u043e\u0442\u0440\u0435\u0431\u0438\u0442\u0435\u043b: John Doe\nEmail: johndoe@example.com\n\u0417\u0430\u043f\u0438\u0442\u0432\u0430\u043d\u0435 \u0438\u043b\u0438 \u0437\u0430\u044f\u0432\u043a\u0430: Hello, this is a test message.",
                "VillageLife");
    }

    @Test
    void testCreateMessageErrorCreatingMessage() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setUserName("John Doe");
        messageDTO.setEmail("johndoe@example.com");
        messageDTO.setUserMessage("Hello, this is a test message.");

        doThrow(new RuntimeException("Error creating message")).when(messageRepository).save(any(Message.class));

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            messageService.createMessage(messageDTO);
        });

        Assertions.assertEquals("Error creating message", exception.getMessage());

        verify(messageRepository).save(any(Message.class));

        verify(emailSenderService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testMessageToMessageDTOWithValidMessage() {
        Message message = new Message();
        message.setId(123L);
        message.setUserMessage("Hello, World!");

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(123L);
        messageDTO.setUserMessage("Hello, World!");

        when(modelMapper.map(message, MessageDTO.class)).thenReturn(messageDTO);

        MessageDTO result = messageService.messageToMessageDTO(message);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(123L, result.getId());
        Assertions.assertEquals("Hello, World!", result.getUserMessage());
    }

    @Test
    void testMessageToMessageDTOWithNullMessage() {
        MessageDTO result = messageService.messageToMessageDTO(null);
        Assertions.assertNull(result);
    }
}
