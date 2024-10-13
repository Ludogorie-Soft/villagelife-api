package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.PropertyImageDTO;
import com.example.ludogorieSoft.village.dtos.PropertyUserDTO;
import com.example.ludogorieSoft.village.dtos.UserDTO;
import com.example.ludogorieSoft.village.model.PropertyImage;
import com.example.ludogorieSoft.village.model.PropertyUser;
import com.example.ludogorieSoft.village.repositories.PropertyImageRepository;
import com.example.ludogorieSoft.village.repositories.PropertyUserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

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
    public List<PropertyImageDTO> createPropertyImage(List<PropertyImageDTO> propertyImageDTOs){
        List<PropertyImage> savedPropertyImages = new ArrayList<>();
        for (int i = 0; i < propertyImageDTOs.size(); i++) {
            PropertyImage propertyImage = propertyImageDTOToPropertyImage(propertyImageDTOs.get(i));
            byte[] imageData = propertyImageDTOs.get(i).getPropertyImageBytes();
            String randomUuid = UUID.randomUUID().toString();
            propertyImage.setImageName(randomUuid);
            savedPropertyImages.add(propertyImage);
            imageService.uploadImage(imageData, propertyImage.getImageName());
            propertyImageRepository.save(propertyImage);
        }

        return savedPropertyImages.stream()
                .map(this::propertyImageToPropertyImageDTO)
                .toList();
   }

}
