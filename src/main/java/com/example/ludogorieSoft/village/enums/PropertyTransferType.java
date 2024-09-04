package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertyTransferType {

    SALE("продажба"),
    RENT("наем");

    private final String name;
}
