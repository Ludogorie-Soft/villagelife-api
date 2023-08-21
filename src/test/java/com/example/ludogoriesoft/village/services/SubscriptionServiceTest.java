package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.SubscriptionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Subscription;
import com.example.ludogorieSoft.village.repositories.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {
    @InjectMocks
    SubscriptionService subscriptionService;
    @Mock
    SubscriptionRepository subscriptionRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSubscriptionWhenEmailBlank() {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO(null, "", null);
        assertThrows(ApiRequestException.class, () -> subscriptionService.createSubscription(subscriptionDTO));
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void testCreateSubscriptionWhenSubscriptionExistsAndDeleted() {
        String email = "test@example.com";
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setEmail(email);

        Subscription existingSubscription = new Subscription();
        existingSubscription.setEmail(email);
        existingSubscription.setDeletedAt(LocalDateTime.now());

        when(subscriptionRepository.findByEmail(email)).thenReturn(existingSubscription);
        SubscriptionDTO resultDTO = subscriptionService.createSubscription(subscriptionDTO);

        assertNull(resultDTO.getDeletedAt());
        verify(subscriptionRepository).save(existingSubscription);
    }

    @Test
    void testCreateSubscriptionWhenSubscriptionDoesNotExist() {
        String email = "test@example.com";
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setEmail(email);

        when(subscriptionRepository.findByEmail(email)).thenReturn(null);
        SubscriptionDTO resultDTO = subscriptionService.createSubscription(subscriptionDTO);

        assertEquals(email, resultDTO.getEmail());
        verify(subscriptionRepository).save(any(Subscription.class));
    }
}
