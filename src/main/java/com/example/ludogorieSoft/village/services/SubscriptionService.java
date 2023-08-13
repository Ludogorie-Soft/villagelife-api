package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.SubscriptionDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Subscription;
import com.example.ludogorieSoft.village.repositories.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO) {
        if (StringUtils.isBlank(subscriptionDTO.getEmail())) {
            throw new ApiRequestException("Email is blank");
        }
        Subscription subscription = subscriptionRepository.findByEmail(subscriptionDTO.getEmail());
        if(subscription != null && subscription.getDeletedAt() != null){
            subscription.setDeletedAt(null);
            subscriptionRepository.save(subscription);
        }else if(subscription == null){
            subscription = new Subscription();
            subscription.setEmail(subscriptionDTO.getEmail());
            subscriptionRepository.save(subscription);
        }
        return subscriptionDTO;
    }
}
