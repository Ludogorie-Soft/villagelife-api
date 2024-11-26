package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyStatsDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.PropertyStats;
import com.example.ludogorieSoft.village.repositories.PropertyStatsRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PropertyStatsService {
    private final PropertyStatsRepository propertyStatsRepository;
    private final ModelMapper modelMapper;
    private final PropertyService propertyService;

    public PropertyStatsDTO propertyStatsToPropertyStatsDTO(PropertyStats propertyStats) {
        return modelMapper.map(propertyStats, PropertyStatsDTO.class);
    }

    public PropertyStats propertyStatsDTOToPropertyStats(PropertyStatsDTO propertyStatsDTO) {
        return modelMapper.map(propertyStatsDTO, PropertyStats.class);
    }

    //да се използва в createProperty
    public PropertyStatsDTO createPropertyStats(PropertyStatsDTO propertyStatsDTO) {
        checkPropertyStatsValidations(propertyStatsDTO);
        PropertyStats propertyStats = propertyStatsRepository.save(propertyStatsDTOToPropertyStats(propertyStatsDTO));
        return propertyStatsToPropertyStatsDTO(propertyStats);
    }

    protected void checkPropertyStatsValidations(PropertyStatsDTO propertyStatsDTO) {
        if(propertyStatsDTO.getSeenInResults() < 0){
            throw new ApiRequestException("Seen in results cannot be less than 0!");
        }
        if(propertyStatsDTO.getViews() < 0){
            throw new ApiRequestException("Views cannot be less than 0!");
        }
        if(propertyStatsDTO.getShares() < 0){
            throw new ApiRequestException("Shares cannot be less than 0!");
        }
        if(propertyStatsDTO.getSaves() < 0){
            throw new ApiRequestException("Saves cannot be less than 0!");
        }
    }

    public PropertyStatsDTO incrementViewsByPropertyId(Long propertyId) {
        Property property = propertyService.getPropertyById(propertyId);
        propertyStatsRepository.incrementViewsForProperty(property);
        return propertyStatsToPropertyStatsDTO(property.getPropertyStats());
    }

    public void incrementSavesForProperty(Property property){
        propertyStatsRepository.incrementSavesForProperty(property);
    }

    public void decrementSavesForProperty(Property property){
        propertyStatsRepository.decrementSavesForProperty(property);
    }

    public List<PropertyStatsDTO> incrementSeenInResultsByPropertyId(List<Property> properties) {
        propertyStatsRepository.incrementSeenInResultsForProperties(properties);
        return properties
                .stream()
                .map(property -> propertyStatsToPropertyStatsDTO(property.getPropertyStats()))
                .toList();
    }
}
