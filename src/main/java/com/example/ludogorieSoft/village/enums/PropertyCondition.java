package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertyCondition {
    NEW("property.condition.new"),
    AFTER_COMPLETE_RENOVATION("after.complete.renovation"),
    GOOD("property.condition.good"),
    NEEDS_REPAIR("needs.repair"),
    NEEDS_COMPLETE_RENOVATION("needs.complete.renovation"),
    FOR_DEMOLITION("for.demolition");

    private final String key;
}
