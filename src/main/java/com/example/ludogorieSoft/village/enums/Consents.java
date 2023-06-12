package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Consents {
    COMPLETELY_AGREED("Напълно съгласен",100, 1),
    RATHER_AGREE("По-скоро съгласен", 80, 2),
    CANT_DECIDE("Не мога да преценя", 50, 3),
    RATHER_DISAGREE("По-скоро не съм съгласен", 30, 4),
    DISAGREE("Не съм съгласен", 0, 5);
    private final String name;
    private final int value;
    private final int valueAsNumber;
}
