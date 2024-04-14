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

import java.util.ArrayList;
import java.util.List;

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

        Message message = new Message(null, messageDTO.getUserName(), messageDTO.getEmail(), messageDTO.getUserMessage(), null);

        when(messageRepository.save(any(Message.class))).thenReturn(message);

        MessageDTO result = messageService.createMessage(messageDTO);

        Assertions.assertEquals(messageDTO, result);
        verify(messageRepository, times(1)).save(any(Message.class));
        verify(emailSenderService, times(1)).sendEmail(
                "johndoe@example.com",
                "<html><body><table></table></body></html>",
                "VillageLife"
        );
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

    @Test
    void testGetAllMessagesWithExistingMessages() {
        Message message1 = new Message();
        message1.setId(1L);
        message1.setUserMessage("Message 1");

        Message message2 = new Message();
        message2.setId(2L);
        message2.setUserMessage("Message 2");

        MessageDTO messageDTO1 = new MessageDTO();
        messageDTO1.setId(1L);
        messageDTO1.setUserMessage("Message 1");

        MessageDTO messageDTO2 = new MessageDTO();
        messageDTO2.setId(2L);
        messageDTO2.setUserMessage("Message 2");

        List<Message> messages = new ArrayList<>();
        messages.add(message1);
        messages.add(message2);

        List<MessageDTO> expectedMessages = new ArrayList<>();
        expectedMessages.add(messageDTO1);
        expectedMessages.add(messageDTO2);

        when(messageRepository.findAll()).thenReturn(messages);
        when(modelMapper.map(message1, MessageDTO.class)).thenReturn(messageDTO1);
        when(modelMapper.map(message2, MessageDTO.class)).thenReturn(messageDTO2);

        List<MessageDTO> result = messageService.getAllMessages();

        verify(messageRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(message1, MessageDTO.class);
        verify(modelMapper, times(1)).map(message2, MessageDTO.class);
        Assertions.assertEquals(expectedMessages, result);
    }

    @Test
    void testGetAllMessagesWithNoMessages() {
        List<Message> messages = new ArrayList<>();
        List<MessageDTO> expectedMessages = new ArrayList<>();

        when(messageRepository.findAll()).thenReturn(messages);
        List<MessageDTO> result = messageService.getAllMessages();

        verify(messageRepository, times(1)).findAll();
        verify(modelMapper, never()).map(any(Message.class), eq(MessageDTO.class));
        Assertions.assertEquals(expectedMessages, result);
    }

}
