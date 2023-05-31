package com.example.ludogorieSoft.village.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Consents {
    CompletelyAgreed(100),
    RatherAgree(80),
    CantDecide(50),
    RatherDisagree(30),
    Disagree(0);

    private int value;
}
