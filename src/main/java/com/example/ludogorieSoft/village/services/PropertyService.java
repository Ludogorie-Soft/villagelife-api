package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
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
import java.util.List;
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

    public PropertyDTO propertyToPropertyDTO(Property property) {

        return modelMapper.map(property, PropertyDTO.class);
    }

    public Property propertyDTOToProperty(PropertyDTO propertyDTO) {

        return modelMapper.map(propertyDTO, Property.class);
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
        Optional<Property> optionalProperty = propertyRepository.findById(id);
        if (optionalProperty.isEmpty()) {
            throw new ApiRequestException("Property with id: " + id + " Not Found");
        }
        PropertyDTO propertyDTO = propertyToPropertyDTO(optionalProperty.get());
        addMainImageToPropertyDTO(propertyDTO);
        return propertyDTO;
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


