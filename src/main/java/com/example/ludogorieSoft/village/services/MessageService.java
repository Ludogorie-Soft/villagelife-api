package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AdministratorDTO;
import com.example.ludogorieSoft.village.dtos.AdministratorRequest;
import com.example.ludogorieSoft.village.dtos.MessageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Administrator;
import com.example.ludogorieSoft.village.model.Message;
import com.example.ludogorieSoft.village.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    public MessageDTO messageToMessageDTO(Message message){
        return modelMapper.map(message, MessageDTO.class);
    }
    public MessageDTO createMessage(MessageDTO messageDTO) {
        Message message = new Message(null, messageDTO.getUserName(), messageDTO.getEmail(), messageDTO.getUserMessage());
        messageRepository.save(message);
        return messageDTO;
    }
}
