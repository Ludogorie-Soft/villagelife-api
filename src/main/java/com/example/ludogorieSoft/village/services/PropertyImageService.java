package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.*;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.PropertyImage;
import com.example.ludogorieSoft.village.repositories.PropertyImageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PropertyImageService {
    private final PropertyImageRepository propertyImageRepository;
    private final ModelMapper modelMapper;
    private final ImageService imageService;

    public PropertyImageDTO propertyImageToPropertyImageDTO(PropertyImage propertyImage) {

        return modelMapper.map(propertyImage, PropertyImageDTO.class);
    }
    public PropertyImage propertyImageDTOToPropertyImage(PropertyImageDTO propertyImageDTO) {

        return modelMapper.map(propertyImageDTO, PropertyImage.class);
    }
    public List<PropertyImageDTO> createPropertyImage(List<PropertyImageDTO> propertyImageDTOs, Property property){
        List<PropertyImage> savedPropertyImages = new ArrayList<>();
        for (PropertyImageDTO propertyImageDTO : propertyImageDTOs) {
            PropertyImage propertyImage = propertyImageDTOToPropertyImage(propertyImageDTO);
            byte[] imageData = propertyImageDTO.getPropertyImageBytes();
            String randomUuid = UUID.randomUUID().toString();
            propertyImage.setImageName(randomUuid);
            propertyImage.setProperty(property);
            savedPropertyImages.add(propertyImage);
            imageService.uploadImage(imageData, propertyImage.getImageName());
            propertyImageRepository.save(propertyImage);
        }

        return savedPropertyImages.stream()
                .map(this::propertyImageToPropertyImageDTO)
                .toList();
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
