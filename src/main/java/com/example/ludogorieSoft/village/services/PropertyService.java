package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.RegionDTO;
import com.example.ludogorieSoft.village.dtos.VillageDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.Village;
import com.example.ludogorieSoft.village.repositories.PropertyRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final ModelMapper modelMapper;
    public PropertyDTO propertyToPropertyDTO(Property property) {

        return modelMapper.map(property, PropertyDTO.class);
    }
    public Property propertyDTOToProperty(PropertyDTO propertyDTO) {

        return modelMapper.map(propertyDTO, Property.class);
    }
    public PropertyDTO createProperty(PropertyDTO propertyDTO){
      Property property = propertyDTOToProperty(propertyDTO);
      Property savedProperty = propertyRepository.save(property);
      return modelMapper.map(savedProperty, PropertyDTO.class);
    }

}
