package com.example.ludogorieSoft.village.model;

import com.example.ludogorieSoft.village.enums.OwnershipType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="property_users")
public class PropertyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ownership_type",columnDefinition="enum('INDIVIDUAL','AGENCY','BUILDER','INVESTOR')")
    @Enumerated(EnumType.STRING)
    private OwnershipType ownershipType;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Please enter a valid email address!")
    @Column(unique = true, nullable = false)
    private String email;

    @Length(min = 10, message = "Phone number should be at least 10 numbers long!")
    @Column(unique = true)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_search_data_id")
    private UserSearchData userSearchData;

    @NotBlank(message = "Password cannot be empty!")
    @Length(min = 8, message = "Password should be at least 8 characters long!")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @OneToOne
    private BusinessCard businessCard;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime deletedAt;

}
