package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.MessageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Message;
import com.example.ludogorieSoft.village.repositories.MessageRepository;
import com.example.ludogorieSoft.village.utils.TimestampUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
            Message message = new Message(null, messageDTO.getUserName().trim(), messageDTO.getEmail().trim(), messageDTO.getUserMessage().trim(), TimestampUtils.getCurrentTimestamp());
            messageRepository.save(message);
            String emailBody = createMessageEmailBody(message);
            emailSenderService.sendEmail(messageDTO.getEmail(), emailBody, "VillageLife");
            return messageDTO;
        } catch (Exception e) {
            throw new ApiRequestException("Error creating message");
        }
    }

    public String createMessageEmailBody(Message message){
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("<html><body><table>");
        emailSenderService.addTableRow(emailBody, "Име на потребител", message.getUserName());
        emailSenderService.addTableRow(emailBody, "Email", message.getEmail());
        emailSenderService.addTableRow(emailBody, "Запитване или заявка", message.getUserMessage());
        emailBody.append("</table></body></html>");
        return emailBody.toString();
    }
    public List<MessageDTO> getAllMessages() {
        return messageRepository.findAll()
                .stream()
                .map(this::messageToMessageDTO)
                .toList();
    }
}
