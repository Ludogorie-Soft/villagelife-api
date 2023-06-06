package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Consents {
    COMPLETELY_AGREED(100, 1),
    RATHER_AGREE(80, 2),
    CANT_DECIDE(50, 3),
    RATHER_DISAGREE(30, 4),
    DISAGREE(0, 5);

    private final int value;
    private final int valueAsNumber;
}
