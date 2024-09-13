package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertyCondition {
    NEW("property.condition.new"),
    EXCELLENT("property.condition.excellent"),
    GOOD("property.condition.good"),
    FAIR("property.condition.fair"),
    POOR("property.condition.poor"),
    UNDER_CONSTRUCTION("property.condition.under.construction"),
    DAMAGED("property.condition.damaged"),
    RENOVATION_REQUIRED("property.condition.renovation.required");

    private final String key;
}
