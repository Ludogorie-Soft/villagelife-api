package com.example.ludogorieSoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorDTO {
    private Long id;
    private String fullName;
    private String email;
    private String username;
    private String password;
    private String mobile;
    private LocalDateTime createdAt;
    private final boolean enabled = true;

}
