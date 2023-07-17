package com.example.ludogorieSoft.village.dtos.request;

import com.example.ludogorieSoft.village.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String email;
    private String username;
    private String password;
    private String mobile;
    private Role role;
}
