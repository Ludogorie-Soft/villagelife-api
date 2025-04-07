package com.example.ludogorieSoft.village.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name cannot be empty!")
    @Length(min = 2, message = "Name should be at least than 2 characters long!")
    private String userName;
    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Please enter a valid email address!")
    private String email;
    @NotBlank
    private String userMessage;
    private LocalDateTime dateSent;
}
