package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Consents {
    CompletelyAgreed(100, 1),
    RatherAgree(80, 2),
    CantDecide(50, 3),
    RatherDisagree(30, 4),
    Disagree(0, 5);

    private final int value;
    private final int valueAsNumber;
}
