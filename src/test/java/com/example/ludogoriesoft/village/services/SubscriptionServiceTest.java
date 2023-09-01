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
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {
    @InjectMocks
    SubscriptionService subscriptionService;
    @Mock
    SubscriptionRepository subscriptionRepository;
    @Mock
    ModelMapper modelMapper;

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

    @Test
    void testSubscriptionToSubscriptionDTO() {
        Subscription subscription = new Subscription(1L, "test@example.com", null);
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO(1L, "test@example.com", null);
        when(modelMapper.map(subscription, SubscriptionDTO.class)).thenReturn(subscriptionDTO);
        SubscriptionDTO resultDTO = subscriptionService.subscriptionToSubscriptionDTO(subscription);
        assertEquals(subscription.getId(), resultDTO.getId());
    }

    @Test
    void testGetAllSubscriptions() {
        Subscription subscription1 = new Subscription();
        subscription1.setId(1L);
        subscription1.setEmail("test1@example.com");
        subscription1.setDeletedAt(null);

        Subscription subscription2 = new Subscription();
        subscription2.setId(2L);
        subscription2.setEmail("test2@example.com");
        subscription2.setDeletedAt(null);

        List<Subscription> subscriptionList = new ArrayList<>();
        subscriptionList.add(subscription1);
        subscriptionList.add(subscription2);

        when(subscriptionRepository.findAll()).thenReturn(subscriptionList);

        SubscriptionDTO subscriptionDTO1 = new SubscriptionDTO();
        subscriptionDTO1.setId(subscription1.getId());
        subscriptionDTO1.setEmail(subscription1.getEmail());
        subscriptionDTO1.setDeletedAt(subscription1.getDeletedAt());

        SubscriptionDTO subscriptionDTO2 = new SubscriptionDTO();
        subscriptionDTO2.setId(subscription2.getId());
        subscriptionDTO2.setEmail(subscription2.getEmail());
        subscriptionDTO2.setDeletedAt(subscription2.getDeletedAt());

        when(modelMapper.map(subscription1, SubscriptionDTO.class)).thenReturn(subscriptionDTO1);
        when(modelMapper.map(subscription2, SubscriptionDTO.class)).thenReturn(subscriptionDTO2);

        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository, modelMapper);
        List<SubscriptionDTO> subscriptionDTOList = subscriptionService.getAllSubscriptions();

        assertEquals(subscriptionList.size(), subscriptionDTOList.size());

        SubscriptionDTO expectedSubscriptionDTO1 = subscriptionDTOList.get(0);
        assertEquals(subscriptionDTO1.getId(), expectedSubscriptionDTO1.getId());
        assertEquals(subscriptionDTO1.getEmail(), expectedSubscriptionDTO1.getEmail());
        assertEquals(subscriptionDTO1.getDeletedAt(), expectedSubscriptionDTO1.getDeletedAt());

        SubscriptionDTO expectedSubscriptionDTO2 = subscriptionDTOList.get(1);
        assertEquals(subscriptionDTO2.getId(), expectedSubscriptionDTO2.getId());
        assertEquals(subscriptionDTO2.getEmail(), expectedSubscriptionDTO2.getEmail());
        assertEquals(subscriptionDTO2.getDeletedAt(), expectedSubscriptionDTO2.getDeletedAt());
    }

    @Test
    void testEmailExistsWhenEmailExists() {
        String email = "test@example.com";
        Subscription existingSubscription = new Subscription();
        existingSubscription.setEmail(email);

        when(subscriptionRepository.findByEmail(email)).thenReturn(existingSubscription);
        boolean result = subscriptionService.emailExists(email);
        assertTrue(result);
    }

    @Test
    void testEmailExistsWhenEmailDoesNotExist() {
        String email = "nonexistent@example.com";

        when(subscriptionRepository.findByEmail(email)).thenReturn(null);
        boolean result = subscriptionService.emailExists(email);
        assertFalse(result);
    }
}
