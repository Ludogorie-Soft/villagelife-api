package com.example.ludogorieSoft.village.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
public class MessageDTO {
    private Long id;
    private String userName;
    private String email;
    private String userMessage;
}
