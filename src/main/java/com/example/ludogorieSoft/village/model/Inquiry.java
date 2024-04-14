package com.example.ludogorieSoft.village.model;

import com.example.ludogorieSoft.village.enums.InquiryType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inquiries")
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Name cannot be empty!")
    @Length(min = 2, message = "Name should be at least than 2 characters long!")
    private String userName;

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Please enter a valid email address!")
    private String email;

    @NotBlank
    private String userMessage;

    @Length(min = 10, message = "Phone number should be at least 10 numbers long!")
    private String mobile;

    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;

    @Enumerated(EnumType.STRING)
    private InquiryType inquiryType;

    private LocalDateTime dateSent;
}
