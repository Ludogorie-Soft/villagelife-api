package com.example.ludogorieSoft.village.services;


import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.PropertyUserDTO;
import com.example.ludogorieSoft.village.model.Property;
import com.example.ludogorieSoft.village.model.PropertyUser;
import com.example.ludogorieSoft.village.repositories.PropertyUserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyUserService {
    private final PropertyUserRepository propertyUserRepository;
    private final ModelMapper modelMapper;
    public PropertyUserDTO propertyUserToPropertyUserDTO(PropertyUser propertyUser) {

        return modelMapper.map(propertyUser, PropertyUserDTO.class);
    }
    public PropertyUser propertyUserDTOToPropertyUser(PropertyUserDTO propertyUserDTO) {

        return modelMapper.map(propertyUserDTO, PropertyUser.class);
    }
    public PropertyUserDTO createPropertyUser(PropertyUserDTO propertyUserDTO){
        PropertyUser propertyUser = propertyUserDTOToPropertyUser(propertyUserDTO);
        PropertyUser savedPropertyUser = propertyUserRepository.save(propertyUser);
        return modelMapper.map(savedPropertyUser, PropertyUserDTO.class);
    }

}
