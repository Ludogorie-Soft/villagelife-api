package com.example.ludogorieSoft.village.dtos;

import com.example.ludogorieSoft.village.enums.InquiryType;
import com.example.ludogorieSoft.village.model.Village;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InquiryDTO {
    private Long id;
    private String userName;
    private String email;
    private String userMessage;
    private String mobile;
    private Long villageId;
    private InquiryType inquiryType;
}
