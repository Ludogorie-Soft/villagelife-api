package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.SubscriptionDTO;
import com.example.ludogorieSoft.village.services.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }
}
