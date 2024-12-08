package com.example.ludogorieSoft.village.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    private Long userId;
    private String token;
    private String password;
    private String repeatedPassword;
}
