package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertyTransferType {
    SALE("property.transfer.type.sale"),
    RENT("property.transfer.type.rent");
    private final String key;
}
