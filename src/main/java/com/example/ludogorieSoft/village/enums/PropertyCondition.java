package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertyCondition {
/*
Bulgarian: ново, след цялостен ремонт, добро състояние, за ремонт, за цялостен ремонт, за събаряне

    English:
    new
    after complete renovation
    good
    needs repair
    needs complete renovation
    for demolition

    German:
    neu
    nach vollständiger Renovierung
    guter Zustand
    reparaturbedürftig
    vollständig renovierungsbedürftig
    zum Abriss
    */
    NEW("property.condition.new"),
    AFTER_COMPLETE_RENOVATION("after.complete.renovation"),
    GOOD("property.condition.good"),
    NEEDS_REPAIR("needs.repair"),
    NEEDS_COMPLETE_RENOVATION("needs.complete.renovation"),
    FOR_DEMOLITION("for.demolition");

    private final String key;
}
