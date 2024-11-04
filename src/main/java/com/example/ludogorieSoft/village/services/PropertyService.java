package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.PropertyStatsDTO;
import com.example.ludogorieSoft.village.enums.ConstructionType;
import com.example.ludogorieSoft.village.enums.OwnershipType;
import com.example.ludogorieSoft.village.enums.PropertyTransferType;
import com.example.ludogorieSoft.village.enums.PropertyType;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        AlternativeUserDTO alternativeUserDTO = modelMapper.map(property.getAlternativeUser(), AlternativeUserDTO.class);
        propertyDTO.setAlternativeUserDTO(alternativeUserDTO);
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

    public Page<PropertyDTO> getSearchProperties(List<String> propertyTypes, String propertyTransferType,
                                                 Double minBuiltUpArea, Double maxBuiltUpArea, Double minYardArea,
                                                 Double maxYardArea, Short minRoomsCount, Short maxRoomsCount,
                                                 Short minBathroomsCount, Short maxBathroomsCount, List<String> heating,
                                                 List<String> constructionTypes, Short minConstructionYear,
                                                 Short maxConstructionYear, BigDecimal minPrice, BigDecimal maxPrice,
                                                 List<String> ownershipTypes, String villageName, String regionName,
                                                 Pageable pageable) {
        List<PropertyType> propertyTypesValues = null;
        if (propertyTypes != null) propertyTypesValues =
                propertyTypes.stream().map(PropertyType::valueOf).collect(Collectors.toList());
        PropertyTransferType propertyTransferTypeValue =
                propertyTransferType != null ? PropertyTransferType.valueOf(propertyTransferType) : null;

        List<ConstructionType> constructionTypesValues = null;
        if (constructionTypes != null) constructionTypesValues = constructionTypes.stream()
                .map(ConstructionType::valueOf).collect(Collectors.toList());

        List<OwnershipType> ownershipTypesValues = null;
        if (ownershipTypes != null) ownershipTypesValues = ownershipTypes.stream()
                .map(OwnershipType::valueOf).collect(Collectors.toList());

        Page<Property> properties = propertyRepository.searchProperties(propertyTypesValues, propertyTransferTypeValue,
                minBuiltUpArea, maxBuiltUpArea, minYardArea, maxYardArea, minRoomsCount, maxRoomsCount, minBathroomsCount,
                maxBathroomsCount, heating, constructionTypesValues, minConstructionYear, maxConstructionYear, minPrice,
                maxPrice, ownershipTypesValues, villageName, regionName, pageable);
        return properties.map(this::propertyToPropertyDTO);
    }
}
