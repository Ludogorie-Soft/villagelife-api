package com.example.ludogoriesoft.village.dtos;

import com.example.ludogoriesoft.village.enums.Role;
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
    private Role role;

    private boolean enabled = true;
}
