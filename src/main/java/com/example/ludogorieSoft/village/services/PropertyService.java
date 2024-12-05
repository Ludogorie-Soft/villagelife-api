package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.dtos.BusinessCardDTO;
import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.PropertyStatsDTO;
import com.example.ludogorieSoft.village.enums.PropertyCondition;
import com.example.ludogorieSoft.village.enums.Role;
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

import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.UUID.randomUUID;

@Service
@AllArgsConstructor
public class PropertyService {
    private PropertyRepository propertyRepository;
    private final ModelMapper modelMapper;
    private VillageService villageService;
    private ImageService imageService;
    private PropertyImageService propertyImageService;


    public Property propertyDTOToProperty(PropertyDTO propertyDTO) {
        return modelMapper.map(propertyDTO, Property.class);
    }

    public PropertyDTO propertyToPropertyDTO(Property property) {
        PropertyDTO propertyDTO;
        if (property == null) {
            throw new IllegalArgumentException("Property cannot be null");
        }
        propertyDTO = modelMapper.map(property, PropertyDTO.class);
        if (property.getVillage() != null) {
            propertyDTO.setVillageDTO(villageService.villageToVillageDTO(property.getVillage()));
        }
        if (property.getAlternativeUser() != null) {
            AlternativeUserDTO alternativeUserDTO = modelMapper.map(property.getAlternativeUser(), AlternativeUserDTO.class);
            propertyDTO.setAlternativeUserDTO(alternativeUserDTO);

            if (alternativeUserDTO.getRole() != Role.USER && alternativeUserDTO.getRole() != Role.ADMIN &&
                    property.getAlternativeUser().getBusinessCard() != null) {
                BusinessCardDTO businessCardDTO = modelMapper.map(
                        property.getAlternativeUser().getBusinessCard(),
                        BusinessCardDTO.class
                );
                propertyDTO.getAlternativeUserDTO().setBusinessCardDTO(businessCardDTO);
            }
        }
        propertyDTO.setImageUrl(property.getImageUrl());
        if (property.getPropertyStats() != null) {
            PropertyStatsDTO propertyStatsDTO = modelMapper.map(property.getPropertyStats(), PropertyStatsDTO.class);
            propertyDTO.setPropertyStatsDTO(propertyStatsDTO);
        }

        return propertyDTO;
    }

    public Page<PropertyDTO> getAllPropertiesAndMainImage(int pageNumber, int elementsCount) {
        Pageable page = PageRequest.of(pageNumber, elementsCount);
        Page<Property> properties = propertyRepository.findByDeletedAtIsNullOrderByCreatedAtDesc(page);
        List<PropertyDTO> propertyDTOS = properties.stream()
                .filter(Objects::nonNull)
                .map(property -> {
                    PropertyDTO propertyDTO = propertyToPropertyDTO(property);
                    addMainImageToPropertyDTO(propertyDTO);
                    return propertyDTO;
                })
                .toList();
        return new PageImpl<>(propertyDTOS, page, properties.getTotalElements());
    }

    public List<PropertyDTO> getAllPropertiesByVillageIdAndMainImage(Long villageId) {
        List<Property> properties = propertyRepository.findByVillageIdAndDeletedAtIsNullOrderByCreatedAtDesc(villageId);
        return addMainImageToPropertyDTOList(properties);
    }

    public List<PropertyDTO> addMainImageToPropertyDTOList(List<Property> properties) {
        return properties.stream().map(property -> {
            PropertyDTO propertyDTO = propertyToPropertyDTO(property);
            addMainImageToPropertyDTO(propertyDTO);
            return propertyDTO;
        }).toList();
    }

    public void addMainImageToPropertyDTO(PropertyDTO propertyDTO) {
        String imagePath = propertyDTO.getImageUrl();
        if (imagePath != null && !imagePath.equals("")) {
            String base64Image = imageService.getImageFromSpace(imagePath);
            propertyDTO.setImageUrl(base64Image);
        }
    }

    public PropertyDTO getPropertyWithMainImageById(Long id) {
        Property property = getPropertyById(id);
        PropertyDTO propertyDTO = propertyToPropertyDTO(property);
        addMainImageToPropertyDTO(propertyDTO);
        if (property.getAlternativeUser().getBusinessCard() != null) {
            propertyDTO.getAlternativeUserDTO().getBusinessCardDTO().setImageName(imageService.getImageFromSpace(property.getAlternativeUser().getBusinessCard().getImageName()));
        }
        return propertyDTO;
    }

    public Property getPropertyById(Long id) {
        Optional<Property> optionalProperty = propertyRepository.findById(id);
        if (optionalProperty.isEmpty()) {
            throw new ApiRequestException("Property with id: " + id + " Not Found");
        }
        return optionalProperty.get();
    }

