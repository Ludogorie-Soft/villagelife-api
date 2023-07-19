package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.MessageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
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
    private final EmailSenderService emailSenderService;
    public MessageDTO messageToMessageDTO(Message message){
        return modelMapper.map(message, MessageDTO.class);
    }
    public MessageDTO createMessage(MessageDTO messageDTO) {
        try {
            Message message = new Message(null, messageDTO.getUserName(), messageDTO.getEmail(), messageDTO.getUserMessage());
            messageRepository.save(message);

            emailSenderService.sendEmail(
                    messageDTO.getEmail(),
                    "Име на потребител: " + messageDTO.getUserName() +
                            "\nEmail: " + messageDTO.getEmail() +
                            "\nЗапитване или заявка: " + messageDTO.getUserMessage(),
                    "VillageLife");
            return messageDTO;
        } catch (Exception e) {
            throw new ApiRequestException("Error creating message");
        }
    }
}
