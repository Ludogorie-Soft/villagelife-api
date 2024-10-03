package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyImageDTO;
import com.example.ludogorieSoft.village.dtos.PropertyUserDTO;
import com.example.ludogorieSoft.village.model.PropertyImage;
import com.example.ludogorieSoft.village.model.PropertyUser;
import com.example.ludogorieSoft.village.repositories.PropertyImageRepository;
import com.example.ludogorieSoft.village.repositories.PropertyUserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyImageService {
    private final PropertyImageRepository propertyImageRepository;
    private final ModelMapper modelMapper;
    public PropertyImageDTO propertyImageToPropertyImageDTO(PropertyImage propertyImage) {

        return modelMapper.map(propertyImage, PropertyImageDTO.class);
    }
    public PropertyImage propertyImageDTOToPropertyImage(PropertyImageDTO propertyImageDTO) {

        return modelMapper.map(propertyImageDTO, PropertyImage.class);
    }
    public PropertyImageDTO createPropertyImage(PropertyImageDTO propertyImageDTO){
        PropertyImage propertyImage = propertyImageDTOToPropertyImage(propertyImageDTO);
        PropertyImage savedPropertyImage = propertyImageRepository.save(propertyImage);
        return modelMapper.map(savedPropertyImage, PropertyImageDTO.class);
    }
}
