package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InquiryType {
    REQUEST_FOR_CONTACT ("Запитване за контакт в село"),
    LOOKING_FOR_BETTER_INDICATORS("Търся село с по-добри показатели"),
    JOB_OPPORTUNITIES("Възможности за работа"),
    OTHER("Друго");
    private final String name;
}
