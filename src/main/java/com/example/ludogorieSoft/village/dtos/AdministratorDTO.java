package com.example.ludogoriesoft.village.dtos;

import com.example.ludogoriesoft.village.enums.Role;
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
    private String mobile;
    private LocalDateTime createdAt;
    private static final boolean ENABLED = true;

    private Role role;

}
