package com.example.ludogoriesoft.village.services;

import com.example.ludogoriesoft.village.dtos.AdministratorDTO;
import com.example.ludogoriesoft.village.dtos.AdministratorRequest;
import com.example.ludogoriesoft.village.dtos.MessageDTO;
import com.example.ludogoriesoft.village.exeptions.ApiRequestException;
import com.example.ludogoriesoft.village.model.Administrator;
import com.example.ludogoriesoft.village.model.Message;
import com.example.ludogoriesoft.village.repositories.MessageRepository;
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
