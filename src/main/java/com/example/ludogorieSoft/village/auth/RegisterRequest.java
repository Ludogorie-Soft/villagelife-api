package com.example.ludogoriesoft.village.auth;

import com.example.ludogoriesoft.village.enums.Role;
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
