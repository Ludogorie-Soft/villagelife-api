package com.example.ludogorieSoft.village.controllers;

import com.example.ludogorieSoft.village.dtos.AlternativeUserDTO;
import com.example.ludogorieSoft.village.dtos.PropertyDTO;
import com.example.ludogorieSoft.village.dtos.UserSavedPropertyDTO;
import com.example.ludogorieSoft.village.enums.Role;
import com.example.ludogorieSoft.village.exeptions.handler.ApiExceptionHandler;
import com.example.ludogorieSoft.village.services.UserSavedPropertyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = UserSavedPropertyController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = UserSavedPropertyController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = ApiExceptionHandler.class
                )
        }
)
class UserSavedPropertyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSavedPropertyService userSavedPropertyService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserSavedPropertyDTO userSavedPropertyDTO;

    @BeforeEach
    public void setup() {
        AlternativeUserDTO userDTO = new AlternativeUserDTO();
        userDTO.setRole(Role.USER);

        PropertyDTO propertyDTO = new PropertyDTO();

        userSavedPropertyDTO = new UserSavedPropertyDTO();
        userSavedPropertyDTO.setId(1L);
        userSavedPropertyDTO.setUserDTO(userDTO);
        userSavedPropertyDTO.setPropertyDTO(propertyDTO);
        userSavedPropertyDTO.setDeletedAt(LocalDateTime.now());
    }

    @Test
    void testCreateUserSavedProperty() throws Exception {
        when(userSavedPropertyService.createUserSavedProperty(any(UserSavedPropertyDTO.class))).thenReturn(userSavedPropertyDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user-saved-properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSavedPropertyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userDTO.role").value("USER"))
                .andExpect(jsonPath("$.propertyDTO").exists())
                .andExpect(jsonPath("$.deletedAt").exists())
                .andReturn();

        verify(userSavedPropertyService, times(1)).createUserSavedProperty(any(UserSavedPropertyDTO.class));
    }

    @Test
    void testGetAllUserSavedProperties() throws Exception {
        UserSavedPropertyDTO userSavedPropertyDTO1 = new UserSavedPropertyDTO();
        userSavedPropertyDTO1.setId(1L);
        userSavedPropertyDTO1.setUserDTO(new AlternativeUserDTO());
        userSavedPropertyDTO1.setPropertyDTO(new PropertyDTO());
        userSavedPropertyDTO1.setDeletedAt(LocalDateTime.now());

        UserSavedPropertyDTO userSavedPropertyDTO2 = new UserSavedPropertyDTO();
        userSavedPropertyDTO2.setId(2L);
        userSavedPropertyDTO2.setUserDTO(new AlternativeUserDTO());
        userSavedPropertyDTO2.setPropertyDTO(new PropertyDTO());
        userSavedPropertyDTO2.setDeletedAt(LocalDateTime.now());

        List<UserSavedPropertyDTO> userSavedPropertyDTOList = Arrays.asList(userSavedPropertyDTO1, userSavedPropertyDTO2);

        when(userSavedPropertyService.getAllUserSavedProperties()).thenReturn(userSavedPropertyDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user-saved-properties")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[0].deletedAt").exists())
                .andExpect(jsonPath("$.[1].deletedAt").exists())
                .andReturn();

        verify(userSavedPropertyService, times(1)).getAllUserSavedProperties();
    }

    @Test
    void testIsPropertySavedByPropertyIdAndAlternativeUserId() throws Exception {
        Long propertyId = 1L;
        Long userId = 1L;
        when(userSavedPropertyService.isPropertySavedByPropertyIdAndAlternativeUserId(propertyId, userId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user-saved-properties/property/{propertyId}/user/{userId}", propertyId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true))
                .andReturn();

        verify(userSavedPropertyService, times(1)).isPropertySavedByPropertyIdAndAlternativeUserId(propertyId, userId);
    }

    @Test
    void testTogglePropertySavedByPropertyIdAndAlternativeUserId() throws Exception {
        Long propertyId = 1L;
        Long userId = 1L;

        when(userSavedPropertyService.toggleUserSavedProperty(propertyId, userId)).thenReturn(userSavedPropertyDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user-saved-properties/toggle/property/{propertyId}/user/{userId}", propertyId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.deletedAt").exists())
                .andReturn();

    }
}
