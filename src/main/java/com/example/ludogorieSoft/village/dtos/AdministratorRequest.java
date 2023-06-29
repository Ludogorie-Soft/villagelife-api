package com.example.ludogorieSoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorRequest {
    private String fullName;
    private String email;
    private String username;
    private String password;
    private String mobile;
}
