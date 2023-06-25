package com.example.ludogoriesoft.village.controllers;

import com.example.ludogoriesoft.village.dtos.LivingConditionDTO;
import com.example.ludogoriesoft.village.dtos.MessageDTO;
import com.example.ludogoriesoft.village.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/messages")
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;
    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@Valid @RequestBody MessageDTO messageDTO) {
        MessageDTO messageDTO1 = messageService.createMessage(messageDTO);
        return new ResponseEntity<>(messageDTO1, HttpStatus.CREATED);
    }
}
