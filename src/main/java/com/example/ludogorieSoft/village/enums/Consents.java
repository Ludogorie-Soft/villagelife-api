package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Consents {
    COMPLETELY_AGREED("consents.completely_agreed",100, 1),
    RATHER_AGREE("consents.rather_agree", 80, 2),
    CANT_DECIDE("consents.cant_decide", 60, 3),
    RATHER_DISAGREE("consents.rather_disagree", 40, 4),
    DISAGREE("consents.disagree", 20, 5);
    private final String name;
    private final int value;
    private final int valueAsNumber;
}
