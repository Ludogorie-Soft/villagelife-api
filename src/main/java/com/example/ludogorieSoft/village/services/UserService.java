package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.UserDTO;
import com.example.ludogorieSoft.village.model.User;
import com.example.ludogorieSoft.village.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserDTO checkUserDTO(UserDTO userDTO) {

        if (!userDTO.getEmail().isEmpty() && !userDTO.getFullName().isEmpty() && !userDTO.getConsent().equals(false)) {
            return saveUser(userDTO);
        } else {
            return null;
        }
    }

    public UserDTO saveUser(UserDTO userDTO) {
        Optional<User> foundUser = userRepository.findByEmail(userDTO.getEmail().trim());
        if (foundUser.isPresent()) {
            return mapUserToUserDTO(foundUser.get());
        } else {
            User user = new User();
            user.setFullName(userDTO.getFullName().trim());
            user.setEmail(userDTO.getEmail().trim());
            user.setConsent(userDTO.getConsent());
            userRepository.save(user);
            return mapUserToUserDTO(user);
        }
    }

    public UserDTO mapUserToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
