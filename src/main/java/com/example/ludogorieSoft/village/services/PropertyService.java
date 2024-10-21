package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.PropertyStats;
import com.example.ludogorieSoft.village.repositories.PropertyRepository;
import com.example.ludogorieSoft.village.repositories.PropertyStatsRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
//    private void checkPropertyValidations(PropertyDTO propertyDTO) {
//        if (propertyDTO.getPrice() == null) throw new ApiRequestException("Price is required!");
//        if (propertyDTO.getPrice().compareTo(BigDecimal.ZERO) < 0)
//            throw new ApiRequestException("The price ust be greater than or equal to 0!");
//        if (propertyDTO.getPhoneNumber() == null) throw new ApiRequestException("Phone number is required!");
//        if (propertyDTO.getPhoneNumber().trim().length() < 10)
//            throw new ApiRequestException("Phone number should be at least 10 characters long!");
//        if (propertyDTO.getBuildUpArea() == 0) throw new ApiRequestException("Build up area is required!");
//        if (propertyDTO.getBuildUpArea() < 0)
//            throw new ApiRequestException("The build up area must be greater than or equal to 0!");
//        if (propertyDTO.getYardArea() == 0) throw new ApiRequestException("Yard area is required!");
//        if (propertyDTO.getYardArea() < 0)
//            throw new ApiRequestException("The yard area must be greater than or equal to 0!");
//        if (propertyDTO.getRoomsCount() == 0) throw new ApiRequestException("Rooms count is required!");
//        if (propertyDTO.getRoomsCount() < 0)
//            throw new ApiRequestException("The number of the rooms must be greater than or equal to 0!");
//        if (propertyDTO.getBathroomsCount() == 0) throw new ApiRequestException("Bathrooms count is required!");
//        if (propertyDTO.getBathroomsCount() < 0)
//            throw new ApiRequestException("The number of the bathrooms must be greater than or equal to 0!");
//    }


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
        List<PropertyDTO> propertyDTOS = addMainImageToPropertyDTOList(properties.stream().toList());
        return new PageImpl<>(propertyDTOS, page, properties.getTotalElements());
    }

    public List<PropertyDTO> getAllPropertiesByVillageIdAndMainImage(Long villageId) {
        List<Property> properties = propertyRepository.findByVillageIdAndDeletedAtIsNullOrderByCreatedAtDesc(villageId);
        List<PropertyDTO> propertyDTOS =  addMainImageToPropertyDTOList(properties);
        return propertyDTOS;
    }
    private List<PropertyDTO> addMainImageToPropertyDTOList(List<Property> properties){
        return properties.stream().map(property -> {
            PropertyDTO propertyDTO = propertyToPropertyDTO2(property);
            String imagePath = property.getImageUrl();
            if (imagePath != null && !imagePath.equals("")) {
                String base64Image = imageService.getImageFromSpace(imagePath);
                propertyDTO.setImageUrl(base64Image);
            }
            return propertyDTO;
        }).toList();
    }
    public PropertyDTO createProperty(PropertyDTO propertyDTO){
//        checkPropertyValidations(propertyDTO);
      Property property = propertyDTOToProperty(propertyDTO);
      String imageUUID = randomUUID().toString();
      String imageName = imageService.uploadImage(propertyDTO.getMainImageBytes(),imageUUID);
      property.setImageUrl(imageName);
      property.setHeating(splitHeatingText(propertyDTO.getHeatingText()));
      Property savedProperty = propertyRepository.save(property);
      propertyImageService.createPropertyImage(propertyDTO.getImages(), savedProperty);
      return modelMapper.map(savedProperty, PropertyDTO.class);
    }

    private List<String> splitHeatingText(String heatingText){
        List<String> heatingTypes = List.of(heatingText.split("\\s*[;,]\\s*"));
        List<String> heatingTypesWithoutSpace = new ArrayList<>();
        for (String heatingType : heatingTypes) {
            heatingTypesWithoutSpace.add(heatingType.trim());
        }
        return heatingTypesWithoutSpace;
    }

}
