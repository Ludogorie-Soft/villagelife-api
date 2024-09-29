package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
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

@Service
@AllArgsConstructor
public class PropertyService {
    private PropertyRepository propertyRepository;
    private final ModelMapper modelMapper;
    private VillageService villageService;
    private ImageService imageService;

    public PropertyDTO propertyToPropertyDTO(Property property) {
        return modelMapper.map(property, PropertyDTO.class);
    }

    public Property propertyDTOtoProperty(PropertyDTO propertyDTO) {
        return modelMapper.map(propertyDTO, Property.class);
    }
    private PropertyDTO propertyToPropertyDTO2(Property property) {
        PropertyDTO propertyDTO = propertyToPropertyDTO(property);
        propertyDTO.setVillageDTO(villageService.villageToVillageDTO(property.getVillage()));
        propertyDTO.setPropertyUserDTO(null);
        propertyDTO.setImageUrl(property.getImageUrl());
        propertyDTO.setPropertyStatsDTO(null);
        return  propertyDTO;
    }
    public Page<PropertyDTO> getAllPropertiesAndMainImage(int pageNumber, int elementsCount) {
        Pageable page = PageRequest.of(pageNumber, elementsCount);
        Page<Property> properties = propertyRepository.findByDeletedAtIsNullOrderByCreatedAtDesc(page);

        List<PropertyDTO> propertyDTOS = properties.stream().map(property -> {
            PropertyDTO propertyDTO = propertyToPropertyDTO2(property);
            String imagePath = property.getImageUrl();
            if (imagePath != null && !imagePath.equals("")) {
                String base64Image = imageService.getImageFromSpace(imagePath);
                propertyDTO.setImageUrl(base64Image);
            }
            return propertyDTO;
        }).toList();

        return new PageImpl<>(propertyDTOS, page, properties.getTotalElements());
    }
}
