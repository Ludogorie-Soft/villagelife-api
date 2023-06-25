package com.example.ludogoriesoft.village.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
public class Messages {
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
    private String message;
}
