package com.example.ludogoriesoft.village.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageDTO {
    private Long id;
    private String userName;
    private String email;
    private String userMessage;
}
