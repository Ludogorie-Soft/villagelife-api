package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.PropertyStatsDTO;
import com.example.ludogorieSoft.village.dtos.PropertyUserDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.repositories.PropertyRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PropertyService {
    private PropertyRepository propertyRepository;
    private final ModelMapper modelMapper;
    private VillageService villageService;
    private ImageService imageService;
    public PropertyDTO propertyToPropertyDTO(Property property) {
        PropertyDTO propertyDTO = modelMapper.map(property, PropertyDTO.class);
        propertyDTO.setVillageDTO(villageService.villageToVillageDTO(property.getVillage()));
        PropertyUserDTO propertyUserDTO = modelMapper.map(property.getPropertyUser(), PropertyUserDTO.class);
        propertyDTO.setPropertyUserDTO(propertyUserDTO);
        propertyDTO.setImageUrl(property.getImageUrl());
        PropertyStatsDTO propertyStatsDTO = modelMapper.map(property.getPropertyStats(), PropertyStatsDTO.class);
        propertyDTO.setPropertyStatsDTO(propertyStatsDTO);
        return  propertyDTO;
    }
    public Page<PropertyDTO> getAllPropertiesAndMainImage(int pageNumber, int elementsCount) {
        Pageable page = PageRequest.of(pageNumber, elementsCount);
        Page<Property> properties = propertyRepository.findByDeletedAtIsNullOrderByCreatedAtDesc(page);
        List<PropertyDTO> propertyDTOS = properties.stream().map(property -> {
            PropertyDTO propertyDTO = propertyToPropertyDTO(property);
            addMainImageToPropertyDTO(propertyDTO);
            return propertyDTO;
        }).toList();
        return new PageImpl<>(propertyDTOS, page, properties.getTotalElements());
    }

    public List<PropertyDTO> getAllPropertiesByVillageIdAndMainImage(Long villageId) {
        List<Property> properties = propertyRepository.findByVillageIdAndDeletedAtIsNullOrderByCreatedAtDesc(villageId);
        return addMainImageToPropertyDTOList(properties);
    }
    public List<PropertyDTO> addMainImageToPropertyDTOList(List<Property> properties){
        return properties.stream().map(property -> {
            PropertyDTO propertyDTO = propertyToPropertyDTO(property);
            addMainImageToPropertyDTO(propertyDTO);
            return propertyDTO;
        }).toList();
    }

    public void addMainImageToPropertyDTO(PropertyDTO propertyDTO){
        String imagePath = propertyDTO.getImageUrl();
        if (imagePath != null && !imagePath.equals("")) {
            String base64Image = imageService.getImageFromSpace(imagePath);
            propertyDTO.setImageUrl(base64Image);
        }
    }

    public PropertyDTO getPropertyWithMainImageById(Long id){
        Optional<Property> optionalProperty = propertyRepository.findById(id);
        if (optionalProperty.isEmpty()) {
            throw new ApiRequestException("Property with id: " + id + " Not Found");
        }
        PropertyDTO propertyDTO = propertyToPropertyDTO(optionalProperty.get());
        addMainImageToPropertyDTO(propertyDTO);
        return propertyDTO;
    }
}
