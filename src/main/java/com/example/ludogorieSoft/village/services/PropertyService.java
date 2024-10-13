package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyDTO;
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

import java.util.List;

import static java.util.UUID.randomUUID;

@Service
@AllArgsConstructor
public class PropertyService {
    private PropertyRepository propertyRepository;
    private final ModelMapper modelMapper;
    private VillageService villageService;
    private ImageService imageService;
    private PropertyStatsRepository propertyStatsRepository;

    public PropertyDTO propertyToPropertyDTO(Property property) {

        return modelMapper.map(property, PropertyDTO.class);
    }
    public Property propertyDTOToProperty(PropertyDTO propertyDTO) {

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
      Property property = propertyDTOToProperty(propertyDTO);
      String imageUUID = randomUUID().toString();
      String imageName = imageService.uploadImage(propertyDTO.getMainImageBytes(),imageUUID);
      property.setImageUrl(imageName);
      Property savedProperty = propertyRepository.save(property);
      return modelMapper.map(savedProperty, PropertyDTO.class);
    }
//    public void initializeStats(Property property) {
//        if (property.getPropertyStats() == null) {
//            PropertyStats stats = new PropertyStats();
//            stats.setSeenInResults(0L);
//            stats.setViews(0);
//            stats.setShares(0);
//            stats.setSaves(0);
//            property.setPropertyStats(stats);
//            propertyStatsRepository.save(stats);
//        }
//    }
//    public void incrementViews(Property property) {
//        initializeStats(property);
//        PropertyStats stats = property.getPropertyStats();
//        stats.setViews(stats.getViews() + 1);
//        propertyStatsRepository.save(stats);
//    }
//    public void incrementShares(Property property) {
//        initializeStats(property);
//        PropertyStats stats = property.getPropertyStats();
//        stats.setShares(stats.getShares() + 1);
//        propertyStatsRepository.save(stats);
//    }
//    public void incrementSaves(Property property) {
//        initializeStats(property);
//        PropertyStats stats = property.getPropertyStats();
//        stats.setSaves(stats.getSaves() + 1);
//        propertyStatsRepository.save(stats);
//    }

}
