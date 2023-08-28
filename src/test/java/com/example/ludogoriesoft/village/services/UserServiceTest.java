package com.example.ludogorieSoft.village.services;


import com.example.ludogorieSoft.village.dtos.UserDTO;
import com.example.ludogorieSoft.village.model.User;
import com.example.ludogorieSoft.village.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        modelMapper = Mockito.mock(ModelMapper.class);
        userService = new UserService(userRepository, modelMapper);
    }

    @Test
    void testCheckUserDTO_SaveUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setFullName("John Doe");
        userDTO.setConsent(true);

        User foundUser = new User();
        foundUser.setEmail("test@example.com");
        foundUser.setFullName("John Doe");
        foundUser.setConsent(true);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(foundUser));
        when(userService.mapUserToUserDTO(foundUser)).thenReturn(userDTO);
        when(userService.saveUser(userDTO)).thenReturn(userDTO);

        UserDTO result = userService.checkUserDTO(userDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("John Doe", result.getFullName());
        assertTrue(result.getConsent());
    }

    @Test
    void testCheckUserDTO_Null() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setFullName("John Doe");
        userDTO.setConsent(false);

        UserDTO result = userService.checkUserDTO(userDTO);

        assertNull(result);
    }

    @Test
    void testSaveUser_FoundUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setFullName("John Doe");
        userDTO.setConsent(true);

        User foundUser = new User();
        foundUser.setEmail("test@example.com");
        foundUser.setFullName("John Doe");
        foundUser.setConsent(true);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(foundUser));
        when(userService.mapUserToUserDTO(foundUser)).thenReturn(userDTO);

        UserDTO result = userService.saveUser(userDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("John Doe", result.getFullName());
        assertTrue(result.getConsent());
    }
}
