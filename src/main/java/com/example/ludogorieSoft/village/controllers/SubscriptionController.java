package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.SubscriptionDTO;
import com.example.ludogorieSoft.village.services.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/subscriptions")
@AllArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    @PostMapping
    public ResponseEntity<SubscriptionDTO> createSubscription(@Valid @RequestBody SubscriptionDTO subscriptionDTO, UriComponentsBuilder uriComponentsBuilder) {
        SubscriptionDTO createdSubscriptionDTO = subscriptionService.createSubscription(subscriptionDTO);
        URI location = uriComponentsBuilder.path("/api/v1/subscriptions/{id}")
                .buildAndExpand(createdSubscriptionDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdSubscriptionDTO);
    }
}
