package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.UserSavedPropertyDTO;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.UserSavedProperty;
import com.example.ludogorieSoft.village.repositories.UserSavedPropertyRepository;
import com.example.ludogorieSoft.village.utils.TimestampUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserSavedPropertyService {
    private final UserSavedPropertyRepository userSavedPropertyRepository;
    private final AdministratorService administratorService;
    private final PropertyService propertyService;

    public UserSavedPropertyDTO userSavedPropertyToUserSavedPropertyDTO(UserSavedProperty userSavedProperty) {
        return new UserSavedPropertyDTO(userSavedProperty.getId(),
                administratorService.administratorToAdministratorDTO(userSavedProperty.getUser()),
                propertyService.propertyToPropertyDTO(userSavedProperty.getProperty()),
                userSavedProperty.getDeletedAt());
    }

    public UserSavedPropertyDTO createUserSavedProperty(UserSavedPropertyDTO userSavedPropertyDTO) {
        AlternativeUser alternativeUser = administratorService.administratorDTOToAdministrator(userSavedPropertyDTO.getUserDTO());
        Property property = propertyService.propertyDTOToProperty(userSavedPropertyDTO.getPropertyDTO());

        UserSavedProperty userSavedProperty = new UserSavedProperty(null, alternativeUser, property, null);
        return userSavedPropertyToUserSavedPropertyDTO(userSavedPropertyRepository.save(userSavedProperty));
    }

    public List<UserSavedPropertyDTO> getAllUserSavedProperties() {
        List<UserSavedProperty> userSavedProperties = userSavedPropertyRepository.findAll();
        return userSavedProperties
                .stream()
                .map(this::userSavedPropertyToUserSavedPropertyDTO)
                .toList();
    }

    public Boolean isPropertySavedByPropertyIdAndAlternativeUserId(Long propertyId, Long userId) {
        return userSavedPropertyRepository.existsByPropertyIdAndUserId(propertyId, userId);
    }

    public UserSavedPropertyDTO toggleUserSavedProperty(Long propertyId, Long userId) {
        UserSavedProperty existingEntry = userSavedPropertyRepository.findByPropertyIdAndUserId(propertyId, userId);
        if (existingEntry == null) {
            AlternativeUser alternativeUser = administratorService.administratorDTOToAdministrator(administratorService.getAdministratorById(userId));
            Property property = propertyService.getPropertyById(propertyId);
            existingEntry = new UserSavedProperty(null, alternativeUser, property, null);
        } else {
            if (existingEntry.getDeletedAt() == null){
                existingEntry.setDeletedAt(TimestampUtils.getCurrentTimestamp());
            } else{
                existingEntry.setDeletedAt(null);
            }
        }
        return userSavedPropertyToUserSavedPropertyDTO(userSavedPropertyRepository.save(existingEntry));
    }
}
