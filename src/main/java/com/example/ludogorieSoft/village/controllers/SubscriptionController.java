package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.SubscriptionDTO;
import com.example.ludogorieSoft.village.services.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/subscriptions")
@AllArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    @PostMapping
    public ResponseEntity<SubscriptionDTO> createSubscription(@Valid @RequestBody SubscriptionDTO subscriptionDTO) {
        SubscriptionDTO createdSubscriptionDTO = subscriptionService.createSubscription(subscriptionDTO);
        return new ResponseEntity<>(createdSubscriptionDTO, HttpStatus.CREATED);
    }
}
