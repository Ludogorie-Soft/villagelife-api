package com.example.ludogorieSoft.village.dtos;

import com.example.ludogorieSoft.village.enums.InquiryType;
import lombok.*;

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