    public Page<PropertyDTO> getSearchProperties(List<String> propertyTypes, String propertyTransferType,
                                                 Double minBuiltUpArea, Double maxBuiltUpArea, Double minYardArea,
                                                 Double maxYardArea, Short minRoomsCount, Short maxRoomsCount,
                                                 Short minBathroomsCount, Short maxBathroomsCount, List<String> heating,
                                                 List<String> constructionTypes, List<String> propertyConditions, Short minConstructionYear,
                                                 Short maxConstructionYear, BigDecimal minPrice, BigDecimal maxPrice,
                                                 List<String> ownershipTypes, String villageName, String regionName,
                                                 Pageable pageable) {

        List<PropertyType> propertyTypesValues = mapToPropertyTypeList(propertyTypes);
        PropertyTransferType propertyTransferTypeValue = mapToPropertyTransferType(propertyTransferType);
        List<ConstructionType> constructionTypesValues = mapToConstructionTypeList(constructionTypes);
        List<PropertyCondition> propertyConditionsValues = mapToPropertyConditionList(propertyConditions);
        List<OwnershipType> ownershipTypesValues = mapToOwnershipTypeList(ownershipTypes);

        Page<Property> properties = propertyRepository.searchProperties(
                propertyTypesValues, propertyTransferTypeValue, minBuiltUpArea, maxBuiltUpArea, minYardArea, maxYardArea,
                minRoomsCount, maxRoomsCount, minBathroomsCount, maxBathroomsCount, heating, constructionTypesValues, propertyConditionsValues,
                minConstructionYear != null ? minConstructionYear.toString() : null,
                maxConstructionYear != null ? maxConstructionYear.toString() : null, minPrice, maxPrice, ownershipTypesValues, villageName, regionName, pageable);

        return properties.map(this::propertyToPropertyDTO);
    }

    public void incrementSearchPropertiesSeenInResults(List<String> propertyTypes, String propertyTransferType,
                                                 Double minBuiltUpArea, Double maxBuiltUpArea, Double minYardArea,
                                                 Double maxYardArea, Short minRoomsCount, Short maxRoomsCount,
                                                 Short minBathroomsCount, Short maxBathroomsCount, List<String> heating,
                                                 List<String> constructionTypes, List<String> propertyConditions, Short minConstructionYear,
                                                 Short maxConstructionYear, BigDecimal minPrice, BigDecimal maxPrice,
                                                 List<String> ownershipTypes, String villageName, String regionName) {

        List<PropertyType> propertyTypesValues = mapToPropertyTypeList(propertyTypes);
        PropertyTransferType propertyTransferTypeValue = mapToPropertyTransferType(propertyTransferType);
        List<ConstructionType> constructionTypesValues = mapToConstructionTypeList(constructionTypes);
        List<PropertyCondition> propertyConditionsValues = mapToPropertyConditionList(propertyConditions);
        List<OwnershipType> ownershipTypesValues = mapToOwnershipTypeList(ownershipTypes);

        propertyRepository.updateSeenInResultsForFilteredProperties(
                propertyTypesValues, propertyTransferTypeValue, minBuiltUpArea, maxBuiltUpArea, minYardArea, maxYardArea,
                minRoomsCount, maxRoomsCount, minBathroomsCount, maxBathroomsCount, heating, constructionTypesValues, propertyConditionsValues,
                minConstructionYear != null ? minConstructionYear.toString() : null,
                maxConstructionYear != null ? maxConstructionYear.toString() : null, minPrice, maxPrice, ownershipTypesValues, villageName, regionName);
    }

    private List<PropertyType> mapToPropertyTypeList(List<String> propertyTypes) {
        if (propertyTypes == null) return null;
        return propertyTypes.stream()
                .map(type -> {
                    try {
                        return PropertyType.valueOf(type);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private PropertyTransferType mapToPropertyTransferType(String propertyTransferType) {
        if (propertyTransferType == null) return null;
        try {
            return PropertyTransferType.valueOf(propertyTransferType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private List<ConstructionType> mapToConstructionTypeList(List<String> constructionTypes) {
        if (constructionTypes == null) return null;
        return constructionTypes.stream()
                .map(type -> {
                    try {
                        return ConstructionType.valueOf(type);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private List<PropertyCondition> mapToPropertyConditionList(List<String> propertyConditions) {
        if (propertyConditions == null) return null;
        return propertyConditions.stream()
                .map(type -> {
                    try {
                        return PropertyCondition.valueOf(type);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private List<OwnershipType> mapToOwnershipTypeList(List<String> ownershipTypes) {
        if (ownershipTypes == null) return null;
        return ownershipTypes.stream()
                .map(type -> {
                    try {
                        return OwnershipType.valueOf(type);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public PropertyDTO createProperty(PropertyDTO propertyDTO) {
        Property property = propertyDTOToProperty(propertyDTO);
        String imageUUID = randomUUID().toString();
        String imageName = imageService.uploadImage(propertyDTO.getMainImageBytes(), imageUUID);
        property.setImageUrl(imageName);
        List<String> heatingTypes = new ArrayList<>();
        if (propertyDTO.getHeating() != null) {
            heatingTypes.addAll(propertyDTO.getHeating());
        }
        if (propertyDTO.getHeatingText() != null && !propertyDTO.getHeatingText().trim().isEmpty()) {
            List<String> additionalHeating = splitHeatingText(propertyDTO.getHeatingText());
            heatingTypes.addAll(additionalHeating);
        }
        property.setHeating(heatingTypes);
        Property savedProperty = propertyRepository.save(property);
        propertyImageService.createPropertyImage(propertyDTO.getImages(), savedProperty);
        return modelMapper.map(savedProperty, PropertyDTO.class);
    }

    protected List<String> splitHeatingText(String heatingText) {
        if (heatingText == null || heatingText.trim().isEmpty()) {
            return new ArrayList<>();
        }
        Pattern pattern = Pattern.compile("[^;,\\s]{1,100}");
        Matcher matcher = pattern.matcher(heatingText);

        List<String> heatingTypes = new ArrayList<>();
        while (matcher.find()) {
            heatingTypes.add(matcher.group().trim());
        }
        return heatingTypes;
    }


}



