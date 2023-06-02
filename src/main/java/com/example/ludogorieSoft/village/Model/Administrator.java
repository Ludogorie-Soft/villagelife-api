package com.example.ludogorieSoft.village.Model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "admins")
public class Administrator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name cannot be empty!")
    @Length(min = 2, message = "Full name should be at least than 2 characters long!")
    @Column(unique = true, nullable = false)
    private String fullName;

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Please enter a valid email address!")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Username cannot be empty!")
    @Length(max = 10, message = "Username should be less than 10 characters long!")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password cannot be empty!")
    @Length(min = 8, message = "Password should be at least 8 characters long!")
    @Column(unique = true, nullable = false)
    private String password;

    @Length(min = 10, message = "Phone number should be at least 10 numbers long!")
    @Column(unique = true)
    private String mobile;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private final boolean enabled = true;

}
