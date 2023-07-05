package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.MessageDTO;
import com.example.ludogorieSoft.village.model.Message;
import com.example.ludogorieSoft.village.repositories.MessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;

class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //@Test
    //void testCreateMessage() {
    //    MessageDTO messageDTO = new MessageDTO();
    //    messageDTO.setUserName("John");
    //    messageDTO.setEmail("john@example.com");
    //    messageDTO.setUserMessage("Hello!");
//
    //    Message message = new Message(null, messageDTO.getUserName(), messageDTO.getEmail(), messageDTO.getUserMessage());
//
    //    when(messageRepository.save(any(Message.class))).thenReturn(message);
//
    //    MessageDTO result = messageService.createMessage(messageDTO);
//
    //    Assertions.assertEquals(messageDTO, result);
//
    //    verify(messageRepository, times(1)).save(any(Message.class));
    //}
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
