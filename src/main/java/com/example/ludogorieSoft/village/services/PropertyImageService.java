package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyImageDTO;
import com.example.ludogorieSoft.village.model.PropertyImage;
import com.example.ludogorieSoft.village.repositories.PropertyImageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PropertyImageService {
    private final ModelMapper modelMapper;
    private PropertyImageRepository propertyImageRepository;
    private ImageService imageService;

    public PropertyImageDTO propertyImageToPropertyImageDTO(PropertyImage propertyImage) {
        return modelMapper.map(propertyImage, PropertyImageDTO.class);
    }

    public PropertyImage propertyImageDTOtoPropertyImage(PropertyImageDTO propertyImageDTO) {
        return modelMapper.map(propertyImageDTO, PropertyImage.class);
    }

    public List<PropertyImageDTO> getPropertyImagesByPropertyId(Long propertyId) {
        return propertyImageRepository.findByProperty_VillageIdAndDeletedAtIsNull(propertyId)
                .stream()
                .map(propertyImage -> {
                    propertyImage.setImageName(imageService.getImageFromSpace(propertyImage.getImageName()));
                    return propertyImageToPropertyImageDTO(propertyImage);
                }).toList();
    }
}
