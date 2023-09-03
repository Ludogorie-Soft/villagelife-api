package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.SubscriptionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Subscription;
import com.example.ludogorieSoft.village.repositories.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final ModelMapper modelMapper;

    public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO) {
        if (StringUtils.isBlank(subscriptionDTO.getEmail())) {
            throw new ApiRequestException("Email is blank");
        }
        Subscription subscription = subscriptionRepository.findByEmail(subscriptionDTO.getEmail());
        if (subscription != null && subscription.getDeletedAt() != null) {
            subscription.setDeletedAt(null);
            subscriptionRepository.save(subscription);
        } else if (subscription == null) {
            subscription = new Subscription();
            subscription.setEmail(subscriptionDTO.getEmail());
            subscriptionRepository.save(subscription);
        }
        return subscriptionDTO;
    }

    public SubscriptionDTO subscriptionToSubscriptionDTO(Subscription subscription) {
        return modelMapper.map(subscription, SubscriptionDTO.class);
    }

    public List<SubscriptionDTO> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptions
                .stream()
                .map(this::subscriptionToSubscriptionDTO)
                .toList();
    }

    public boolean emailExists(String email) {
        Subscription existingSubscription = subscriptionRepository.findByEmail(email);
        return existingSubscription != null && existingSubscription.getDeletedAt() == null;
    }

}
